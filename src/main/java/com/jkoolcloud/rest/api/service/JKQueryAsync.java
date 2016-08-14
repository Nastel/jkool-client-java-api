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
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * This class defines an async way to run jKool queries via WebSockets. Supports
 * standard queries and subscriptions.
 * 
 * @author albert
 */
public class JKQueryAsync extends JKQuery implements JKWSHandler, Closeable {
	private static final String DEFAULT_QUERY = "SUBSCRIBE TO ORPHANS"; // dummy
																		// query
																		// associated
																		// with
																		// default
																		// response
																		// handler
	private final ConcurrentMap<String, JKQueryHandle> SUBID_MAP = new ConcurrentHashMap<String, JKQueryHandle>();

	URI webSockUri;
	JKWSClient socket;

	Vector<JKQueryHandle> defCallbacks = new Vector<JKQueryHandle>(5, 5);
	Vector<JKConnectionHandler> conHandlers = new Vector<JKConnectionHandler>(5, 5);

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

	/**
	 * Add a default callback handler for responses
	 * not handled by a specific query handler
	 * 
	 * @param callbacks list of query callback handlers
	 * @return itself
	 */
	public JKQueryAsync addDefaultCallbackHandler(JKQueryCallback...callbacks) {
		if (callbacks == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i=0; i < callbacks.length; i++) {
			defCallbacks.add(new JKQueryHandle(DEFAULT_QUERY, callbacks[i]));	
		}
		return this;
	}

	/**
	 * Remove a callback handler from the list of default handlers
	 * 
	 * @param callbacks list of callback handlers
	 * @return itself
	 */
	public JKQueryAsync removeConnectionHandler(JKQueryCallback...callbacks) {
		if (callbacks == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i=0; i < callbacks.length; i++) {
			conHandlers.remove(callbacks[i]);
		}
		return this;
	}

	/**
	 * Add a connection handler to the list of handlers
	 * 
	 * @param cHandlers list of connection handlers
	 * @return itself
	 */
	public JKQueryAsync addConnectionHandler(JKConnectionHandler...cHandlers) {
		if (cHandlers == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i=0; i < cHandlers.length; i++) {
			conHandlers.add(cHandlers[i]);
		}
		return this;
	}

