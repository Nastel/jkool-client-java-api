package io.swagger.client.model;



import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "")
public class Activity {

	public String trackingId = null;
	public String sourceFqn = null;
	public String status = null;
	public String type = null;
	public String timeUsec = null;
	public String eventName = null;

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
		return sourceFqn;
	}

	public void setSourceFqn(String sourceFqn) {
		this.sourceFqn = sourceFqn;
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
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Event {\n");
		sb.append("  trackingId: ").append(trackingId).append("\n");
		sb.append("  sourceFqn: ").append(sourceFqn).append("\n");
		sb.append("  status: ").append(status).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  operation: ").append(eventName).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");

		sb.append("}\n");
		return sb.toString();
	}
}
