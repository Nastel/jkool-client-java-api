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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.CloseReason;

public class JKQueryAsync extends JKQuery implements Closeable {
	public static final String QUERY_KEY = "query";
	public static final String SUBID_KEY = "subid";
	public static final String MAX_ROWS_KEY = "maxResultRows";
	public static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url",
			"ws://jkool.jkoolcloud.com/jKool/jkqlasync");

	private static ConcurrentMap<String, QueryHandle> SUBID_MAP = new ConcurrentHashMap<String, QueryHandle>();

	URI webSockUri;
	WebsocketClient socket;
	JKConnectionHandler connectHandler;
	JKResultCallback orphanHandler;
	
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

	public JKQueryAsync setOrphanHandler(JKResultCallback rhandler) {
		this.orphanHandler = rhandler;
		return this;
	}

	public JKQueryAsync setConnectionHandler(JKConnectionHandler cHandler) {
		this.connectHandler = cHandler;
		return this;
	}

	public WebsocketClient getConnection() {
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
			socket = new WebsocketClient(webSockUri, new JKMessageHandlerImpl(this));
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
	public QueryHandle subAsync(String query, JKResultCallback callback) throws JKApiException, IOException {
		return callAsync(QueryHandle.SUB_QUERY_PREFIX + query, 100, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return callback callback class
	 * @throws JKApiException
	 */
	public QueryHandle callAsync(String query, JKResultCallback callback) throws JKApiException, IOException {
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
	public QueryHandle callAsync(String query, int maxRows, JKResultCallback callback) throws IOException {
		QueryHandle qhandle = new QueryHandle(query, callback);
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(TOKEN_KEY, getToken())
				.add(QUERY_KEY, query)
				.add(MAX_ROWS_KEY, maxRows)
				.add(SUBID_KEY, qhandle.getId()).build();

		socket.sendMessageAsync(jsonQuery.toString());
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Cancel a live subscription
	 * 
	 * @param handle
	 *            query handle {@#call(String, JKResultCallback)}
	 * @return un-subscription response
	 * @throws JKApiException
	 * @throws IOException 
	 */
	public QueryHandle cancelAsync(QueryHandle handle) throws IOException {
		return cancelAsync(handle.getId());
	}
	
	/**
	 * Cancel a live subscription
	 * 
	 * @param subid
	 *            subscription id returned by {@#call(String, JKResultCallback)}
	 * @return un-subscription response
	 * @throws JKApiException
	 * @throws IOException 
	 */
	public QueryHandle cancelAsync(String subid) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(TOKEN_KEY, getToken())
				.add(QUERY_KEY, QueryHandle.UNSUB_QUERY_PREFIX + subid)
				.add(SUBID_KEY, subid).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return SUBID_MAP.remove(subid);
	}

	protected void routeResponse(String subid, JsonObject response) {
		QueryHandle qhandle = SUBID_MAP.get(subid);
		if (qhandle != null) {
			if (!qhandle.isSubscribeQ()) {
				SUBID_MAP.remove(subid);
			}
			qhandle.getCallback().handle(qhandle, response);
		} else if (this.orphanHandler != null) {
			orphanHandler.handle(qhandle, response);
		}
	}

	protected void routeError(Throwable error) {
		if (this.connectHandler != null) {
			connectHandler.error(this, error);
		}
	}
	
	protected void routeOpen() {
		if (this.connectHandler != null) {
			connectHandler.open(this);
		}
	}

	protected void routeClose(CloseReason reason) {
		if (this.connectHandler != null) {
			connectHandler.close(this, reason);
		}
	}
}