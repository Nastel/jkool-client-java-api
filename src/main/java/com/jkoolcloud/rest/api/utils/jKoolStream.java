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

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.model.Activity;
import com.jkoolcloud.rest.api.model.Event;
import com.jkoolcloud.rest.api.model.Snapshot;

public class jKoolStream extends JKService {
	public static final String JKOOL_STREAM_URL = System.getProperty("jkool.stream.url", "https://data.jkoolcloud.com/JESL");

	public jKoolStream() {
		this(JKOOL_STREAM_URL, JKOOL_TOKEN);
	}

	public jKoolStream(String token) {
		this(JKOOL_STREAM_URL, token);
	}

	public jKoolStream(String endPoint, String token) {
		super(endPoint, token);
	}

	public Response post(Event event) throws JKApiException {
		return target.path("event").request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(event), MediaType.APPLICATION_JSON));
	}

	public Response post(Activity activity) throws JKApiException {
		return target.path("activity").request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(activity), MediaType.APPLICATION_JSON));
	}

	public Response post(Snapshot snapshot) throws JKApiException {
		return target.path("snapshot").request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(snapshot), MediaType.APPLICATION_JSON));
	}
}
