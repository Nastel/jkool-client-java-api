/*
 * Copyright 2014-2018 JKOOL, LLC.
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
package com.jkoolcloud.client.api.service;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * This class defines an async way to run jKool queries via WebSockets. Supports standard queries and subscriptions.
 * 
 * @author albert
 */
public class JKQueryAsync extends JKQuery implements Closeable {
	private static final String DEFAULT_QUERY = "SUBSCRIBE TO ORPHANS"; // dummy query associated with default response
																		// handler
	private final ConcurrentMap<String, JKQueryHandle> SUBID_MAP = new ConcurrentHashMap<String, JKQueryHandle>();

	URI webSockUri;
	JKWSClient socket;
	WSClientHandler wsHandler;
	final Collection<JKQueryHandle> defCallbacks = Collections.synchronizedList(new ArrayList<JKQueryHandle>(5));
	final Collection<JKConnectionHandler> conHandlers = Collections
			.synchronizedList(new ArrayList<JKConnectionHandler>(5));

	/**
	 * Create a jKool asynchronous query service end-point
	 * 
	 * @param token
	 *            security access token
	 * @throws URISyntaxException
	 *             if target URL is invalid
	 */
	public JKQueryAsync(String token) throws URISyntaxException {
		this(new URI(JKOOL_WEBSOCK_URL), JKOOL_QUERY_URL, token);
	}

	/**
	 * Create a jKool asynchronous query service end-point
	 * 
	 * @param webSockUri
	 *            query service WebSocket URI
	 * @param token
	 *            security access token
	 * @throws URISyntaxException
	 *             if target URL is invalid
	 */
	public JKQueryAsync(String webSockUri, String token) throws URISyntaxException {
		this(new URI(webSockUri), JKOOL_QUERY_URL, token);
	}

	/**
	 * Create a jKool asynchronous query service end-point
	 * 
	 * @param webSockUrl
	 *            query service WebSocket URI
	 * @param token
	 *            security access token
	 */
	public JKQueryAsync(URI webSockUrl, String token) {
		this(webSockUrl, JKOOL_QUERY_URL, token);
	}

	/**
	 * Create a jKool asynchronous query service end-point
	 * 
	 * @param wsUri
	 *            query service WebSocket URI
	 * @param queryUrl
	 *            query service Restful URL
	 * @param token
	 *            security access token
	 */
	public JKQueryAsync(URI wsUri, String queryUrl, String token) {
		super(queryUrl, token);
		this.webSockUri = wsUri;
		this.wsHandler = new WSClientHandler(this);
	}

	/**
	 * Prepare a JQKL query statement with default max rows
	 * 
	 * @param query
	 *            JKQL query
	 * @param callback
	 *            associated with this query
	 * @return {@link JKStatement} instance
	 */
	public JKStatement prepare(String query, JKQueryCallback callback) {
		return new JKStatementImpl(this, query, DEFAULT_MAX_ROWS, callback);
	}

	/**
	 * Prepare a JQKL query statement
	 * 
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            maximum rows in the response result
	 * @param callback
	 *            associated with this query
	 * @return {@link JKStatement} instance
	 */
	public JKStatement prepare(String query, int maxRows, JKQueryCallback callback) {
		return new JKStatementImpl(this, query, maxRows, callback);
	}

	/**
	 * Obtain service URL for executing async queries
	 * 
	 * @return service URL for executing async queries
	 */
	public String getAsyncServiceUrl() {
		return webSockUri.toString();
	}

	/**
	 * Add a default callback handler for responses not handled by a specific query handler
	 * 
	 * @param callbacks
	 *            list of query callback handlers
	 * @return itself
	 */
	public JKQueryAsync addDefaultCallbackHandler(JKQueryCallback... callbacks) {
		if (callbacks == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i = 0; i < callbacks.length; i++) {
			defCallbacks.add(new JKQueryHandle(DEFAULT_QUERY, callbacks[i]));
		}
		return this;
	}

	/**
	 * Add a connection handler to the list of handlers
	 * 
	 * @param cHandlers
	 *            list of connection handlers
	 * @return itself
	 */
	public JKQueryAsync addConnectionHandler(JKConnectionHandler... cHandlers) {
		if (cHandlers == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i = 0; i < cHandlers.length; i++) {
			conHandlers.add(cHandlers[i]);
		}
		return this;
	}

