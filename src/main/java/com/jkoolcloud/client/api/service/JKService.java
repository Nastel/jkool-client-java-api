/*
 * Copyright 2014-2023 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkoolcloud.client.api.service;

import java.io.Closeable;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkoolcloud.client.api.utils.JKUtils;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

/**
 * This class defines a common way to communicate with jKool via RESTFull interface.
 * 
 * @author albert
 */
abstract public class JKService implements JKQIConstants, Closeable, AutoCloseable {

	String token;
	String basePath;

	Client rsClient;
	WebTarget target;
	ObjectMapper mapper;

	/**
	 * Create a jKool service end-point
	 * 
	 * @param endPoint
	 *            URL end-point
	 */
	public JKService(String endPoint) {
		this(endPoint, JKOOL_TOKEN);
	}

	/**
	 * Create a jKool service end-point
	 * 
	 * @param endPoint
	 *            URL end-point
	 * @param token
	 *            security access token
	 */
	public JKService(String endPoint, String token) {
		this.token = token;
		this.basePath = endPoint;
		this.mapper = JKUtils.newObjectMapper();
		this.rsClient = ClientBuilder.newClient();
		this.target = rsClient.target(basePath);
	}

	/**
	 * Obtain access token associated with this service
	 * 
	 * @return access token associated with this service
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Assign a security access token
	 * 
	 * @param token
	 *            security access token
	 * @return itself
	 */
	public JKService setToken(String token) {
		this.token = token;
		return this;
	}

	/**
	 * Obtain service URL for executing sync queries
	 * 
	 * @return service URL for executing sync queries
	 */
	public String getServiceUrl() {
		return basePath;
	}

	/**
	 * Serialize an object into JSON format
	 * 
	 * @param obj
	 *            java object instance (non null)
	 * @return JSON representation of the object
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public String serialize(Object obj) throws JKStreamException {
		return JKUtils.serialize(mapper, obj);
	}

	/**
	 * Determine if current handle is connected
	 * 
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected() {
		return rsClient != null;
	}

	@Override
	public void close() throws IOException {
		if (rsClient != null) {
			try {
				rsClient.close();
			} catch (Throwable t) {
				throw new IOException("Failed to close JAX-RS client", t);
			}
		}
	}
}
