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
package com.jkoolcloud.client.api.service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jkoolcloud.client.api.model.Snapshot;
import com.jkoolcloud.client.api.model.Trackable;

/**
 * This class implements RESTFull event streaming pipe to jKool
 * 
 * @author albert
 * @see JKService
 */
public class JKStream extends JKService {

	/**
	 * Create a jKool stream end-point
	 * with default end-point and access token
	 * 
	 */	
	public JKStream() {
		this(JKOOL_STREAM_URL, JKOOL_TOKEN);
	}

	/**
	 * Create a jKool stream end-point
	 * 
	 * @param token security access token
	 */	
	public JKStream(String token) {
		this(JKOOL_STREAM_URL, token);
	}

	/**
	 * Create a jKool stream end-point
	 * 
	 * @param endPoint service end-point URL
	 * @param token security access token
	 */	
	public JKStream(String endPoint, String token) {
		super(endPoint, token);
	}

	/**
	 * Send a trackable object to jKool end-point
	 * 
	 * @param event trackable event
	 * @throws JKStreamException
	 */	
	public Response post(Trackable event) throws JKStreamException {
		if (!event.isValid()) {
			throw new JKStreamException(200, "Invalid event=" + event);
		}
		return target.path(JK_EVENT_KEY).request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(event), MediaType.APPLICATION_JSON));
	}

	/**
	 * Send a snapshot object to jKool end-point
	 * 
	 * @param event trackable event
	 * @throws JKStreamException
	 */	
	public Response post(Snapshot snapshot) throws JKStreamException {
		if (!snapshot.isValid()) {
			throw new JKStreamException(200, "Invalid snapshot=" + snapshot);
		}
		return target.path(JK_SNAPSHOT_KEY).request().header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(snapshot), MediaType.APPLICATION_JSON));
	}
}
