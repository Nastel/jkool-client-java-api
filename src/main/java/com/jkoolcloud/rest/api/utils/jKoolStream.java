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
	public static final String JKOOL_REST_URL = System.getProperty("jkool.rest.url", "https://data.jkoolcloud.com/JESL");
	
	String basePath = JKOOL_REST_URL;
	Client client = ClientBuilder.newClient();
	WebTarget target = client.target(basePath);
	String token;
	
	public jKoolStream(String token) {
		this(JKOOL_REST_URL, token);
	}
	
	public jKoolStream(String endPoint, String token) {
		basePath = endPoint;
		client = ClientBuilder.newClient();
		target = client.target(basePath);		
		this.token = token;
	}
	
	public Response post(Event event) throws ApiException {
		return target.path("event").request().header("token", token)
		        .post(Entity.entity(serialize(event), "application/json"));
	}

	public Response post(Activity activity) throws ApiException {
		return target.path("activity").request().header("token", token)
		        .post(Entity.entity(serialize(activity), "application/json"));
	}

	public Response post(Snapshot snapshot) throws ApiException {
		return target.path("snapshot").request().header("token", token)
		        .post(Entity.entity(serialize(snapshot), "application/json"));
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
