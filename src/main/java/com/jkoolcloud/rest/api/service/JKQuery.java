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

import java.net.URLEncoder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This class defines a RESTFul way to run jKool queries.
 * Supports standard queries only (does not support subscriptions)
 * 
 * @author albert
 */
public class JKQuery extends JKService {
	HttpClient httpClient = new DefaultHttpClient();
	
	public JKQuery() {
		super(JKOOL_QUERY_URL, JKOOL_TOKEN);
	}

	public JKQuery(String token) {
		super(JKOOL_QUERY_URL, token);
	}

	public JKQuery(String endPoint, String token) {
		super(endPoint, token);
	}

	public Response call(String query) throws JKStreamException {
		return call(query, DEFAULT_MAX_ROWS);
	}

	public HttpResponse get(String query) throws JKStreamException {
		return get(query, DEFAULT_MAX_ROWS);
	}

	public Response call(String query, int maxRows) throws JKStreamException {
		return target.queryParam(JK_QUERY_KEY, query)
				.queryParam(JK_MAX_ROWS_KEY, maxRows)
				.queryParam(JK_TOKEN_KEY, getToken())
				.request(MediaType.APPLICATION_JSON)
				.header(JK_TOKEN_KEY, getToken())
				.get(Response.class);
	}

	public HttpResponse get(String query, int maxRows) throws JKStreamException {
		try {
			String urlQuery = JK_QUERY_KEY + "=" + URLEncoder.encode(query, "UTF-8")
					+ "&" + JK_MAX_ROWS_KEY + "=" + maxRows
					+ "&" + JK_TOKEN_KEY + "=" + getToken();
			HttpGet request = new HttpGet(getServiceUrl() + "?" + urlQuery);
			request.addHeader(JK_TOKEN_KEY, getToken());
			request.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
			HttpResponse response = httpClient.execute(request);
			return response;
		} catch (Throwable e) {
			throw new JKStreamException(300, "Failed: path=" + getServiceUrl() + ", query=" + query, e);
		}
	}
}