	/**
	 * Remove a connection handler from the list of handlers
	 * 
	 * @param cHandlers
	 *            connection handler list
	 * @return itself
	 */
	public JKQueryAsync removeConnectionHandler(JKConnectionHandler... cHandlers) {
		if (cHandlers == null) {
			throw new IllegalArgumentException("list can not be null");
		}
		for (int i = 0; i < cHandlers.length; i++) {
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
		ArrayList<JKQueryHandle> list = new ArrayList<>(SUBID_MAP.values());
		return list;
	}

	/**
	 * Obtain a list of all default subscription handles
	 * 
	 * @return list of all default subscription handles
	 */
	public List<JKQueryHandle> getAllDefaultHandles() {
		ArrayList<JKQueryHandle> list = new ArrayList<>(defCallbacks);
		return list;
	}

	/**
	 * Determine if current session is connected
	 * 
	 * @return true if connected, false otherwise
	 */
	public synchronized boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	/**
	 * Close all communication sessions
	 * 
	 * @return self
	 * @throws IOException
	 *             on error during IO
	 */
	public synchronized JKQueryAsync connect() throws IOException {
		try {
			if (socket == null) {
				socket = new JKWSClient(webSockUri, wsHandler);
				socket.connect();
			} else if (!socket.isConnected()) {
				socket.connect();
			}
			return this;
		} catch (Throwable ex) {
			wsHandler.onError(socket, socket.getSession(), ex);
			throw ex;
		}
	}

	/**
	 * Close all communication sessions
	 * 
	 * @throws IOException
	 *             on error during IO
	 */
	@Override
	public synchronized void close() throws IOException {
		if (socket != null) {
			socket.disconnect();
		}
		socket = null;
	}

	/**
	 * Search for events that contain a given string and a maximum rows of {@link #DEFAULT_MAX_ROWS}.
	 * 
	 * @param searchText
	 *            search text
	 * @param callback
	 *            handle associated with response handling
	 * @return query handle associated with the query
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryHandle searchAsync(String searchText, JKQueryCallback callback) throws IOException {
		return searchAsync(searchText, DEFAULT_MAX_ROWS, callback);
	}

	/**
	 * Search for events that contain a given string.
	 * 
	 * @param searchText
	 *            search text
	 * @param maxRows
	 *            maximum rows to return
	 * @param callback
	 *            handle associated with response handling
	 * @return query handle associated with the query
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryHandle searchAsync(String searchText, int maxRows, JKQueryCallback callback) throws IOException {
		return callAsync(String.format(JK_SEARCH_QUERY_PREFIX, searchText), maxRows, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of {@link #DEFAULT_MAX_ROWS}.
	 * 
	 * @param query
	 *            JKQL query
	 * @param callback
	 *            handle associated with response handling
	 * @return query handle associated with the query
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryHandle subAsync(String query, JKQueryCallback callback) throws IOException {
		return callAsync(JK_SUB_QUERY_PREFIX + query, DEFAULT_MAX_ROWS, callback);
	}

	/**
	 * Call query in async mode using a callback and a maximum rows of {@link #DEFAULT_MAX_ROWS}.
	 * 
	 * @param query
	 *            JKQL query
	 * @param callback
	 *            handle associated with response handling
	 * @return query handle associated with the query
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryHandle callAsync(String query, JKQueryCallback callback) throws IOException {
		return callAsync(query, DEFAULT_MAX_ROWS, callback);
	}

	/**
	 * Call query in async mode using a callback
	 * 
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            maximum rows to return
	 * @param callback
	 *            handle associated with response handling
	 * @return query handle associated with the query
	 * @throws IOException
	 *             on error during IO
	 * @throws IllegalArgumentException
	 *             on bad arguments
	 */
	public JKQueryHandle callAsync(String query, int maxRows, JKQueryCallback callback) throws IOException {
		if (callback == null) {
			throw new IllegalArgumentException("callback can not be null");
		}
		JKQueryHandle qhandle = createQueryHandle(query, getTimeZone(), getDateRange(), getRepoId(), callback)
				.setMaxRows(maxRows)
				.setTrace(this.isTrace());
		return callAsync(qhandle);
	}

	/**
	 * Call query in async mode using a query handle
	 * 
	 * @param qhandle
	 *            JKQL query handle
	 * @throws IOException
	 *             on error during IO
	 * @throws IllegalArgumentException
	 *             on bad arguments
	 * @return query handle associated with the query
	 */
	public JKQueryHandle callAsync(JKQueryHandle qhandle) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(JK_TOKEN_KEY, getToken())
				.add(JK_QUERY_KEY, qhandle.getQuery())
				.add(JK_TIME_ZONE_KEY, qhandle.getTimeZone())
				.add(JK_DATE_KEY, qhandle.getDateRange())
				.add(JK_REPO_KEY, qhandle.getRepoId())
				.add(JK_MAX_ROWS_KEY, qhandle.getMaxRows())
				.add(JK_TRACE_KEY, qhandle.isTrace())
				.add(JK_SUBID_KEY, qhandle.getId()).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return qhandle;
	}

	/**
	 * Call query in async mode using default callback(s). All responses will be tagged with auto generated id and
	 * routed to all registered default handlers.
	 * 
	 * @param query
	 *            JKQL query
	 * @return itself
	 * @throws IOException
	 *             on error during IO
	 * @throws IllegalArgumentException
	 *             on bad arguments
	 */
	public JKQueryAsync callAsync(String query) throws IOException {
		return callAsync(query, JKQueryHandle.newId(query), DEFAULT_MAX_ROWS);
	}

