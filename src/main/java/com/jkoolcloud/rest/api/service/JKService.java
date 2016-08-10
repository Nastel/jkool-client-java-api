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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkoolcloud.rest.api.utils.JKUtils;

/**
 * This class defines a common way to communicate with jKool 
 * via RESTFull interface.
 * 
 * @author albert
 */
abstract public class JKService {
	public static final String TOKEN_KEY = "token";
	public static final String CONTENT_TYPE = "content-type";
	public static final String JKOOL_TOKEN = System.getProperty("jkool.api.token");

	String token;
	String basePath;

	Client rsClient;
	WebTarget target;
	ObjectMapper mapper;

	public JKService(String endPoint) {
		this(endPoint, JKOOL_TOKEN);
	}
	
	public JKService(String endPoint, String token) {
		this.token = token;
		this.basePath = endPoint;
		this.mapper = JKUtils.newObjectMapper();
		this.rsClient = ClientBuilder.newClient();
		this.target = rsClient.target(basePath);
	}
	
	public String getToken() {
		return token;
	}
	
	public String getServiceUrl() {
		return basePath;
	}
	
	
	/**
	 * Serialize an object into JSON format
	 * 
	 * @param obj java object instance (non null)
	 * @return JSON representation of the object
	 * @throws JKApiException
	 */
	public String serialize(Object obj) throws JKApiException {
		if (obj == null) {
			throw new JKApiException(500, "Object must not be null");			
		}
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new JKApiException(600, "Failed to serialize object: " + e.getMessage(), e);
		}
	}	
}
