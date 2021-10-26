/*
 * Copyright 2014-2021 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkoolcloud.client.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.jkoolcloud.client.api.utils.JKUtils;

/**
 * This class defines an async way to run jKool queries via WebSockets. Supports standard queries and subscriptions.
 * 
 * @author albert
 */
public class JKQueryAsync extends JKQuery {
	private static final String DEFAULT_QUERY = "SUBSCRIBE TO ORPHANS"; // dummy query associated with default response
																		// handler
	private final ConcurrentMap<String, JKStatementAsyncImpl> SUBID_MAP = new ConcurrentHashMap<>();

	private URI wsURI;
	private JKWSClient socket;
	private WSClientHandler wsHandler;
	
	final Collection<JKStatementAsyncImpl> defCallbacks = Collections.synchronizedList(new ArrayList<>(5));
	final Collection<JKConnectionHandler> conHandlers = Collections.synchronizedList(new ArrayList<>(5));

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
		this.wsURI = wsUri;
		this.wsHandler = new WSClientHandler(this);
	}

	/**
	 * Prepare a JQKL query statement with default max rows
	 * 
	 * @param query
	 *            JKQL query
	 * @param callback
	 *            associated with this query
	 * @return {@link JKStatementAsync} instance
	 */
	public JKStatementAsync prepare(String query, JKQueryCallback callback) {
		return new JKStatementAsyncImpl(this, query, DEFAULT_MAX_ROWS, callback);
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
	 * @return {@link JKStatementAsync} instance
	 */
	public JKStatementAsync prepare(String query, int maxRows, JKQueryCallback callback) {
		return new JKStatementAsyncImpl(this, query, maxRows, callback);
	}

	/**
	 * Obtain service URL for executing async queries
	 * 
	 * @return service URL for executing async queries
	 */
	public String getAsyncServiceUrl() {
		return wsURI.toString();
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
		for (JKQueryCallback callback : callbacks) {
			defCallbacks.add((JKStatementAsyncImpl)prepare(DEFAULT_QUERY, callback));
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
		Collections.addAll(conHandlers, cHandlers);
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
		for (JKConnectionHandler cHandler : cHandlers) {
			conHandlers.remove(cHandler);
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
	public JKStatementAsync getHandle(String id) {
		return SUBID_MAP.get(id);
	}

	/**
	 * Obtain a list of all active subscription handles
	 * 
	 * @return query handle associated with a default response handler
	 */
	public List<JKStatementAsync> getAllHandles() {
		ArrayList<JKStatementAsync> list = new ArrayList<>(SUBID_MAP.values());
		return list;
	}

	/**
	 * Obtain a list of all default subscription handles
	 * 
	 * @return list of all default subscription handles
	 */
	public List<JKStatementAsync> getAllDefaultHandles() {
		ArrayList<JKStatementAsync> list = new ArrayList<>(defCallbacks);
		return list;
	}

	/**
	 * Determine if current session is connected
	 * 
	 * @return true if connected, false otherwise
	 */
	@Override
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
				socket = new JKWSClient(wsURI, wsHandler);
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

	@Override
	public synchronized void close() throws IOException {
		if (socket != null) {
			socket.disconnect();
			socket = null;
		}
		super.close();
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
	public JKStatementAsync searchAsync(String searchText, JKQueryCallback callback) throws IOException {
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
	public JKStatementAsync searchAsync(String searchText, int maxRows, JKQueryCallback callback) throws IOException {
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
	public JKStatementAsync subAsync(String query, JKQueryCallback callback) throws IOException {
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
	public JKStatementAsync callAsync(String query, JKQueryCallback callback) throws IOException {
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
	public JKStatementAsync callAsync(String query, int maxRows, JKQueryCallback callback) throws IOException {
		if (callback == null) {
			throw new IllegalArgumentException("callback can not be null");
		}
		JKStatementAsync qHandle = createQueryHandle(query, maxRows, callback);
		return callAsync(qHandle);
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
	public JKStatementAsync callAsync(JKStatementAsync qhandle) throws IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder //
				.add(JK_TOKEN_KEY, getToken()) //
				.add(JK_QUERY_KEY, qhandle.getQuery()) //
				.add(JK_TIME_ZONE_KEY, qhandle.getTimeZone()) //
				.add(JK_DATE_KEY, qhandle.getDateRange()) //
				.add(JK_REPO_KEY, qhandle.getRepoId()) //
				.add(JK_MAX_ROWS_KEY, qhandle.getMaxRows()) //
				.add(JK_TRACE_KEY, qhandle.isTrace()) //
				.add(JK_SUBID_KEY, qhandle.getId()) //
				.add(X_REFERER, qhandle.getReferrer()) //
				.add(X_API_HOSTNAME, JKUtils.VM_HOST) //
				.add(X_API_HOSTADDR, JKUtils.VM_NETADDR) //
				.add(X_API_RUNTIME, JKUtils.VM_NAME) //
				.add(X_API_VERSION, QAPI_CLIENT_VERSION) //
				.build();

		if (qhandle.getCallback() != null) {
			qhandle.getCallback().onCall(qhandle, jsonQuery);
		}
		sendJsonQuery(jsonQuery);
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
		return callAsync(query, newId(query), DEFAULT_MAX_ROWS);
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
		return callAsync(query, newId(query), maxRows);
	}

	/**
	 * Call query in async mode using default callback(s). 
	 * All responses will be tagged with given id and routed to all registered default handlers.
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
		JsonObject jsonQuery = jsonBuilder //
				.add(JK_TOKEN_KEY, getToken()) //
				.add(JK_QUERY_KEY, query) //
				.add(JK_TIME_ZONE_KEY, getTimeZone()) //
				.add(JK_DATE_KEY, getDateRange()) //
				.add(JK_REPO_KEY, getRepoId()) //
				.add(JK_MAX_ROWS_KEY, maxRows) //
				.add(JK_TRACE_KEY, isTrace()) //
				.add(JK_SUBID_KEY, id) //
				.add(X_API_HOSTNAME, JKUtils.VM_HOST) //
				.add(X_API_HOSTADDR, JKUtils.VM_NETADDR) //
				.add(X_API_RUNTIME, JKUtils.VM_NAME) //
				.add(X_API_VERSION, QAPI_CLIENT_VERSION) //
				.build();

		sendJsonQuery(jsonQuery);
		return this;
	}

	/**
	 * Close all active handles
	 * 
	 * @return itself
	 * @throws IOException
	 *             on error during IO
	 */
	public JKQueryAsync closeAll() throws IOException {
		ArrayList<JKStatementAsync> idList = new ArrayList<>(SUBID_MAP.values());
		for (JKStatementAsync handle : idList) {
			try { handle.close(); }
			catch (Throwable e) {}
		}
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
		ArrayList<JKStatementAsync> idList = new ArrayList<>(SUBID_MAP.values());
		for (JKStatementAsync handle : idList) {
			cancelAsync(handle);
		}
		return this;
	}

	/**
	 * Cancel a live subscription
	 * 
	 * @param handle
	 *            query handle {#callAsync(String, JKQueryCallback)}
	 * @return query handle associated with subscription
	 * @throws IOException
	 *             on error during IO
	 */
	public JKStatementAsync cancelAsync(JKStatementAsync handle) throws IOException {
		if (handle == null) {
			throw new IllegalArgumentException("handle id can not be null");
		}
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		JsonObject jsonQuery = jsonBuilder //
				.add(JK_TOKEN_KEY, getToken()) //
				.add(JK_TIME_ZONE_KEY, getTimeZone()) //
				.add(JK_REPO_KEY, getRepoId()) //
				.add(JK_MAX_ROWS_KEY, handle.getMaxRows())//
				.add(JK_TRACE_KEY, isTrace())//
				.add(JK_QUERY_KEY, JKQIConstants.JK_UNSUB_QUERY_PREFIX + "'" + handle.getLastMsgId() + "'") //
				.add(JK_SUBID_KEY, handle.getId()) //
				.add(X_API_HOSTNAME, JKUtils.VM_HOST) //
				.add(X_API_HOSTADDR, JKUtils.VM_NETADDR) //
				.add(X_API_RUNTIME, JKUtils.VM_NAME) //
				.add(X_API_VERSION, QAPI_CLIENT_VERSION) //
				.build();

		if (handle.getCallback() != null) {
			handle.getCallback().onCall(handle, jsonQuery);
		}
		sendJsonQuery(jsonQuery);
		return SUBID_MAP.get(handle.getId());
	}

	/**
	 * Close query handle
	 * 
	 * @param handle
	 *            query handle
	 */
	public void close(JKStatementAsync handle) {
		SUBID_MAP.remove(handle.getId());
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
	public JKQueryAsync restoreSubscriptions(JKGate<JKStatementAsync> hGate) throws IOException {
		ArrayList<JKStatementAsync> handleList = new ArrayList<>(SUBID_MAP.values());
		for (JKStatementAsync handle : handleList) {
			if (hGate.check(handle)) {
				// restore subscription
				callAsync(handle);
			} else {
				// remove standard query subscription since none will ever be coming
				handle.close();
			}
		}
		return this;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {uri: \"" + wsURI + "\", socket: \"" + socket + "\"}";
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
	protected JKStatementAsync createQueryHandle(String query, JKQueryCallback callback) {
		JKStatementAsyncImpl qhandle = (JKStatementAsyncImpl) prepare(query, callback);
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Create a new query handle for a given callback instance and query.
	 * 
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            JKQL query max rows
	 * @param callback
	 *            callback associated with the query
	 * @return itself
	 */
	protected JKStatementAsync createQueryHandle(String query, int maxRows, JKQueryCallback callback) {
		JKStatementAsyncImpl qhandle = (JKStatementAsyncImpl) prepare(query, maxRows, callback);
		SUBID_MAP.put(qhandle.getId(), qhandle);
		return qhandle;
	}

	/**
	 * Send JSON query via a WebSocket
	 * 
	 * @param jsonQuery
	 *            JSON query
	 * @return WebSocket client
	 * @throws IOException
	 *             on error during IO
	 */
	private JKWSClient sendJsonQuery(JsonObject jsonQuery) throws IOException {
		if (socket != null) {
			return socket.sendMessageAsync(jsonQuery.toString());
		} else {
			throw new IllegalStateException("Connection closed");
		}
	}

	/**
	 * Create a unique identifier for a given query
	 * 
	 * @param q
	 *            JKQL query
	 * @return a unique identifier
	 */
	protected static String newId(String q) {
		String uuid = UUID.randomUUID().toString();
		uuid = JKUtils.isSubscribeQ(q) ? JK_SUB_UUID_PREFIX + uuid : uuid;
		return uuid;
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
		for (JKStatementAsyncImpl stmt : defCallbacks) {
			stmt.onResponse(response, ex);
		}
	}

	/**
	 * Handle async message response
	 * 
	 * @param subId
	 *            subscription id returned by {#callAsync(String, JKQueryCallback)}
	 * @param response
	 *            JSON message response
	 * @return itself
	 */
	protected JKQueryAsync handleResponse(String subId, JsonObject response) {
		String qerror = response.getString(JKQueryAsync.JK_ERROR_KEY, null);
		Throwable ex = !isEmpty(qerror) ? new JKStreamException(100, qerror) : null;
		JKStatementAsyncImpl qhandle = (subId != null ? SUBID_MAP.get(subId) : null);
		String callName = response.getString(JKQueryAsync.JK_CALL_KEY, "");

		try {
			if (qhandle != null) {
				qhandle.onResponse(response, ex);
			} else if (defCallbacks.size() > 0) {
				invokeDefaultHandles(response, ex);
			}
			return this;
		} finally {
			complete(callName, qhandle);
		}
	}

	private void complete(String callName, JKStatementAsyncImpl qhandle) {
		if (qhandle != null) {
			if (!qhandle.isSubscribe() || callName.equalsIgnoreCase(JK_CALL_CANCEL)) {
				qhandle.onDone();
			}
		}
	}
}
