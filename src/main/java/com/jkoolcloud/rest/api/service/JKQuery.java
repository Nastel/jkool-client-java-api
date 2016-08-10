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

public class JKQuery extends JKService {
	public static final String JKOOL_QUERY_URL = System.getProperty("jkool.query.url", "http://jkool.jkoolcloud.com/jKool/");
	public static final String QUERY_ENDPOINT = "jkql";
	public static final String QUERY_KEY = "query";

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

	public Response call(String query) throws JKApiException {
		return target.path(QUERY_ENDPOINT).queryParam(QUERY_KEY, query).queryParam(TOKEN_KEY, getToken())
				.request(MediaType.APPLICATION_JSON).header(TOKEN_KEY, getToken()).get(Response.class);
	}

	public HttpResponse get(String query) throws JKApiException {
		try {
			String urlQuery = URLEncoder.encode(QUERY_KEY + "=" + query + "&" + TOKEN_KEY + "=" + getToken(), "UTF-8");
			HttpGet request = new HttpGet(basePath + QUERY_ENDPOINT + "?" + urlQuery);
			request.addHeader(TOKEN_KEY, getToken());
			request.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
			HttpResponse response = httpClient.execute(request);
			return response;
		} catch (Throwable e) {
			throw new JKApiException(300, "Failed run query=" + query, e);
		}
	}
}
