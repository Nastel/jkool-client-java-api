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

package com.jkoolcloud.rest.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jkoolcloud.rest.api.utils.JKUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "")
public abstract class Trackable {
	public static final String DEFAULT_DC_NAME = System.getProperty("jkool.client.dc.name", "none");
	public static final String DEFAULT_APP_NAME = System.getProperty("jkool.client.appl.name", "java");
	public static final String DEFAULT_GEOADDR = System.getProperty("jkool.client.geo.addr", "0,0");

	Severities severity = Severities.INFO;
	EventTypes type = EventTypes.EVENT;
	CompCodes compCode = CompCodes.SUCCESS;

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
	List<Property> properties;
	List<Snapshot> snapshots = new ArrayList<Snapshot>();

	public Trackable() {
		timeUsec = System.currentTimeMillis() * 1000;
		trackingId = UUID.randomUUID().toString();
	}

	public Trackable(String name) {
		eventName = name;
		timeUsec = System.currentTimeMillis() * 1000;
		trackingId = UUID.randomUUID().toString();
	}

	public Trackable(String name, String tid) {
		eventName = name;
		trackingId = tid;
		timeUsec = System.currentTimeMillis() * 1000;
	}

	public Trackable(String name, String tid, long timeMs) {
		eventName = name;
		trackingId = tid;
		timeUsec = timeMs * 1000;
	}

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
	public boolean isValid() {
		return eventName != null && appl != null && netAddr != null && server != null && dataCenter != null
		        && geoAddr != null && (getStartTimeUsec() <= getEndTimeUsec()) && (getElapsedTimeUsec() >= 0);
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("tracking-id")
	public String getTrackingId() {
		return trackingId;
	}

	public Trackable setTrackingId(String trackingId) {
		this.trackingId = trackingId;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("source-fqn")
	public String getSourceFqn() {
		return "APPL=" + appl + "#SERVER=" + server + "#NETADDR=" + netAddr + "#DATACENTER=" + dataCenter + "#GEOADDR="
		        + geoAddr;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("source-url")
	public String getSourceUrl() {
		return sourceUrl;
	}

	public Trackable setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("severity")
	public Severities getSeverity() {
		return severity;
	}

	public Trackable setSeverity(Severities severity) {
		this.severity = severity;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public EventTypes getType() {
		return type;
	}

	public Trackable setType(EventTypes type) {
		this.type = type;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("pid")
	public long getPid() {
		return pid;
	}

	public Trackable setPid(long pid) {
		this.pid = pid;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("tid")
	public long getTid() {
		return tid;
	}

	public Trackable setTid(long tid) {
		this.tid = tid;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("comp-code")
	public CompCodes getCompCode() {
		return compCode;
	}

	public Trackable setCompCode(CompCodes compCode) {
		this.compCode = compCode;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("reason-code")
	public long getReasonCode() {
		return reasonCode;
	}

	public Trackable setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	public Trackable setLocation(String location) {
		this.location = location;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	public Trackable setUser(String user) {
		this.user = user;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("time-usec")
	public long getTimeUsec() {
		return timeUsec;
	}

	public Trackable setTime(long timeMs) {
		this.timeUsec = timeMs * 1000;
		return this;
	}

	public Trackable setTime(Date timeUsec) {
		this.timeUsec = timeUsec.getTime() * 1000;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("start-time-usec")
	public long getStartTimeUsec() {
		if (startTimeUsec > 0)
			return startTimeUsec;
		else
			return getTimeUsec();
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
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("end-time-usec")
	public long getEndTimeUsec() {
		if (endTimeUsec > 0) {
			return endTimeUsec;
		} else {
			return getStartTimeUsec() + getElapsedTimeUsec();
		}
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("elapsed-time-usec")
	public long getElapsedTimeUsec() {
		return elapsedTimeUsec;
	}

	public Trackable setElapsedTimeUsec(long elapsedTimeUsec) {
		this.elapsedTimeUsec = elapsedTimeUsec;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("wait-time-usec")
	public long getWaitTimeUsec() {
		return waitTimeUsec;
	}

	public Trackable setWaitTimeUsec(long waitTimeUsec) {
		this.waitTimeUsec = waitTimeUsec;
		return this;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("snapshots")
	public List<Snapshot> getSnapshots() {
		return snapshots;
	}

	public Trackable setSnapshots(List<Snapshot> snapshots) {
		this.snapshots = snapshots;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("corrid")
	public List<String> getCorrId() {
		return corrId;
	}

	public Trackable setCorrId(List<String> corrId) {
		this.corrId = corrId;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("resource")
	public String getResource() {
		return resource;
	}

	public Trackable setResource(String resource) {
		this.resource = resource;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("exception")
	public String getException() {
		return exception;
	}

	public Trackable setException(String exception) {
		this.exception = exception;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("parent-id")
	public String getParentTrackId() {
		return parentTrackId;
	}

	public Trackable setParentTrackId(String parentTrackId) {
		this.parentTrackId = parentTrackId;
		return this;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("operation")
	public String getName() {
		return eventName;
	}

	public Trackable setName(String eventName) {
		this.eventName = eventName;
		return this;
	}

	/**
	  **/
	@ApiModelProperty(value = "")
	@JsonProperty("properties")
	public List<Property> getProperties() {
		return properties;
	}

	public Trackable setProperties(List<Property> properties) {
		this.properties = properties;
		return this;
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
		sb.append("class " + this.getClass().getName() + " {\n");

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
