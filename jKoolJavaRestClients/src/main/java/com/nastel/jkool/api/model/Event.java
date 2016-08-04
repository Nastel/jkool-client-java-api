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
import java.util.*;

import io.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nastel.jkool.api.model.Snapshot;

@ApiModel(description = "")
public class Event {

	private String trackingId = null;
	private String sourceUrl = null;
	private Severities severity = null;
	private EventTypes type = null;
	private int pid;
	private int tid;
	private CompCodes compCode = null;
	private int reasonCode;
	private String location = null;
	private String user = null;
	private long timeUsec;
	private long startTimeUsec;
	private long endTimeUsec;
	private long elapsedTimeUsec;
	private String msgText = null;
	private String msgEncoding = null;
	private String msgCharset = null;
	private List<String> corrId = null;
	private String resource = null;
	private String msgMimeType = null;
	private Long msgAgeUsec = null;
	private String exception = null;
	private String msgTag = null;
	private String parentTrackId = null;
	private long waitTimeUsec;
	private String eventName = null;
	private List<Property> properties = null;;
	private List<Snapshot> snapshots = new ArrayList<Snapshot>();
	private String appl = null;
	private String server = null;
	private String netAddr = null;
	private String dataCenter = null;
	private String geoAddr = null;

	public Event() {
		timeUsec = System.currentTimeMillis()*1000;
		trackingId = UUID.randomUUID().toString();
		type = EventTypes.EVENT;
	}

	public Event(String tid) {
		trackingId = tid;
		timeUsec = System.currentTimeMillis()*1000;
		type = EventTypes.EVENT;
	}

	public Event(String tid, long timeMs) {
		trackingId = tid;
		timeUsec = timeMs*1000;
		type = EventTypes.EVENT;
	}

	public Event(String tid, Date time) {
		trackingId = tid;
		timeUsec = time.getTime()*1000;
		type = EventTypes.EVENT;
	}


	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("tracking-id")
	public String getTrackingId() {
		return trackingId;
	}

