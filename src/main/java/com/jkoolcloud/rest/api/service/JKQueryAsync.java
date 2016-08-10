/*
 * Copyright 2014-2015 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkoolcloud.rest.api.service;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.Session;

public class JKQueryAsync extends JKQuery implements JKWSHandler, Closeable {
	public static final String QUERY_KEY = "query";
	public static final String SUBID_KEY = "subid";
	public static final String ERROR_KEY = "query_error";
	public static final String MAX_ROWS_KEY = "maxResultRows";
	public static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url",
			"ws://jkool.jkoolcloud.com/jKool/jkqlasync");

	private static ConcurrentMap<String, JKQueryHandle> SUBID_MAP = new ConcurrentHashMap<String, JKQueryHandle>();

	URI webSockUri;
	JKWSClient socket;
	JKConnectionHandler connectHandler;
	JKQueryCallback orphanHandler;
	
	public JKQueryAsync(String token) throws URISyntaxException {
		this(new URI(JKOOL_WEBSOCK_URL), JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(URI webSockUrl, String token){
		this(webSockUrl, JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(String webSockUri, String token) throws URISyntaxException {
		this(new URI(webSockUri), JKOOL_QUERY_URL, token);
	}
	
	public JKQueryAsync(URI wsUri, String queryUrl, String token) {
		super(queryUrl, token);
		this.webSockUri = wsUri;
	}

	public JKQueryAsync setDefaultResponseHandler(JKQueryCallback rhandler) {
		this.orphanHandler = rhandler;
		return this;
	}

	public JKQueryAsync setConnectionHandler(JKConnectionHandler cHandler) {
		this.connectHandler = cHandler;
		return this;
	}

	public JKWSClient getConnection() {
		return this.socket;
	}
	
	/**
	 * Close all communication sessions
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public synchronized boolean isConnected(){
		return socket != null? socket.isConnected(): false;
	}
	
	/**
	 * Close all communication sessions
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public synchronized JKQueryAsync connect() throws IOException {
		if (socket == null) {
			socket = new JKWSClient(webSockUri, this);
			socket.connect();
		}
		return this;
	}

	/**
	 * Close all communication sessions
	 * 
	 * @throws IOException
	 */
	@Override
	public synchronized void close() throws IOException {
		if (socket != null) {
			socket.disconnect();
		}
		socket = null;
	}
	
	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return callback callback class
	 * @throws JKApiException
	 */
	public JKQueryHandle subAsync(String query, JKQueryCallback callback) throws JKApiException, IOException {
		return callAsync(JKQueryHandle.SUB_QUERY_PREFIX + query, 100, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return callback callback class
	 * @throws JKApiException
	 */
	public JKQueryHandle callAsync(String query, JKQueryCallback callback) throws JKApiException, IOException {
		return callAsync(query, 100, callback);
	}

	/**
	 * Call query in async mode using a callback
	 * 
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            maximum rows to return
	 * @return callback callback class
	 * @throws JKApiException
	 */
	public JKQueryHandle callAsync(String query, int maxRows, JKQueryCallback callback) throws IOException {
		JKQueryHandle qhandle = new JKQueryHandle(query, callback);
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(TOKEN_KEY, getToken())
				.add(QUERY_KEY, query)
				.add(MAX_ROWS_KEY, Integer.toString(maxRows))
				.add(SUBID_KEY, qhandle.getId()).build();

		socket.sendMessageAsync(jsonQuery.toString());
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Cancel a live subscription
	 * 
	 * @param handle
	 *            query handle {@#call(String, JKQueryCallback)}
	 * @return un-subscription response
	 * @throws JKApiException
	 * @throws IOException 
	 */
	public JKQueryHandle cancelAsync(JKQueryHandle handle) throws IOException {
		return cancelAsync(handle.getId());
	}
	
	/**
	 * Cancel a live subscription
	 * 
	 * @param subid
	 *            subscription id returned by {@#call(String, JKQueryCallback)}
	 * @return un-subscription response
	 * @throws JKApiException
	 * @throws IOException 
	 */
	public JKQueryHandle cancelAsync(String subid) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(TOKEN_KEY, getToken())
				.add(QUERY_KEY, JKQueryHandle.UNSUB_QUERY_PREFIX + subid)
				.add(SUBID_KEY, subid).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return SUBID_MAP.remove(subid);
	}

	@Override
    public void onMessage(JKWSClient client, String message) {
		JsonReader reader = Json.createReader(new StringReader(message));
		JsonObject	jsonMessage = reader.readObject();
		String subid = jsonMessage.getString(JKQueryAsync.SUBID_KEY);
		handleResponse(subid, jsonMessage);
    }

	@Override
    public void onClose(JKWSClient client, Session userSession, CloseReason reason) {
		if (this.connectHandler != null) {
			connectHandler.close(this, reason);
		}
	}

	@Override
    public void onError(JKWSClient client, Session userSession, Throwable ex) {
		if (this.connectHandler != null) {
			connectHandler.error(this, ex);
		}
    }

	@Override
    public void onOpen(JKWSClient client, Session userSession) {
		if (this.connectHandler != null) {
			connectHandler.open(this);
		}
    }
	
	protected void handleResponse(String subid, JsonObject response) {
		JKQueryHandle qhandle = SUBID_MAP.get(subid);
		String qerror = response.containsKey(JKQueryAsync.ERROR_KEY) ? response.getString(JKQueryAsync.ERROR_KEY) : null;
		Throwable ex = qerror != null? new JKApiException(100, qerror): null;
		if (qhandle != null) {
			if (!qhandle.isSubscribeQ()) {
				SUBID_MAP.remove(subid);
			}
			qhandle.getCallback().handle(qhandle, response, ex);
		} else if (this.orphanHandler != null) {
			orphanHandler.handle(qhandle, response, ex);
		}
	}	
}