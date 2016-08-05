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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class jKoolRetrieve {
	public static final String TOKEN_KEY = "token";
	public static final String MEDIA_TYPE = "application/json";
	public static final String JKOOL_TOKEN = System.getProperty("jkool.api.token");
	//public static final String JKOOL_REST_URL = System.getProperty("jkool.rest.url", "https://jkool.jkoolcloud.com/jKool");
	public static final String JKOOL_REST_URL = System.getProperty("jkool.rest.url", "http://localhost:8080/jKool");
	
	String basePath = JKOOL_REST_URL;
	String token = JKOOL_TOKEN;

	Client rsClient;
	WebTarget target;
	ObjectMapper mapper;

	public jKoolRetrieve() {
		this(JKOOL_REST_URL, JKOOL_TOKEN);
	}

	public jKoolRetrieve(String token) {
		this(JKOOL_REST_URL, token);
	}

	public jKoolRetrieve(String endPoint, String token) {
		this.token = token;
		this.basePath = endPoint;
		this.mapper = jsonUtils.newObjectMapper();
		this.rsClient = ClientBuilder.newClient();
		this.target = rsClient.target(basePath);
	}

	public Response get(String query) throws JKApiException {
		return target.path("jkql").queryParam("query", query).request(MediaType.TEXT_PLAIN_TYPE).header("username", "test3").header("password", "pwtest3").header("repositoryId", "R_test385529$O_test3").get();
		//return target.path("jkql").queryParam("query", query).request(MediaType.APPLICATION_JSON).header("username", "test3").header("password", "pwtest3").header("repositoryId", "R_test385529$O_test3").get(String.class);

	}

	
}
