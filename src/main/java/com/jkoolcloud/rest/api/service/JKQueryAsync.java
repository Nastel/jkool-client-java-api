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

import java.net.URISyntaxException;

import javax.ws.rs.core.Response;


public class JKQueryAsync extends JKQuery {
	public static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url", "wss://jkool.jkoolcloud.com/jKool/");
	
	String webSockUrl;
	WebsocketClient socket;
	
	public JKQueryAsync(String token) throws URISyntaxException {
		this(JKOOL_WEBSOCK_URL, JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(String webSockUrl, String token) throws URISyntaxException {
		this(webSockUrl, JKOOL_QUERY_URL, token);
	}

	public JKQueryAsync(String webSockUrl, String queryUrl, String token) throws URISyntaxException {
		super(queryUrl, token);
		socket = new WebsocketClient(webSockUrl);
	}
	
	/**
	 * Call query in async mode using a callback
	 * 
	 * @param query JKQL query
	 * @return callback callback class
	 * @throws JKApiException
	 */
	public String call(String query, JKResultCallback callback) throws JKApiException {
		return null;
	}
	
	/**
	 * Cancel a live subscription
	 * 
	 * @param subid subscription id returned by {@#call(String, JKResultCallback)}
	 * @return un-subscription response
	 * @throws JKApiException
	 */
	public Response cancel(String subid) throws JKApiException {
		return super.call("unsubscribe " + subid);
	}
}