	/**
	 * Call query in async mode using default callback(s). All responses will be tagged with auto generated id and
	 * routed to all registered default handlers.
	 * 
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            maximum rows to return
	 * @return itself
	 * @throws IOException
	 *             on error during IO
	 * @throws IllegalArgumentException
	 *             on bad arguments
	 */
	public JKQueryAsync callAsync(String query, int maxRows) throws IOException {
		return callAsync(query, JKQueryHandle.newId(query), maxRows);
	}

	/**
	 * Call query in async mode using default callback(s). All responses will be tagged with given id and routed to all
	 * registered default handlers.
	 * 
	 * @param query
	 *            JKQL query
	 * @param id
	 *            tag associated with the query
	 * @param maxRows
	 *            maximum rows to return
	 * @return itself
	 * @throws IOException
	 *             on error during IO
	 * @throws IllegalArgumentException
	 *             on bad arguments
	 */
	public JKQueryAsync callAsync(String query, String id, int maxRows) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(JK_TOKEN_KEY, getToken())
				.add(JK_QUERY_KEY, query)
				.add(JK_TIME_ZONE_KEY, getTimeZone())
				.add(JK_DATE_KEY, getDateRange())
				.add(JK_REPO_KEY, getRepoId())
				.add(JK_MAX_ROWS_KEY, maxRows)
				.add(JK_TRACE_KEY, isTrace())
				.add(JK_SUBID_KEY, id).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return this;
	}

	/**
	 * Cancel all active subscriptions
	 * 
	 * @return itself
	 * @throws IOException
	 *             on error during IO
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
	 *            query handle {#callAsync(String, JKQueryCallback)}
	 * @return un-subscription response
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryHandle cancelAsync(JKQueryHandle handle) throws IOException {
		return cancelAsync(handle.getId());
	}

	/**
	 * Cancel a live subscription
	 * 
	 * @param subid
	 *            subscription id returned by {#callAsync(String, JKQueryCallback)}
	 * @return un-subscription response
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryHandle cancelAsync(String subid) throws IOException {
		if (subid == null) {
			throw new IllegalArgumentException("subscription id can not be null");
		}
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder
				.add(JK_TOKEN_KEY, getToken())
				.add(JK_QUERY_KEY, JKQueryHandle.JK_UNSUB_QUERY_PREFIX)
				.add(JK_MAX_ROWS_KEY, 10)
				.add(JK_SUBID_KEY, subid).build();

		socket.sendMessageAsync(jsonQuery.toString());
		return SUBID_MAP.get(subid);
	}

	/**
	 * Create a new query handle for a given callback instance and query.
	 * 
	 * @param query
	 *            JKQL query
	 * @param callback
	 *            callback associated with the query
	 * @return itself
	 */
	protected JKQueryHandle createQueryHandle(String query, JKQueryCallback callback) {
		JKQueryHandle qhandle = new JKQueryHandle(query, getTimeZone(), getDateRange(), getRepoId(), callback);
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Create a new query handle for a given callback instance and query.
	 * 
	 * @param query
	 *            JKQL query
	 * @param tz
	 *            JKQL query timezone
	 * @param drange
	 *            JKQL query date range
	 * @param repo
	 *            JKQL query repo (null if default)
	 * @param callback
	 *            callback associated with the query
	 * @return itself
	 */
	protected JKQueryHandle createQueryHandle(String query, String tz, String drange, String repo, JKQueryCallback callback) {
		JKQueryHandle qhandle = new JKQueryHandle(query, tz, drange, repo, callback);
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Restore subscriptions (re-subscribe) based on a given gate. Subscribe when
	 * {@code JKGate<JKQueryHandle>.check(JKQueryHandle)} return true, skip otherwise.
	 * 
	 * @param hGate
	 *            query handle gate check for true or false
	 * @return itself
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryAsync restoreSubscriptions(JKGate<JKQueryHandle> hGate) throws IOException {
		ArrayList<JKQueryHandle> handleList = new ArrayList<JKQueryHandle>(SUBID_MAP.values());
		for (JKQueryHandle handle : handleList) {
			if (hGate.check(handle)) {
				// restore subscription
				callAsync(handle);
			} else {
				// remove standard query subscription since none will ever be coming
				SUBID_MAP.remove(handle.getId());
				handle.done();
			}
		}
		return this;
	}

	/**
	 * Invoke default handles with a given response, exception
	 * 
	 * @param response
	 *            JSON message response
	 * @param ex
	 *            exception
	 */
	protected void invokeDefaultHandles(JsonObject response, Throwable ex) {
		for (JKQueryHandle handle : defCallbacks) {
			handle.handle(response, ex);
		}
	}

	/**
	 * Handle async message response
	 * 
	 * @param subid
	 *            subscription id returned by {#callAsync(String, JKQueryCallback)}
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
				qhandle.done();
			}
		}
	}

	@Override
	public String toString() {
		return "{" + "class: \"" + this.getClass().getSimpleName() + "\", uri: \"" + webSockUri + "\"}";
	}
}
