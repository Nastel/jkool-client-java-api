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

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "")
public class Activity {

	private String trackingId = null;
	private String status = "END";
	private long timeUsec;
	private long startTime;
	private long endTime;
	private long elapsedTimeUsec;
	private String activityName = null;
	private String appl = null;
	private String server = null;
	private String netAddr = null;
	private String dataCenter = null;
	private String geoAddr = null;
	private String exception = null;
	private EventTypes type = EventTypes.ACTIVITY;

	public Activity() {
		type = EventTypes.ACTIVITY;
		timeUsec = System.currentTimeMillis() * 1000;
		trackingId = UUID.randomUUID().toString();
	}

	public Activity(String tid) {
		type = EventTypes.ACTIVITY;
		trackingId = tid;
		timeUsec = System.currentTimeMillis() * 1000;
	}

	public Activity(String tid, long timeMs) {
		type = EventTypes.ACTIVITY;
		trackingId = tid;
		timeUsec = timeMs * 1000;
	}

	public Activity(String tid, Date time) {
		type = EventTypes.ACTIVITY;
		trackingId = tid;
		timeUsec = time.getTime() * 1000;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("tracking-id")
	public String getTrackingId() {
		return trackingId;
	}

	public Activity setTrackingId(String trackingId) {
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
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("time-usec")
	public long getTimeUsec() {
		return timeUsec;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public EventTypes getType() {
		return type;
	}

	public Activity setTimeUsec(Date timeUsec) {
		this.timeUsec = timeUsec.getTime() * 1000;
		return this;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("operation")
	public String getActivityName() {
		return activityName;
	}

	public Activity setActivityName(String activityName) {
		this.activityName = activityName;
		return this;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("start-time-usec")
	public long getStartTime() {
		if (startTime > 0)
			return startTime;
		else
			return getTimeUsec();
	}

	public Activity setStartTime(Date startTime) {
		this.startTime = startTime.getTime() * 1000;
		return this;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("end-time-usec")
	public long getEndTime() {
		if (endTime > 0)
			return endTime;
		else
			return getStartTime();
	}
	
	public Activity setEndTime(Date endTime) {
		this.endTime = endTime.getTime() * 1000;
		return this;
	}

	/**
	   **/
	@ApiModelProperty(value = "")
	@JsonProperty("elapsed-time-usec")
	public long getElapsedTimeUsec() {
		return elapsedTimeUsec;
	}

	public Activity setElapsedTimeUsec(long elapsedTimeUsec) {
		this.elapsedTimeUsec = elapsedTimeUsec;
		return this;
	}

	/**
	   **/
	@ApiModelProperty(value = "")
	@JsonProperty("exception")
	public String getException() {
		return exception;
	}

	public Activity setException(String exception) {
		this.exception = exception;
		this.status = "EXCEPTION";
		return this;
	}

	public String getAppl() {
		return appl;
	}

	public Activity setAppl(String appl) {
		this.appl = appl;
		return this;
	}

	public String getServer() {
		return server;
	}

	public Activity setServer(String server) {
		this.server = server;
		return this;
	}

	public String getNetAddr() {
		return netAddr;
	}

	public Activity setNetAddr(String netAddr) {
		this.netAddr = netAddr;
		return this;
	}

	public String getDataCenter() {
		return dataCenter;
	}

	public Activity setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
		return this;
	}

	public String getGeoAddr() {
		return geoAddr;
	}

	public Activity setGeoAddr(String geoAddr) {
		this.geoAddr = geoAddr;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Event {\n");
		sb.append("  trackingId: ").append(trackingId).append("\n");
		sb.append("  status: ").append(status).append("\n");
		;
		sb.append("  operation: ").append(activityName).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");

		sb.append("}\n");
		return sb.toString();
	}
}
