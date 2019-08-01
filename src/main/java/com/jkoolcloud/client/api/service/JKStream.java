/*
 * Copyright 2014-2019 JKOOL, LLC.
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

import com.jkoolcloud.client.api.model.Activity;
import com.jkoolcloud.client.api.model.Dataset;
import com.jkoolcloud.client.api.model.Event;
import com.jkoolcloud.client.api.model.LogMsg;
import com.jkoolcloud.client.api.model.Level;
import com.jkoolcloud.client.api.model.Snapshot;
import com.jkoolcloud.client.api.utils.JKUtils;

/**
 * This class implements RESTFull event streaming pipe to jKool
 * 
 * @author albert
 * @see JKService
 */
public class JKStream extends JKService {
	public static final String CLIENT_HOSTNAME = "J-Client-Host-Name";
	public static final String CLIENT_HOSTADDR = "J-Client-Host-Addr";
	public static final String CLIENT_RUNTIME = "J-Client-Runtime";
	public static final String CLIENT_VERSION = "J-Client-Version";

	private static final String VALUE_VERSION = JKStream.class.getPackage().getImplementationVersion();
	private static final String VALUE_HOSTNAME = JKUtils.VM_HOST;
	private static final String VALUE_HOSTADDR = JKUtils.VM_NETADDR;
	private static final String VALUE_VMNAME = JKUtils.VM_NAME;

	/**
	 * Create a jKool stream end-point with default end-point and access token
	 * 
	 */
	public JKStream() {
		this(JKOOL_STREAM_URL, JKOOL_TOKEN);
	}

	/**
	 * Create a jKool stream end-point
	 * 
	 * @param token
	 *            security access token
	 */
	public JKStream(String token) {
		this(JKOOL_STREAM_URL, token);
	}

	/**
	 * Create a jKool stream end-point
	 * 
	 * @param endPoint
	 *            service end-point URL
	 * @param token
	 *            security access token
	 */
	public JKStream(String endPoint, String token) {
		super(endPoint, token);
	}

