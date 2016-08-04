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
package com.nastel.jkool.api.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.nastel.jkool.api.model.Activity;
import com.nastel.jkool.api.model.Event;

public class jKoolSend {
	public static final String JKOOL_REST_URL = System.getProperty("jkool.rest.url", "https://data.jkoolcloud.com/JESL");
	
	String basePath = JKOOL_REST_URL;
	Client client = ClientBuilder.newClient();
	WebTarget target = client.target(basePath);
	
	public jKoolSend(String endPoint) {
		this(JKOOL_REST_URL);
	}
	
	public jKoolSend(String endPoint) {
		basePath = endPoint;
		client = ClientBuilder.newClient();
		target = client.target(basePath);		
	}
	
	public Response post(Event event, String token) {
		return target.path("event").request().header("token", token)
		        .post(Entity.entity(serialize(event), "application/json"));
	}

	public Response post(Activity activity, String token) {
		return target.path("activity").request().header("token", token)
		        .post(Entity.entity(serialize(activity), "application/json"));
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