	/**
	 * Remove a connection handler from the list of handlers
	 * 
	 * @param cHandlers connection handler list
	 * @return itself
	 */
	public JKQueryAsync removeConnectionHandler(JKConnectionHandler...cHandlers) {
		if (cHandlers == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i=0; i < cHandlers.length; i++) {
			conHandlers.remove(cHandlers[i]);
		}
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
	public int getHandleCount() {
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
	 * Obtain a list of all active subscription handles
	 * 
	 * @return query handle associated with a default response handler
	 */
	public List<JKQueryHandle> getAllHandles() {
		ArrayList<JKQueryHandle> list = new ArrayList<JKQueryHandle>(SUBID_MAP.values());
		return list;
	}

	/**
	 * Obtain a list of all default subscription handles
	 * 
	 * @return list of all default subscription handles
	 */
	public List<JKQueryHandle> getAllDefaultHandles() {
		ArrayList<JKQueryHandle> list = new ArrayList<JKQueryHandle>(defCallbacks);
		return list;
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
		try {
			if (socket == null) {
				socket = new JKWSClient(webSockUri, this);
				socket.connect();
			} else if (!socket.isConnected()) {
				socket.connect();				
			}
			return this;
		} catch (Throwable ex) {
			onError(socket, socket.getSession(), ex);
			throw ex;
		}
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
	 * @return query handle associated with the query
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
	 * @return query handle associated with the query
	 * @throws IOException
	 */
	public JKQueryHandle searchAsync(String searchText, int maxRows, JKQueryCallback callback) throws IOException {
		return callAsync(String.format(JK_SEARCH_QUERY_PREFIX, searchText), maxRows, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return query handle associated with the query
	 * @throws IOException
	 */
	public JKQueryHandle subAsync(String query, JKQueryCallback callback) throws IOException {
		return callAsync(JK_SUB_QUERY_PREFIX + query, 100, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of 100.
	 * 
	 * @param query
	 *            JKQL query
	 * @return query handle associated with the query
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
	 * @return query handle associated with the query
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public JKQueryHandle callAsync(String query, int maxRows, JKQueryCallback callback) throws IOException {
		if (callback == null) {
			throw new IllegalArgumentException("callback can not be null");
		}
		JKQueryHandle qhandle = createQueryHandle(query, callback).setMaxRows(maxRows);
		return callAsync(qhandle);
	}

	/**
	 * Call query in async mode using a query handle
	 * 
	 * @param qhandle
	 *            JKQL query handle
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @return query handle associated with the query
	 */
	public JKQueryHandle callAsync(JKQueryHandle qhandle) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder.add(JK_TOKEN_KEY, getToken())
				.add(JK_QUERY_KEY, qhandle.getQuery())
				.add(JK_MAX_ROWS_KEY, qhandle.getMaxRows())
				.add(JK_SUBID_KEY, qhandle.getId()).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return qhandle;
	}

	/**
	 * Call query in async mode using a callback
	 * All responses will be tagged with given id and
	 * routed to all registered default handlers.
	 * 
	 * @param query
	 *            JKQL query
	 * @param id
	 *            tag associated with the query
	 * @param maxRows
	 *            maximum rows to return
	 * @return itself
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public JKQueryAsync callAsync(String query, String id, int maxRows) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder.add(JK_TOKEN_KEY, getToken())
				.add(JK_QUERY_KEY, query)
				.add(JK_MAX_ROWS_KEY, maxRows)
				.add(JK_SUBID_KEY, id).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return this;
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
	 *            subscription id returned by {@#callAsync(String,
	 *            JKQueryCallback)}
	 * @return un-subscription response
	 * @throws IOException
	 */
	public JKQueryHandle cancelAsync(String subid) throws IOException {
		if (subid == null) {
			throw new IllegalArgumentException("subscription id can not be null");
		}
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder.add(JK_TOKEN_KEY, getToken())
				.add(JK_QUERY_KEY, JKQueryHandle.JK_UNSUB_QUERY_PREFIX)
				.add(JK_MAX_ROWS_KEY, 10).add(JK_SUBID_KEY, subid)
				.build();

		socket.sendMessageAsync(jsonQuery.toString());
		return SUBID_MAP.get(subid);
	}

	@Override
	public void onMessage(JKWSClient client, String message) {
		JsonReader reader = Json.createReader(new StringReader(message));
		JsonObject jsonMessage = reader.readObject();
		String subid = jsonMessage.getString(JKQueryAsync.JK_SUBID_KEY, null);
		handleResponse(subid, jsonMessage);
	}

	@Override
	public void onClose(JKWSClient client, Session userSession, CloseReason reason) {
		synchronized (conHandlers) {
			for (JKConnectionHandler ch: conHandlers) {
				ch.close(this, reason);
			}
		}
	}

	@Override
	public void onError(JKWSClient client, Session userSession, Throwable ex) {
		synchronized (conHandlers) {
			for (JKConnectionHandler ch: conHandlers) {
				ch.error(this, ex);
			}
		}
	}

	@Override
	public void onOpen(JKWSClient client, Session userSession) {
		synchronized (conHandlers) {
			for (JKConnectionHandler ch: conHandlers) {
				ch.open(this);
			}
		}
	}

	/**
	 * Create a new query handle for a given callback instance and
	 * query.
	 * 
	 * @param query
	 *            JKQL query
	 * @param callback
	 *            callback associated with the query
	 * @return itself
	 */
	protected JKQueryHandle createQueryHandle(String query, JKQueryCallback callback) {
		JKQueryHandle qhandle = new JKQueryHandle(query, callback);
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Restore subscriptions (re-subscribe) based on a
	 * given gate. Subscribe when {@code JKGate<JKQueryHandle>.check(JKQueryHandle)} 
	 * return true, skip otherwise.
	 * 
	 * @param hGate
	 *            query handle gate check for true or false
	 * @return itself
	 */
	public JKQueryAsync restoreSubscriptions(JKGate<JKQueryHandle> hGate) throws IOException {
		ArrayList<JKQueryHandle> handleList = new ArrayList<JKQueryHandle>(SUBID_MAP.values());
		for (JKQueryHandle handle: handleList) {
			if (hGate.check(handle)) {
				// restore subscription
				callAsync(handle);
			} else {
				// remove standard query subscription since none will ever be coming
				SUBID_MAP.remove(handle.getId());
				handle.dead();
			}
		}
		return this;
    }

	/**
	 * Invoke default handles with a given response, exception
	 * 
	 * @param response
	 *            JSON message response
	 * @param ex exception
	 * @return itself
	 */
	protected void invokeDefaultHandles(JsonObject response, Throwable ex) {
		for (JKQueryHandle handle: defCallbacks) {
			handle.handle(response, ex);
		}		
	}
	
	/**
	 * Handle async message response
	 * 
	 * @param subid
	 *            subscription id returned by {@#callAsync(String,
	 *            JKQueryCallback)}
	 * @param response
	 *            JSON message response
	 * @return itself
	 */
	protected JKQueryAsync handleResponse(String subid, JsonObject response) {
		String qerror = response.getString(JKQueryAsync.JK_ERROR_KEY, null);
		Throwable ex = ((qerror != null && !qerror.trim().isEmpty()) ? new JKStreamException(100, qerror) : null);
		JKQueryHandle qhandle = (subid != null ? SUBID_MAP.get(subid) : null);
		String callName = response.getString(JKQueryAsync.JK_CALL_KEY, "");

		try {
			if (qhandle != null) {
				qhandle.handle(response, ex);
			} else if (defCallbacks.size() > 0) {
				invokeDefaultHandles(response, ex);
			}
			return this;
		} finally {
			cleanHandlers(callName, qhandle);
		}
	}

	private void cleanHandlers(String callName, JKQueryHandle qhandle) {
		if (qhandle != null) {
			if (!qhandle.isSubscribeQuery() || callName.equalsIgnoreCase(JK_CALL_CANCEL)) {
				SUBID_MAP.remove(qhandle.getId());
				qhandle.dead();
			}
		}
	}

	@Override
	public String toString() {
		return "{" + "class: \"" + this.getClass().getSimpleName() + "\", uri: \"" + webSockUri + "\"}";
	}
}