	/**
	 * Send a {@link Activity} object to jKool end-point
	 * 
	 * @param activity
	 *            trackable activity
	 * @throws JKStreamException
	 *             on error during send
	 * @return response message
	 */
	public Response post(Activity activity) throws JKStreamException {
		if (!activity.isValid()) {
			throw new JKStreamException(200, "Invalid activity=" + activity);
		}
		return target.path(JK_ACTIVITY_KEY).request()
				.header(CLIENT_HOSTNAME, VALUE_HOSTNAME)
				.header(CLIENT_HOSTADDR, VALUE_HOSTADDR)
				.header(CLIENT_RUNTIME, VALUE_VMNAME)
				.header(CLIENT_VERSION, VALUE_VERSION)
				.header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(activity), MediaType.APPLICATION_JSON));
	}

	/**
	 * Send a {@link Event} object to jKool end-point
	 * 
	 * @param event
	 *            trackable event
	 * @throws JKStreamException
	 *             on error during send
	 * @return response message
	 */
	public Response post(Event event) throws JKStreamException {
		if (!event.isValid()) {
			throw new JKStreamException(200, "Invalid event=" + event);
		}
		return target.path(JK_EVENT_KEY).request()
				.header(CLIENT_HOSTNAME, VALUE_HOSTNAME)
				.header(CLIENT_HOSTADDR, VALUE_HOSTADDR)
				.header(CLIENT_RUNTIME, VALUE_VMNAME)
				.header(CLIENT_VERSION, VALUE_VERSION)
				.header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(event), MediaType.APPLICATION_JSON));
	}

	/**
	 * Send a {@link LogMsg} object to jKool end-point
	 * 
	 * @param event
	 *            trackable log message
	 * @throws JKStreamException
	 *             on error during send
	 * @return response message
	 */
	public Response post(LogMsg event) throws JKStreamException {
		if (!event.isValid()) {
			throw new JKStreamException(200, "Invalid logmsg=" + event);
		}
		return target.path(JK_LOG_KEY).request()
				.header(CLIENT_HOSTNAME, VALUE_HOSTNAME)
				.header(CLIENT_HOSTADDR, VALUE_HOSTADDR)
				.header(CLIENT_RUNTIME, VALUE_VMNAME)
				.header(CLIENT_VERSION, VALUE_VERSION)
				.header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(event), MediaType.APPLICATION_JSON));
	}

	/**
	 * Send a {@link Snapshot}  object to jKool end-point
	 * 
	 * @param snapshot
	 *            trackable snapshot
	 * @throws JKStreamException
	 *             on error during send
	 * @return response message
	 */
	public Response post(Snapshot snapshot) throws JKStreamException {
		if (!snapshot.isValid()) {
			throw new JKStreamException(200, "Invalid snapshot=" + snapshot);
		}
		return target.path(JK_SNAPSHOT_KEY).request()
				.header(CLIENT_HOSTNAME, VALUE_HOSTNAME)
				.header(CLIENT_HOSTADDR, VALUE_HOSTADDR)
				.header(CLIENT_RUNTIME, VALUE_VMNAME)
				.header(CLIENT_VERSION, VALUE_VERSION)
				.header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(snapshot), MediaType.APPLICATION_JSON));
	}

	/**
	 * Send a {@link Dataset}  object to jKool end-point
	 * 
	 * @param dataset
	 *            trackable dataset
	 * @throws JKStreamException
	 *             on error during send
	 * @return response message
	 */
	public Response post(Dataset dataset) throws JKStreamException {
		if (!dataset.isValid()) {
			throw new JKStreamException(200, "Invalid dataset=" + dataset);
		}
		return target.path(JK_DATASET_KEY).request()
				.header(CLIENT_HOSTNAME, VALUE_HOSTNAME)
				.header(CLIENT_HOSTADDR, VALUE_HOSTADDR)
				.header(CLIENT_RUNTIME, VALUE_VMNAME)
				.header(CLIENT_VERSION, VALUE_VERSION)
				.header(TOKEN_KEY, token)
				.post(Entity.entity(serialize(dataset), MediaType.APPLICATION_JSON));
	}
	
	/**
	 * Send an info log message to the stream
	 * 
	 * @param sev
	 *            severity
	 * @param msg
	 *            log message
	 * @throws JKStreamException
	 *             on error during send
	 */
	public void log(Level sev, String msg) throws JKStreamException {
		this.post(newLogMsg(sev, msg));
	}
	
	/**
	 * Send an info log message to the stream
	 * 
	 * @param msg
	 *            log message
	 * @throws JKStreamException
	 *             on error during send
	 */
	public void info(String msg) throws JKStreamException {
		this.post(newLogMsg(Level.INFO, msg));
	}
	
	/**
	 * Send a warning log message to the stream
	 * 
	 * @param msg
	 *            log message
	 * @throws JKStreamException
	 *             on error during send
	 */
	public void warn(String msg) throws JKStreamException {
		this.post(newLogMsg(Level.WARNING, msg));
	}
	
	/**
	 * Send an error log message to the stream
	 * 
	 * @param msg
	 *            log message
	 * @throws JKStreamException
	 *             on error during send
	 */
	public void error(String msg) throws JKStreamException {
		this.post(newLogMsg(Level.ERROR, msg));
	}
	
	/**
	 * Send a debug log message to the stream
	 * 
	 * @param msg
	 *            log message
	 * @throws JKStreamException
	 *             on error during send
	 */
	public void debug(String msg) throws JKStreamException {
		this.post(newLogMsg(Level.DEBUG, msg));
	}
	
	/**
	 * Create a new {@link LogMsg}
	 * 
	 * @return {@link LogMsg}
	 */
	public static LogMsg newLogMsg() {
		return new LogMsg();
	}

	/**
	 * Create a new {@link LogMsg}
	 * 
	 * @param msg
	 *            log message
	 * @return {@link LogMsg}
	 */
	public static LogMsg newLogMsg(String msg) {
		return new LogMsg(msg);
	}

	/**
	 * Create a new {@link LogMsg}
	 * 
	 * @param sev
	 *            severity
	 * @param msg
	 *            log message
	 * @return {@link LogMsg}
	 */
	public static LogMsg newLogMsg(Level sev, String msg) {
		return new LogMsg(sev, msg);
	}

	/**
	 * Create a new {@link Event}
	 * 
	 * @return {@link Event}
	 */
	public static Event newEvent() {
		return new Event();
	}

	/**
	 * Create a new {@link Event}
	 * 
	 * @param name
	 *            event name
	 * @return {@link Event}
	 */
	public static Event newEvent(String name) {
		return new Event(name);
	}

	/**
	 * Create a new {@link Snapshot}
	 * 
	 * @param cat
	 *            category name
	 * @param name
	 *            snapshot name
	 * @return {@link Snapshot}
	 */
	public static Snapshot newSnapshot(String cat, String name) {
		return new Snapshot(cat, name);
	}

	/**
	 * Create a new {@link Dataset}
	 * 
	 * @param cat
	 *            category name
	 * @param name
	 *            dataset name
	 * @return {@link Dataset}
	 */
	public static Dataset newDataset(String cat, String name) {
		return new Dataset(cat, name);
	}
}
