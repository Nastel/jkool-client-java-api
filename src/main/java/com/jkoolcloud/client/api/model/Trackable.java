/*
 * Copyright 2014-2025 JKOOL, LLC.
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

package com.jkoolcloud.client.api.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jkoolcloud.client.api.utils.JKUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class is a common time series entity for tracking application activities. The entity has time, id and a set of user defined
 * parameters {@link Snapshot}.
 * 
 * @author Cathy
 */
@Schema(description = "")
public abstract class Trackable implements Validated {
	public static final String DEFAULT_DC_NAME = System.getProperty("jkool.client.dc.name", "none");
	public static final String DEFAULT_APP_NAME = System.getProperty("jkool.client.appl.name", "java");
	public static final String DEFAULT_GEOADDR = System.getProperty("jkool.client.geo.addr", "0,0");

	Level severity = Level.INFO;
	EvType type = EvType.EVENT;
	CCode compCode = CCode.OK;

	long timeUsec;
	long startTimeUsec;
	long endTimeUsec;
	long elapsedTimeUsec;
	long waitTimeUsec;
	long pid = JKUtils.getVMPID();
	long tid = Thread.currentThread().getId();

	int reasonCode;

	String trackingId;
	String sourceUrl;
	String exception;
	String parentTrackId;

	String appl = DEFAULT_APP_NAME;
	String dataCenter = DEFAULT_DC_NAME;
	String geoAddr = DEFAULT_GEOADDR;
	String eventName = getClass().getName();

	String location = JKUtils.getHostAddress();
	String resource = JKUtils.getVMName();
	String server = JKUtils.getHostName();
	String netAddr = JKUtils.getHostAddress();
	String user = JKUtils.getVMUser();

	List<String> corrId;
	List<Property> properties = new ArrayList<>();
	List<Snapshot> snapshots = new ArrayList<>();

	/**
	 * Create a time series entity with default attributes
	 * 
	 */
	public Trackable() {
		timeUsec = System.currentTimeMillis() * 1000;
		trackingId = UUID.randomUUID().toString();
	}

	/**
	 * Create a time series entity
	 * 
	 * @param name
	 *            associated with the entity
	 */
	public Trackable(String name) {
		eventName = name;
		timeUsec = System.currentTimeMillis() * 1000;
		trackingId = UUID.randomUUID().toString();
	}

	/**
	 * Create a time series entity with
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 */
	public Trackable(String name, String tid) {
		eventName = name;
		trackingId = tid;
		timeUsec = System.currentTimeMillis() * 1000;
	}

	/**
	 * Create a time series entity with
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 * @param timeMs
	 *            timestamp in milliseconds
	 */
	public Trackable(String name, String tid, long timeMs) {
		eventName = name;
		trackingId = tid;
		timeUsec = timeMs * 1000;
	}

	/**
	 * Create a time series entity with
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 * @param time
	 *            timestamp associated with the entity
	 */
	public Trackable(String name, String tid, Date time) {
		eventName = name;
		trackingId = tid;
		timeUsec = time.getTime() * 1000;
	}

	/**
	 * Validate fields of this entity
	 *
	 * @return true if valid, false otherwise
	 */
	@Override
	public boolean isValid() {
		return eventName != null && appl != null && netAddr != null && server != null && dataCenter != null
				&& geoAddr != null && (getStartTimeUsec() <= getEndTimeUsec()) && (getElapsedTimeUsec() >= 0);
	}

	/**
	 * Add a list of snapshots
	 *
	 * @param snapshots
	 *            list of snapshots
	 * @return self
	 */
	public Trackable addSnapshot(List<Snapshot> snapshots) {
		this.snapshots.addAll(snapshots);
		return this;
	}

	/**
	 * Add a variable set of snapshots
	 *
	 * @param snapshots
	 *            list of snapshots
	 * @return self
	 */
	public Trackable addSnapshot(Snapshot... snapshots) {
		this.snapshots.addAll(Arrays.asList(snapshots));
		return this;
	}

	/**
	 * Add a list of properties
	 *
	 * @param props
	 *            list of properties
	 * @return self
	 */
	public Trackable addProperty(List<Property> props) {
		this.properties.addAll(props);
		return this;
	}

