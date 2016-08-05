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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.model.Activity;
import com.jkoolcloud.rest.api.model.Event;
import com.jkoolcloud.rest.api.model.Snapshot;

public class jKoolStream {
	public static final String TOKEN_KEY = "token";
	public static final String MEDIA_TYPE = "application/json";
	public static final String JKOOL_TOKEN = System.getProperty("jkool.api.token");
	public static final String JKOOL_REST_URL = System.getProperty("jkool.rest.url",
			"https://data.jkoolcloud.com/JESL");

	String basePath = JKOOL_REST_URL;
	String token = JKOOL_TOKEN;

	Client rsClient;
	WebTarget target;

	public jKoolStream() {
		this(JKOOL_REST_URL, JKOOL_TOKEN);
	}

	public jKoolStream(String token) {
		this(JKOOL_REST_URL, token);
	}

	public jKoolStream(String endPoint, String token) {
		this.basePath = endPoint;
		this.rsClient = ClientBuilder.newClient();
		this.target = rsClient.target(basePath);
		this.token = token;
	}

	public Response post(Event event) throws ApiException {
		return target.path("event").request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(event), MEDIA_TYPE));
	}

	public Response post(Activity activity) throws ApiException {
		return target.path("activity").request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(activity), MEDIA_TYPE));
	}

	public Response post(Snapshot snapshot) throws ApiException {
		return target.path("snapshot").request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(snapshot), MEDIA_TYPE));
	}

	/**
	 * Serialize the given Java object into JSON string.
	 */
	public static String serialize(Object obj) throws ApiException {
		try {
			if (obj != null)
				return JsonUtil.getJsonMapper().writeValueAsString(obj);
			else
				return null;
		} catch (Exception e) {
			throw new ApiException(500, e.getMessage());
		}
	}
}
