package io.swagger.client.model;

import java.util.*;
import io.swagger.client.model.Snapshot;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "")
public class EventActivity {

	private String trackingId = null;
	private String source = null;
	private String sourceFqn = null;
	private String sourceInfo = null;
	private String sourceUrl = null;
	private String status = null;
	private String severity = null;
	private Integer severityNo = null;
	private String type = null;
	private Integer typeNo = null;
	private Integer pid = null;
	private Integer tid = null;
	private String compCode = null;
	private Integer compCodeNo = null;
	private Integer reasonCode = null;
	private String location = null;
	private String operation = null;
	private String user = null;
	private String timeUsec = null;
	private String startTimeUsec = null;
	private String endTimeUsec = null;
	private Integer elapsedTimeUsec = null;
	private String idCount = null;
	private Integer snapCount = null;
	private String idSet = null;
	private String msgText = null;
	private Integer msgSize = 0;
	private String msgEncoding = null;
	private String msgCharset = null;
	private String corrId = null;
	private String resource = null;
	private String msgMimeType = null;
	private Integer msgAge = null;
	private String Exception = null;
	private String msgTag = null;
	private String parentTrackId = null;
	private String waitTimeUsec = null;
	private List<Snapshot> snapshots = new ArrayList<Snapshot>();

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
	@JsonProperty("source")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("source-fqn")
	public String getSourceFqn() {
		return sourceFqn;
	}

	public void setSourceFqn(String sourceFqn) {
		this.sourceFqn = sourceFqn;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("source-info")
	public String getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
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
	@JsonProperty("severity")
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("severity-no")
	public Integer getSeverityNo() {
		return severityNo;
	}

	public void setSeverityNo(Integer severityNo) {
		this.severityNo = severityNo;
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
	@JsonProperty("type-no")
	public Integer getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(Integer typeNo) {
		this.typeNo = typeNo;
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
	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("comp-code-no")
	public Integer getCompCodeNo() {
		return compCodeNo;
	}

	public void setCompCodeNo(Integer compCodeNo) {
		this.compCodeNo = compCodeNo;
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
	@JsonProperty("operation")
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
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
		return timeUsec;
	}

	public void setTimeUsec(String timeUsec) {
		this.timeUsec = timeUsec;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("start-time-usec")
	public String getStartTimeUsec() {
		return startTimeUsec;
	}

	public void setStartTimeUsec(String startTimeUsec) {
		this.startTimeUsec = startTimeUsec;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("end-time-usec")
	public String getEndTimeUsec() {
		return endTimeUsec;
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
	@JsonProperty("id-count")
	public String getIdCount() {
		return idCount;
	}

	public void setIdCount(String idCount) {
		this.idCount = idCount;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("snap-count")
	public Integer getSnapCount() {
		return snapCount;
	}

	public void setSnapCount(Integer snapCount) {
		this.snapCount = snapCount;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("id-set")
	public String getIdSet() {
		return idSet;
	}

	public void setIdSet(String idSet) {
		this.idSet = idSet;
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
	@JsonProperty("msg-text")
	public String getMsg() {
		return msgText;
	}

	public void setMsg(String msg) {
		this.msgText = msg;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("msg-size")
	public Integer getMsgCount() {
		return msgSize;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgSize = msgCount;
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
	public String getCorrId() {
		return corrId;
	}

	public void setCorrId(String corrId) {
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
	@JsonProperty("mime-type")
	public String getMimeType() {
		return msgMimeType;
	}

	public void setMimeType(String mimeType) {
		this.msgMimeType = mimeType;
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
	@JsonProperty("msg-encoding")
	public String getMsgEncoding() {
		return msgEncoding;
	}

	public void setMsgEncoding(String msgEncoding) {
		this.msgEncoding = msgEncoding;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("msg-charset")
	public String getMsgCharset() {
		return msgCharset;
	}

	public void setMsgCharset(String msgCharset) {
		this.msgCharset = msgCharset;
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
		return Exception;
	}

	public void setException(String exception) {
		Exception = exception;
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
	@JsonProperty("parent-track-id")
	public String getParentTrackId() {
		return parentTrackId;
	}

	public void setParentTrackId(String parentTrackId) {
		this.parentTrackId = parentTrackId;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("waitTimeUsec")
	public String getWaitTimeUsec() {
		return waitTimeUsec;
	}

	public void setWaitTimeUsec(String waitTimeUsec) {
		this.waitTimeUsec = waitTimeUsec;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Event {\n");

		sb.append("  trackingId: ").append(trackingId).append("\n");
		sb.append("  source: ").append(source).append("\n");
		sb.append("  sourceFqn: ").append(sourceFqn).append("\n");
		sb.append("  sourceInfo: ").append(sourceInfo).append("\n");
		sb.append("  sourceUrl: ").append(sourceUrl).append("\n");
		sb.append("  status: ").append(status).append("\n");
		sb.append("  severity: ").append(severity).append("\n");
		sb.append("  severityNo: ").append(severityNo).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  typeNo: ").append(typeNo).append("\n");
		sb.append("  pid: ").append(pid).append("\n");
		sb.append("  tid: ").append(tid).append("\n");
		sb.append("  compCode: ").append(compCode).append("\n");
		sb.append("  compCodeNo: ").append(compCodeNo).append("\n");
		sb.append("  reasonCode: ").append(reasonCode).append("\n");
		sb.append("  location: ").append(location).append("\n");
		sb.append("  operation: ").append(operation).append("\n");
		sb.append("  user: ").append(user).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");
		sb.append("  startTimeUsec: ").append(startTimeUsec).append("\n");
		sb.append("  endTimeUsec: ").append(endTimeUsec).append("\n");
		sb.append("  elapsedTimeUsec: ").append(elapsedTimeUsec).append("\n");
		sb.append("  idCount: ").append(idCount).append("\n");
		sb.append("  snapCount: ").append(snapCount).append("\n");
		sb.append("  idSet: ").append(idSet).append("\n");
		sb.append("  snapshots: ").append(snapshots).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
