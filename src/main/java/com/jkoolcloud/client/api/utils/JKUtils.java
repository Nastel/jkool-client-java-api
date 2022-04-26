/*
 * Copyright 2014-2021 JKOOL, LLC.
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
package com.jkoolcloud.client.api.utils;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jkoolcloud.client.api.service.JKQIConstants;
import com.jkoolcloud.client.api.service.JKStreamException;

import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

/**
 * This class implements common API utilities
 * 
 * @author albert
 */
public class JKUtils {

	/**
	 * JVM process ID
	 */
	public static final long VM_PID = initVMID();

	/**
	 * JVM user name
	 */
	public static final String VM_USER = System.getProperty("user.name");

	/**
	 * JVM host name
	 */
	public static String VM_HOST;

	/**
	 * JVM network address
	 */
	public static String VM_NETADDR;

	/**
	 * JVM runtime name
	 */
	public static final String VM_NAME = ManagementFactory.getRuntimeMXBean().getName();

	static {
		try {
			VM_HOST = InetAddress.getLocalHost().getHostName();
			VM_NETADDR = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			VM_HOST = "uknown";
			VM_NETADDR = "0.0.0.0";
		}
	}

	private static long initVMID() {
		String _vm_pid_del = System.getProperty("jk.vm.pid.dlm", "@");
		String vm_name = ManagementFactory.getRuntimeMXBean().getName();
		try {
			int index = vm_name.indexOf(_vm_pid_del);
			if (index > 0) {
				return Long.parseLong(vm_name.substring(0, index));
			}
		} catch (Throwable e) {
		}
		return 0;
	}

	/**
	 * Return local host name
	 *
	 * @return Return local host name
	 */
	public static String getHostName() {
		return VM_HOST;
	}

	/**
	 * Return local host address
	 *
	 * @return Return local host address
	 */
	public static String getHostAddress() {
		return VM_NETADDR;
	}

	/**
	 * Return process ID associated with the current VM.
	 *
	 * @return process id associated with the current VM
	 */
	public static long getVMPID() {
		return VM_PID;
	}

	/**
	 * Return user name associated with the current VM.
	 *
	 * @return user name associated with the current VM.
	 */
	public static String getVMUser() {
		return VM_USER;
	}

	/**
	 * Return a name associated with the current VM.
	 *
	 * @return name associated with the current VM
	 */
	public static String getVMName() {
		return VM_NAME;
	}

	/**
	 * Determine if a query represents a subscription query
	 * 
	 * @param query
	 *            JKQL query
	 * @return true if subscribe query, false otherwise
	 */
	public static boolean isSubscribeQ(String query) {
		return query.toLowerCase().startsWith(JKQIConstants.JK_SUB_QUERY_PREFIX)
				|| query.toLowerCase().startsWith(JKQIConstants.JK_SUB_QUERY_PREFIX2);
	}

	/**
	 * Determine if a handle id represents a subscription query
	 * 
	 * @param id
	 *            JKQL query id
	 * @return true if subscribe query, false otherwise
	 */
	public static boolean isSubscribeId(String id) {
		return id.startsWith(JKQIConstants.JK_SUB_UUID_PREFIX);
	}

	/**
	 * Serialize an object into JSON format
	 * 
	 * @param mapper
	 *            JSON object mapper instance
	 * @param obj
	 *            java object instance (non null)
	 * @return JSON representation of the object
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public static String serialize(ObjectMapper mapper, Object obj) throws JKStreamException {
		if (obj == null) {
			throw new JKStreamException(500, "Object must not be null");
		}
		try {
			return mapper.writeValueAsString(obj);
		} catch (Throwable e) {
			throw new JKStreamException(600, "Failed to serialize object", e);
		}
	}

	/**
	 * Create new Object mapper instance for JSON serialization
	 * 
	 * @return JSON object mapper
	 */
	public static ObjectMapper newObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.registerModule(new JodaModule());
		return mapper;
	}

	public static String prettyPrint(JsonStructure json) {
		return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
	}

	public static String jsonFormat(JsonStructure json, String... options) {
		StringWriter stringWriter = new StringWriter();
		Map<String, Boolean> config = buildConfig(options);
		JsonWriterFactory writerFactory = Json.createWriterFactory(config);
		JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

		jsonWriter.write(json);
		jsonWriter.close();
		return stringWriter.toString();
	}

	public static JsonValue getJsonValue(String json_path, JsonObject response) {
		StringTokenizer tk = new StringTokenizer(json_path, "/");

		JsonValue rValue = response;
		JsonObject value = response;
		while (tk.hasMoreTokens()) {
			String key = tk.nextToken();
			rValue = value.get(key);
			if (rValue == null || (rValue.getValueType() != JsonValue.ValueType.OBJECT)) {
				break;
			} else {
				value = (JsonObject) rValue;
			}
		}
		return rValue;
	}

	private static Map<String, Boolean> buildConfig(String... options) {
		Map<String, Boolean> config = new HashMap<>();

		if (options != null) {
			for (String option : options) {
				config.put(option, true);
			}
		}
		return config;
	}
}
