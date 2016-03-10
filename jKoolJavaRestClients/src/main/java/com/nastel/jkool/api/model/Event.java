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
import java.text.SimpleDateFormat;
import java.util.*;

import io.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nastel.jkool.api.model.Snapshot;

@ApiModel(description = "")
public class Event {

	public String trackingId = null;
	public String sourceUrl = null;
	public Severities severity = null;
	public EventTypes type = null;
	public Integer pid = null;
	public Integer tid = null;
	public CompCodes compCode = null;
	public Integer reasonCode = null;
	public String location = null;
	public String user = null;
	public String timeUsec = null;
	public String startTimeUsec = null;
	public String endTimeUsec = null;
	public Integer elapsedTimeUsec = null;
	public String msgText = null;
	public Integer msgSize = 0;
	public String msgEncoding = null;
	public String msgCharset = null;
	public List<String> corrId = null;
	public String resource = null;
	public String msgMimeType = null;
	public Integer msgAge = null;
	public String exception = null;
	public String msgTag = null;
	public String parentTrackId = null;
	public Integer waitTimeUsec = null;
	public String eventName = null;
	private List<Property> properties = null;;
	private List<Snapshot> snapshots = new ArrayList<Snapshot>();
	private String appl = null;
	private String server = null;
	private String netAddr = null;
	private String dataCenter = null;
	private String geoAddr = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

	
	

	public Event() {

	}

	public Event(String trackingId, String sourceUrl,
			Severities severity, EventTypes type, Integer pid, Integer tid,
			CompCodes compCode, Integer reasonCode, String location, String user,
			String timeUsec, String startTimeUsec, String endTimeUsec,
			Integer elapsedTimeUsec, String msgText,
			Integer msgSize, String msgEncoding, String msgCharset,
			List<String> corrId, String resource, String msgMimeType, Integer msgAge,
			String exception, String msgTag, String parentTrackId,
			Integer waitTimeUsec, String eventName, List<Snapshot> snapshots, String appl, String server, String netAddr, String dataCenter, String geoAddr) {
		super();
		this.trackingId = trackingId;
		this.sourceUrl = sourceUrl;
		this.severity = severity;
		this.type = type;
		this.pid = pid;
		this.tid = tid;
		this.compCode = compCode;
		this.reasonCode = reasonCode;
		this.location = location;
		this.user = user;
		this.timeUsec = timeUsec;
		this.startTimeUsec = startTimeUsec;
		this.endTimeUsec = endTimeUsec;
		this.elapsedTimeUsec = elapsedTimeUsec;
		this.msgText = msgText;
		this.msgSize = msgSize;
		this.msgEncoding = msgEncoding;
		this.msgCharset = msgCharset;
		this.corrId = corrId;
		this.resource = resource;
		this.msgMimeType = msgMimeType;
		this.msgAge = msgAge;
		this.exception = exception;
		this.msgTag = msgTag;
		this.parentTrackId = parentTrackId;
		this.waitTimeUsec = waitTimeUsec;
		this.eventName = eventName;
		this.snapshots = snapshots;
		this.appl = appl;
		this.geoAddr = geoAddr;
		this.server = server;
		this.netAddr = netAddr;
		this.dataCenter = dataCenter;
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
	@JsonProperty("source-url")
	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("severity")
	public Severities getSeverity() {
		return severity;
	}

	public void setSeverity(Severities severity) {
		this.severity = severity;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public EventTypes getType() {
		return type;
	}

	public void setType(EventTypes type) {
		this.type = type;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("pid")
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("tid")
	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("comp-code")
	public CompCodes getCompCode() {
		return compCode;
	}

	public void setCompCode(CompCodes compCode) {
		this.compCode = compCode;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("reason-code")
	public Integer getReasonCode() {
		return reasonCode;
	}
	

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("time-usec")
	public String getTimeUsec() {
	try
	{
		return (formatter.parse(timeUsec)).getTime() + "000";
	}
	catch (Exception e)
	{
		return null;
	}}

	public void setTimeUsec(String timeUsec) {
		this.timeUsec = timeUsec;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("start-time-usec")
	public Long getStartTimeUsec() {
		try
		{
			return new Long((formatter.parse(startTimeUsec)).getTime() + "000");
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void setStartTimeUsec(String startTimeUsec) {
		this.startTimeUsec = startTimeUsec;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("end-time-usec")
	public Long getEndTimeUsec() {
		try
		{
			return new Long((formatter.parse(endTimeUsec)).getTime() + "000");
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void setEndTimeUsec(String endTimeUsec) {
		this.endTimeUsec = endTimeUsec;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("elapsed-time-usec")
	public Integer getElapsedTimeUsec() {
		return elapsedTimeUsec;
	}

	public void setElapsedTimeUsec(Integer elapsedTimeUsec) {
		this.elapsedTimeUsec = elapsedTimeUsec;
	}

	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("snapshots")
	public List<Snapshot> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<Snapshot> snapshots) {
		this.snapshots = snapshots;
	}


	@ApiModelProperty(value = "")
	@JsonProperty("encoding")
	public String getEncoding() {
		return msgEncoding;
	}

	public void setEncoding(String encoding) {
		this.msgEncoding = encoding;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("charset")
	public String getCharset() {
		return msgCharset;
	}

	public void setCharset(String charset) {
		this.msgCharset = charset;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("corrid")
	public List<String> getCorrId() {
		return corrId;
	}

	public void setCorrId(List<String> corrId) {
		this.corrId = corrId;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("resource")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("msg-text")
	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("msg-size")
	public Integer getMsgSize() {
		return msgSize;
	}

	public void setMsgSize(Integer msgSize) {
		this.msgSize = msgSize;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("mime-type")
	public String getMsgMimeType() {
		return msgMimeType;
	}

	public void setMsgMimeType(String msgMimeType) {
		this.msgMimeType = msgMimeType;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("msg-age")
	public Integer getMsgAge() {
		return msgAge;
	}

	public void setMsgAge(Integer msgAge) {
		this.msgAge = msgAge;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("exception")
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("msg-tag")
	public String getMsgTag() {
		return msgTag;
	}

	public void setMsgTag(String msgTag) {
		this.msgTag = msgTag;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("parent-id")
	public String getParentTrackId() {
		return parentTrackId;
	}

	public void setParentTrackId(String parentTrackId) {
		this.parentTrackId = parentTrackId;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("waitTimeUsec")
	public Integer getWaitTimeUsec() {
		return waitTimeUsec;
	}
	

	public void setWaitTimeUsec(Integer waitTimeUsec) {
		this.waitTimeUsec = waitTimeUsec;
	}
	
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("operation")
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	 /**
	   **/
	  @ApiModelProperty(value = "")
	  @JsonProperty("properties")
	  public List<Property> getProperties() {
	    return properties;
	  }
	  public void setProperties(List<Property> properties) {
	    this.properties = properties;
	  }
	  
	  


	public String getMsgEncoding() {
		return msgEncoding;
	}

	public void setMsgEncoding(String msgEncoding) {
		this.msgEncoding = msgEncoding;
	}

	public String getMsgCharset() {
		return msgCharset;
	}

	public void setMsgCharset(String msgCharset) {
		this.msgCharset = msgCharset;
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
