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

import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

/**
 * This class defines a RESTFul way to run jKool queries. Supports standard queries only (does not support
 * subscriptions)
 * 
 * @author albert
 */
public class JKQuery extends JKService {
	HttpClient httpClient = HttpClients.createDefault();

	/**
	 * Create a jKool query service end-point with default end-point and access token
	 * 
	 */
	public JKQuery() {
		super(JKOOL_QUERY_URL, JKOOL_TOKEN);
	}

	/**
	 * Create a jKool query service end-point with default end-point and access token
	 * 
	 * @param token
	 *            security access token
	 */
	public JKQuery(String token) {
		super(JKOOL_QUERY_URL, token);
	}

	/**
	 * Create a jKool query service end-point with default end-point and access token
	 * 
	 * @param endPoint
	 *            URL end-point
	 * @param token
	 *            security access token
	 */
	public JKQuery(String endPoint, String token) {
		super(endPoint, token);
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String query) throws JKStreamException {
		return call(query, DEFAULT_MAX_ROWS);
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(JKStatement query) throws JKStreamException {
		return call(query.getQuery(), query.getMaxRows());
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public HttpResponse get(String query) throws JKStreamException {
		return get(query, DEFAULT_MAX_ROWS);
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public HttpResponse get(JKStatement query) throws JKStreamException {
		return get(query.getQuery(), query.getMaxRows());
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @param maxRows
	 *            maximum rows in response
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String query, int maxRows) throws JKStreamException {
		return target.queryParam(JK_QUERY_KEY, query).queryParam(JK_MAX_ROWS_KEY, maxRows)
				.queryParam(JK_TOKEN_KEY, getToken()).request(MediaType.APPLICATION_JSON)
				.header(JK_TOKEN_KEY, getToken()).get(Response.class);
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @param maxRows
	 *            maximum rows in response
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public HttpResponse get(String query, int maxRows) throws JKStreamException {
		try {
			String urlQuery = JK_QUERY_KEY + "=" + URLEncoder.encode(query, "UTF-8") + "&" + JK_MAX_ROWS_KEY + "="
					+ maxRows + "&" + JK_TOKEN_KEY + "=" + getToken();
			HttpGet request = new HttpGet(getServiceUrl() + "?" + urlQuery);
			// optionally, token can be in the header.
			request.addHeader(JK_TOKEN_KEY, getToken());
			request.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
			HttpResponse response = httpClient.execute(request);
			return response;
		} catch (Throwable e) {
			throw new JKStreamException(300, "Failed: path=" + getServiceUrl() + ", query=" + query, e);
		}
	}
}