	/**
	 * Add a variable set of properties
	 *
	 * @param props
	 *            list of properties
	 * @return self
	 */
	public Trackable addProperty(Property... props) {
		addProperty(Arrays.asList(props));
		return this;
	}

	/**
	 * @return tracking id
	 **/
	@Schema(description = "")
	@JsonProperty("tracking-id")
	public String getTrackingId() {
		return trackingId;
	}

	public Trackable setTrackingId(String trackingId) {
		this.trackingId = trackingId;
		return this;
	}

	/**
	 * @return fully qualified source name
	 **/
	@Schema(description = "")
	@JsonProperty("source-fqn")
	public String getSourceFqn() {
		return "APPL=" + appl + "#SERVER=" + server + "#NETADDR=" + netAddr + "#DATACENTER=" + dataCenter + "#GEOADDR="
				+ geoAddr;
	}

	/**
	 * @return source URL
	 **/
	@Schema(description = "")
	@JsonProperty("source-url")
	public String getSourceUrl() {
		return sourceUrl;
	}

	public Trackable setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		return this;
	}

	/**
	 * @return severity
	 **/
	@Schema(description = "")
	@JsonProperty("severity")
	public Level getSeverity() {
		return severity;
	}

	public Trackable setSeverity(Level severity) {
		this.severity = severity;
		return this;
	}

	/**
	 * @return event type
	 **/
	@Schema(description = "")
	@JsonProperty("type")
	public EvType getType() {
		return type;
	}

	public Trackable setType(EvType type) {
		this.type = type;
		return this;
	}

	/**
	 * @return process id
	 **/
	@Schema(description = "")
	@JsonProperty("pid")
	public long getPid() {
		return pid;
	}

	public Trackable setPid(long pid) {
		this.pid = pid;
		return this;
	}

	/**
	 * @return thread id
	 **/
	@Schema(description = "")
	@JsonProperty("tid")
	public long getTid() {
		return tid;
	}

	public Trackable setTid(long tid) {
		this.tid = tid;
		return this;
	}

	/**
	 * @return completion code
	 **/
	@Schema(description = "")
	@JsonProperty("comp-code")
	public CCode getCompCode() {
		return compCode;
	}

	public Trackable setCompCode(CCode compCode) {
		this.compCode = compCode;
		return this;
	}

	/**
	 * @return reason code
	 **/
	@Schema(description = "")
	@JsonProperty("reason-code")
	public long getReasonCode() {
		return reasonCode;
	}

	public Trackable setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
		return this;
	}

	/**
	 * @return location
	 **/
	@Schema(description = "")
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	public Trackable setLocation(String location) {
		this.location = location;
		return this;
	}

	/**
	 * @return user name
	 **/
	@Schema(description = "")
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	public Trackable setUser(String user) {
		this.user = user;
		return this;
	}

	/**
	 * @return start time in micro-seconds since
	 **/
	@Schema(description = "")
	@JsonProperty("time-usec")
	public long getTimeUsec() {
		return timeUsec;
	}

	/**
	 * Set start time in milliseconds
	 *
	 * @param timeMs
	 *            time in milliseconds
	 * @return self
	 **/
	public Trackable setTime(long timeMs) {
		this.timeUsec = timeMs * 1000;
		return this;
	}

	/**
	 * Set start time based on a specific date
	 *
	 * @param dateTime
	 *            date and time
	 * @return self
	 **/
	public Trackable setTime(Date dateTime) {
		this.timeUsec = dateTime.getTime() * 1000;
		return this;
	}

	/**
	 * @return start time in microseconds
	 **/
	@Schema(description = "")
	@JsonProperty("start-time-usec")
	public long getStartTimeUsec() {
		if (startTimeUsec > 0) {
			return startTimeUsec;
		} else {
			return getTimeUsec();
		}
	}

	public Trackable setStartTime(long timeMs) {
		this.startTimeUsec = timeMs * 1000;
		return this;
	}

	public Trackable setStartTime(Date startTime) {
		this.startTimeUsec = startTime.getTime() * 1000;
		return this;
	}

	/**
	 * @return end time in microseconds
	 **/
	@Schema(description = "")
	@JsonProperty("end-time-usec")
	public long getEndTimeUsec() {
		if (endTimeUsec > 0) {
			return endTimeUsec;
		} else {
			return getStartTimeUsec() + getElapsedTimeUsec();
		}
	}

	/**
	 * @return elapsed time in microseconds
	 **/
	@Schema(description = "")
	@JsonProperty("elapsed-time-usec")
	public long getElapsedTimeUsec() {
		return elapsedTimeUsec;
	}

	public Trackable setElapsedTimeUsec(long elapsedTimeUsec) {
		this.elapsedTimeUsec = elapsedTimeUsec;
		return this;
	}

	/**
	 * @return wait time in microseconds
	 **/
	@Schema(description = "")
	@JsonProperty("wait-time-usec")
	public long getWaitTimeUsec() {
		return waitTimeUsec;
	}

	public Trackable setWaitTimeUsec(long waitTimeUsec) {
		this.waitTimeUsec = waitTimeUsec;
		return this;
	}

	/**
	 * @return list of snapshots, empty list if non
	 **/
	@Schema(description = "")
	@JsonProperty("snapshots")
	public List<Snapshot> getSnapshots() {
		return snapshots;
	}

	/**
	 * @return list of correlation ids, empty list if non
	 **/
	@Schema(description = "")
	@JsonProperty("corrid")
	public List<String> getCorrId() {
		return corrId;
	}

	public Trackable setCorrId(List<String> corrId) {
		this.corrId = corrId;
		return this;
	}

	/**
	 * @return resource name
	 **/
	@Schema(description = "")
	@JsonProperty("resource")
	public String getResource() {
		return resource;
	}

	public Trackable setResource(String resource) {
		this.resource = resource;
		return this;
	}

	/**
	 * @return exception string
	 **/
	@Schema(description = "")
	@JsonProperty("exception")
	public String getException() {
		return exception;
	}

	public Trackable setException(String exception) {
		this.exception = exception;
		return this;
	}

	/**
	 * @return parent tracking id
	 **/
	@Schema(description = "")
	@JsonProperty("parent-id")
	public String getParentTrackId() {
		return parentTrackId;
	}

	public Trackable setParentTrackId(String parentTrackId) {
		this.parentTrackId = parentTrackId;
		return this;
	}

	/**
	 * @return operation name
	 **/
	@Schema(description = "")
	@JsonProperty("operation")
	public String getName() {
		return eventName;
	}

	public Trackable setName(String eventName) {
		this.eventName = eventName;
		return this;
	}

	/**
	 * @return list of properties, empty list if non
	 **/
	@Schema(description = "")
	@JsonProperty("properties")
	public List<Property> getProperties() {
		return properties;
	}

	@JsonIgnore
	public String getAppl() {
		return appl;
	}

	public Trackable setAppl(String appl) {
		this.appl = appl;
		return this;
	}

	@JsonIgnore
	public String getServer() {
		return server;
	}

	public Trackable setServer(String server) {
		this.server = server;
		return this;
	}

	@JsonIgnore
	public String getNetAddr() {
		return netAddr;
	}

	public Trackable setNetAddr(String netAddr) {
		this.netAddr = netAddr;
		return this;
	}

	@JsonIgnore
	public String getDataCenter() {
		return dataCenter;
	}

	public Trackable setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
		return this;
	}

	@JsonIgnore
	public String getGeoAddr() {
		return geoAddr;
	}

	public Trackable setGeoAddr(String geoAddr) {
		this.geoAddr = geoAddr;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" {\n");
		sb.append("  trackingId: ").append(trackingId).append("\n");
		sb.append("  sourceUrl: ").append(sourceUrl).append("\n");
		sb.append("  severity: ").append(severity).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  pid: ").append(pid).append("\n");
		sb.append("  tid: ").append(tid).append("\n");
		sb.append("  compCode: ").append(compCode).append("\n");
		sb.append("  reasonCode: ").append(reasonCode).append("\n");
		sb.append("  location: ").append(location).append("\n");
		sb.append("  operation: ").append(eventName).append("\n");
		sb.append("  user: ").append(user).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");
		sb.append("  startTimeUsec: ").append(startTimeUsec).append("\n");
		sb.append("  endTimeUsec: ").append(endTimeUsec).append("\n");
		sb.append("  elapsedTimeUsec: ").append(elapsedTimeUsec).append("\n");
		sb.append("  snapshots: ").append(snapshots).append("\n");
		sb.append("}\n");

		return sb.toString();
	}
}