	public Event setTrackingId(String trackingId) {
		this.trackingId = trackingId;
		return this;
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
	@JsonProperty("source-url")
	public String getSourceUrl() {
		return sourceUrl;
	}

	public Event setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("severity")
	public Severities getSeverity() {
		if (severity != null)
			return severity;
		else
			return Severities.INFO;
	}

	public Event setSeverity(Severities severity) {
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

	public Event setType(EventTypes type) {
		this.type = type;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("pid")
	public Integer getPid() {
		return pid;
	}

	public Event setPid(Integer pid) {
		this.pid = pid;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("tid")
	public Integer getTid() {
		return tid;
	}

	public Event setTid(Integer tid) {
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

	public Event setCompCode(CompCodes compCode) {
		this.compCode = compCode;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("reason-code")
	public Integer getReasonCode() {
		return reasonCode;
	}
	

	public Event setReasonCode(Integer reasonCode) {
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

	public Event setLocation(String location) {
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

	public Event setUser(String user) {
		this.user = user;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("time-usec")
	public Long getTimeUsec() {
		return timeUsec;
	}

	public Event setTimeUsec(Date timeUsec) {
		this.timeUsec = timeUsec.getTime() * 1000;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("start-time-usec")
	public Long getStartTimeUsec() {
		if (startTimeUsec > 0)
			return startTimeUsec;
		else
			return getTimeUsec();
	}

	public Event setStartTimeUsec(Date startTimeUsec) {
		this.startTimeUsec = startTimeUsec.getTime()*1000;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("end-time-usec")
	public Long getEndTimeUsec() {
		if (endTimeUsec > 0)
			return endTimeUsec;
		else
			return getStartTimeUsec();
	}

	public Event setEndTimeUsec(Date endTimeUsec) {
		this.endTimeUsec = endTimeUsec.getTime()*1000;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("elapsed-time-usec")
	public Long getElapsedTimeUsec() {
		return elapsedTimeUsec;
	}

	public Event setElapsedTimeUsec(long elapsedTimeUsec) {
		this.elapsedTimeUsec = elapsedTimeUsec;
		return this;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("snapshots")
	public List<Snapshot> getSnapshots() {
		return snapshots;
	}

	public Event setSnapshots(List<Snapshot> snapshots) {
		this.snapshots = snapshots;
		return this;
	}


	@ApiModelProperty(value = "")
	@JsonProperty("encoding")
	public String getEncoding() {
		return msgEncoding;
	}

	public Event setEncoding(String encoding) {
		this.msgEncoding = encoding;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("charset")
	public String getCharset() {
		return msgCharset;
	}

	public Event setCharset(String charset) {
		this.msgCharset = charset;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("corrid")
	public List<String> getCorrId() {
		return corrId;
	}

	public Event setCorrId(List<String> corrId) {
		this.corrId = corrId;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("resource")
	public String getResource() {
		return resource;
	}

	public Event setResource(String resource) {
		this.resource = resource;
		return this;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("msg-text")
	public String getMsgText() {
		return msgText;
	}

	public Event setMsgText(String msgText) {
		this.msgText = msgText;
		return this;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("msg-size")
	public Integer getMsgSize() {
		if (msgText != null)
			return msgText.length();
		else
			return 0;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("mime-type")
	public String getMsgMimeType() {
		return msgMimeType;
	}

	public Event setMsgMimeType(String msgMimeType) {
		this.msgMimeType = msgMimeType;
		return this;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("msg-age")
	public Long getMsgAgeUsec() {
		return msgAgeUsec;
	}

	public Event setMsgAgeUsec(long ageUsec) {
		this.msgAgeUsec = ageUsec;
		return this;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("exception")
	public String getException() {
		return exception;
	}

	public Event setException(String exception) {
		this.exception = exception;
		return this;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("msg-tag")
	public String getMsgTag() {
		return msgTag;
	}

	public Event setMsgTag(String msgTag) {
		this.msgTag = msgTag;
		return this;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("parent-id")
	public String getParentTrackId() {
		return parentTrackId;
	}

	public Event setParentTrackId(String parentTrackId) {
		this.parentTrackId = parentTrackId;
		return this;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("waitTimeUsec")
	public Long getWaitTimeUsec() {
		return waitTimeUsec;
	}
	

	public Event setWaitTimeUsec(long waitTimeUsec) {
		this.waitTimeUsec = waitTimeUsec;
		return this;
	}
	
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("operation")
	public String getEventName() {
		return eventName;
	}

	public Event setEventName(String eventName) {
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
	  public Event setProperties(List<Property> properties) {
	    this.properties = properties;
		return this;
	  }
	  
	  

	@JsonIgnore
	public String getMsgEncoding() {
		return msgEncoding;
	}

	public Event setMsgEncoding(String msgEncoding) {
		this.msgEncoding = msgEncoding;
		return this;
	}

	@JsonIgnore
	public String getMsgCharset() {
		return msgCharset;
	}

	public Event setMsgCharset(String msgCharset) {
		this.msgCharset = msgCharset;
		return this;
	}

	@JsonIgnore
	public String getAppl() {
		return appl;
	}

	public Event setAppl(String appl) {
		this.appl = appl;
		return this;
	}

	@JsonIgnore
	public String getServer() {
		return server;
	}

	public Event setServer(String server) {
		this.server = server;
		return this;
	}

	@JsonIgnore
	public String getNetAddr() {
		return netAddr;
	}

	public Event setNetAddr(String netAddr) {
		this.netAddr = netAddr;
		return this;
	}

	@JsonIgnore
	public String getDataCenter() {
		return dataCenter;
	}

	public Event setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
		return this;
	}

	@JsonIgnore
	public String getGeoAddr() {
		return geoAddr;
	}

	public Event setGeoAddr(String geoAddr) {
		this.geoAddr = geoAddr;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Event {\n");

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
