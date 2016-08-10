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
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * This class defines an async way to run jKool queries via WebSockets. Supports standard queries and subscriptions.
 * 
 * @author albert
 */
public class JKQueryAsync extends JKQuery implements JKWSHandler, Closeable {
	public static final String SEARCH_QUERY_PREFIX = "get events where message contains \"%s\"";
	
	public static final String QUERY_KEY = "query";
	public static final String SUBID_KEY = "subid";
	public static final String ERROR_KEY = "query_error";
	public static final String CALL_CANCEL = "unsubscribe";
	public static final String CALL_KEY = "call";
	public static final String MAX_ROWS_KEY = "maxResultRows";
	public static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url",
	        "ws://jkool.jkoolcloud.com/jKool/jkqlasync");

	private static final String DEFAULT_QUERY = "SUBSCRIBE-TO-ORPHANS"; // dummy query associated with default response handler
	private static final ConcurrentMap<String, JKQueryHandle> SUBID_MAP = new ConcurrentHashMap<String, JKQueryHandle>();

	URI webSockUri;
	JKWSClient socket;
	JKConnectionHandler connectHandler;
	JKQueryCallback orphanHandler;

	public JKQueryAsync(String token) throws URISyntaxException {
		this(new URI(JKOOL_WEBSOCK_URL), JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(URI webSockUrl, String token) {
		this(webSockUrl, JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(String webSockUri, String token) throws URISyntaxException {
		this(new URI(webSockUri), JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(URI wsUri, String queryUrl, String token) {
		super(queryUrl, token);
		this.webSockUri = wsUri;
	}

	public JKQueryAsync setDefaultResponseHandler(JKQueryCallback callback) {
		this.orphanHandler = callback;
		createQueryHandle(DEFAULT_QUERY, callback);
		return this;
	}

	public JKQueryAsync setConnectionHandler(JKConnectionHandler cHandler) {
		this.connectHandler = cHandler;
		return this;
	}

	/**
	 * Return WebSocket connection handle
	 * 
	 * @return WebSocket connection handle
	 */
	public JKWSClient getConnection() {
		return this.socket;
	}

	/**
	 * Return total number of live query handles
	 * 
	 * @return total number of live query handles
	 */
	public static int getHandleCount() {
		return SUBID_MAP.size();
	}

	/**
	 * Obtain a subscription handle
	 * 
	 * @param id
	 *            subscription id
	 * @return query handle associated with subscription id
	 */
	public JKQueryHandle getHandle(String id) {
		return SUBID_MAP.get(id);
	}

	/**
	 * Obtain a subscription handle for the default response
	 * call back set by {{@link #setDefaultResponseHandler(JKQueryCallback)}
	 * 
	 * @return query handle associated with a default response handler
	 */
	public JKQueryHandle getDefaultHandle() {
		return SUBID_MAP.get(DEFAULT_QUERY);
	}

	/**
	 * Determine if current session is connected
	 * 
	 * @return true if connected, false otherwise
	 */
	public synchronized boolean isConnected() {
		return socket != null ? socket.isConnected() : false;
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
	 * Search for events that contain a given string.
	 * 
	 * @param searchText
	 *            search text
	 * @return callback callback class
	 * @throws IOException
	 */
	public JKQueryHandle searchAsync(String searchText, JKQueryCallback callback) throws IOException {
		return searchAsync(searchText, 100, callback);
	}

	/**
	 * Search for events that contain a given string.
	 * 
	 * @param searchText
	 *            search text
	 * @param maxRows
	 *            maximum rows to return
	 * @return callback callback class
	 * @throws IOException
	 */
	public JKQueryHandle searchAsync(String searchText, int maxRows, JKQueryCallback callback) throws IOException {
		return callAsync(String.format(SEARCH_QUERY_PREFIX, searchText), maxRows, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return callback callback class
	 * @throws IOException
	 */
	public JKQueryHandle subAsync(String query, JKQueryCallback callback) throws IOException {
		return callAsync(JKQueryHandle.SUB_QUERY_PREFIX + query, 100, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return callback callback class
	 * @throws IOException
	 */
	public JKQueryHandle callAsync(String query, JKQueryCallback callback) throws IOException {
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
	 * @throws IOException
	 */
	public JKQueryHandle callAsync(String query, int maxRows, JKQueryCallback callback) throws IOException {
		JKQueryHandle qhandle = createQueryHandle(query, callback);
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder.add(TOKEN_KEY, getToken())
				.add(QUERY_KEY, query)
				.add(MAX_ROWS_KEY, maxRows)
		        .add(SUBID_KEY, qhandle.getId()).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return qhandle;
	}

	/**
	 * Cancel all active subscriptions
	 * 
	 * @return itself
	 * @throws IOException
	 */
	public JKQueryAsync cancelAsyncAll() throws IOException {
		ArrayList<String> idList = new ArrayList<String>(SUBID_MAP.keySet());
		for (String id : idList) {
			cancelAsync(id);
		}
		return this;
	}

	/**
	 * Cancel a live subscription
	 * 
	 * @param handle
	 *            query handle {@#callAsync(String, JKQueryCallback)}
	 * @return un-subscription response
	 * @throws IOException
	 */
	public JKQueryHandle cancelAsync(JKQueryHandle handle) throws IOException {
		return cancelAsync(handle.getId());
	}

	/**
	 * Cancel a live subscription
	 * 
	 * @param subid
	 *            subscription id returned by {@#callAsync(String, JKQueryCallback)}
	 * @return un-subscription response
	 * @throws IOException
	 */
	public JKQueryHandle cancelAsync(String subid) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder.add(TOKEN_KEY, getToken())
		        .add(QUERY_KEY, JKQueryHandle.UNSUB_QUERY_PREFIX + subid)
		        .add(SUBID_KEY, subid).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return SUBID_MAP.get(subid);
	}

	@Override
	public void onMessage(JKWSClient client, String message) {
		JsonReader reader = Json.createReader(new StringReader(message));
		JsonObject jsonMessage = reader.readObject();
		String subid = jsonMessage.containsKey(JKQueryAsync.SUBID_KEY) ? jsonMessage.getString(JKQueryAsync.SUBID_KEY)
		        : null;
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

	protected JKQueryHandle createQueryHandle(String query, JKQueryCallback callback) {
		JKQueryHandle qhandle = new JKQueryHandle(query, callback);		
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}
	
	/**
	 * Handle async message response
	 * 
	 * @param subid
	 *            subscription id returned by {@#callAsync(String, JKQueryCallback)}
	 * @param response
	 *            JSON message response
	 * @return itself
	 */
	protected JKQueryAsync handleResponse(String subid, JsonObject response) {
		String qerror = response.getString(JKQueryAsync.ERROR_KEY, null);
		Throwable ex = ((qerror != null && !qerror.trim().isEmpty()) ? new JKApiException(100, qerror) : null);
		JKQueryHandle qhandle = (subid != null ? SUBID_MAP.get(subid) : null);
		String callName = response.getString(JKQueryAsync.CALL_KEY, "");

		try {
			if (qhandle != null) {
				qhandle.handle(qhandle, response, ex);
			} else if (orphanHandler != null) {
				orphanHandler.handle(qhandle, response, ex);
			}
			return this;
		} finally {
			cleanHandlers(callName, qhandle);
		}
	}

	private void cleanHandlers(String callName, JKQueryHandle qhandle) {
		if (qhandle != null) {
			if (qhandle.getQuery().equalsIgnoreCase(DEFAULT_QUERY)) {
				return;
			}
			if (!qhandle.isSubscribeQuery() || callName.equalsIgnoreCase(CALL_CANCEL)) {
				SUBID_MAP.remove(qhandle.getId());
			}
		}		
	}
	
	@Override
	public String toString() {
		return "{"
				+ "class: \"" + this.getClass().getName()
				+ "\", uri: \"" + webSockUri
				+ "\", socket: \"" + socket
		        + "\"}";
	}
}