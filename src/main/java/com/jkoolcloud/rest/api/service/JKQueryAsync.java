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
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;


public class JKQueryAsync extends JKQuery implements Closeable {
	//public static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url", "ws://11.0.0.40:8080/jKool/jkqlasync");
	public static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url", "ws://jkool.jkoolcloud.com/jKool/jkqlasync");
	
	private static ConcurrentMap<String, JKResultCallback> SUBID_MAP = new ConcurrentHashMap<String, JKResultCallback>();
	
	URI webSockUrl;
	WebsocketClient socket;
	
	public JKQueryAsync(String token) throws URISyntaxException, IOException {
		this(JKOOL_WEBSOCK_URL, JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(String webSockUrl, String token) throws URISyntaxException, IOException {
		this(webSockUrl, JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(String wsUrl, String queryUrl, String token) throws URISyntaxException, IOException {
		super(queryUrl, token);
		this.webSockUrl = new URI(wsUrl);
		connect();
	}
	
	/**
	 * Close all communication sessions
	 * 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public synchronized void connect() throws IOException {
		if (socket == null) {
			socket = new WebsocketClient(webSockUrl, new JKMessageHandlerImpl(this)); 
			socket.connect();
		}
	}
	
	/**
	 * Close all communication sessions
	 * 
	 * @throws IOException 
	 */
	public synchronized void close() throws IOException {
		if (socket != null) {
			socket.disconnect();
		}
		socket = null;
	}
	
	/**
	 * Call query in async mode using a callback
	 * 
	 * @param query JKQL query
	 * @return callback callback class
	 * @throws JKApiException
	 */
	public String call(String query, JKResultCallback callback, String rows) throws JKApiException, IOException {
		// Userid/password coming out. Token will be used instead.
		UUID uuid = UUID.randomUUID();
		String subid = uuid.toString();
		 JsonObject asyncSend = Json.createObjectBuilder()
	                .add("username", "xxxxx") // going away
	                .add("password", "xxxxx") // going away 
	                .add("repositoryId", "xxxxx") // going away
	                .add("query", query)
	                .add("maxResultRows", rows)
	                .add("subid", subid).build();

		
		

		socket.sendMessageAsync(asyncSend.toString());
		SUBID_MAP.put(subid, callback);
		return subid;
	}
	
	/**
	 * Cancel a live subscription
	 * 
	 * @param subid subscription id returned by {@#call(String, JKResultCallback)}
	 * @return un-subscription response
	 * @throws JKApiException
	 */
	public Response cancel(String subid) throws JKApiException {
		Response resp = call("unsubscribe " + subid);
		SUBID_MAP.remove(subid);
		return resp;
	}
	
	protected void routeResponse(String subid, String response) {
		JKResultCallback callBack = SUBID_MAP.get(subid);
		if (callBack != null) {
			callBack.handle(subid, response);
		}		
	}
	
	protected void routeError(Throwable error) {
		for (Entry<String, JKResultCallback> entry: SUBID_MAP.entrySet()) {
			entry.getValue().error(entry.getKey(), error);
		}
	}
}