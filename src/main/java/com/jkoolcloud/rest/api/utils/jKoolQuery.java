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
package com.jkoolcloud.rest.api.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class jKoolQuery extends JKService {
	public static final String JKOOL_QUERY_URL = System.getProperty("jkool.query.url", "http://jkool.jkoolcloud.com:8080/jKool/");
	public static final String QUERY_KEY = "query";
	
	public jKoolQuery() {
		super(JKOOL_QUERY_URL, JKOOL_TOKEN);
	}

	public jKoolQuery(String token) {
		super(JKOOL_QUERY_URL, token);
	}

	public jKoolQuery(String endPoint, String token) {
		super(endPoint, token);
	}

	public HttpResponse get(String query) throws UnsupportedEncodingException, IOException, ClientProtocolException {
    	HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(basePath + "jkql?query=" + query );
        request.addHeader("token",token);
        request.addHeader("content-type", "application/json");
        HttpResponse response =  httpClient.execute(request);
        return response;
	}}

