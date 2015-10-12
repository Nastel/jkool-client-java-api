package com.nastel.jkool.api.model;

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
 
import io.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "")
public class Activity {

	private String trackingId = null;
	private String status = null;
	private String type = null;
	private String timeUsec = null;
	private String activityName = null;
	private String appl = null;
	private String server = null;
	private String netAddr = null;
	private String dataCenter = null;
	private String geoAddr = null;
	
	

	public Activity() {
	}

	public Activity(String trackingId, String status,
			String type, String timeUsec, String activityName, String appl, String server, String netAddr, String dataCenter, String geoAddr) {
		super();
		this.trackingId = trackingId;
		this.status = status;
		this.type = type;
		this.timeUsec = timeUsec;
		this.activityName = activityName;
		this.appl = appl;
		this.server = server;
		this.netAddr = netAddr;
		this.dataCenter = dataCenter;
		this.geoAddr = geoAddr;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("tracking-id")
	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("source-fqn")
	public String getSourceFqn() {
		return "APPL=" + appl + "#SERVER=" + server + "#NETADDR=" + netAddr + "#DATACENTER=" + dataCenter + "#GEOADDR=" + geoAddr;
	}
	


	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("time-usec")
	public String getTimeUsec() {
		return timeUsec;
	}

	public void setTimeUsec(String timeUsec) {
		this.timeUsec = timeUsec;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("operation")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	
	
	public String getAppl() {
		return appl;
	}

	public void setAppl(String appl) {
		this.appl = appl;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getNetAddr() {
		return netAddr;
	}

	public void setNetAddr(String netAddr) {
		this.netAddr = netAddr;
	}

	public String getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}

	public String getGeoAddr() {
		return geoAddr;
	}

	public void setGeoAddr(String geoAddr) {
		this.geoAddr = geoAddr;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Event {\n");
		sb.append("  trackingId: ").append(trackingId).append("\n");
		sb.append("  status: ").append(status).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  operation: ").append(activityName).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");

		sb.append("}\n");
		return sb.toString();
	}
}
