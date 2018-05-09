/*
 * Copyright 2014-2018 JKOOL, LLC.
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
package com.jkoolcloud.client.api.model;

import java.nio.charset.Charset;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class defines an event which has a timestamp and a message. Example: log event.
 * 
 * @author cathy
 */
@ApiModel(description = "")
public class Event extends Trackable {

	String msgText;
	String msgTag;
	String msgCharset = Charset.defaultCharset().displayName();
	String msgEncoding = "none";
	String msgMimeType = "text/plain";
	long msgAgeUsec;

	/**
	 * Create an event
	 * 
	 */
	public Event() {
		super();
		setType(EventTypes.EVENT);
	}

	/**
	 * Create an event
	 * 
	 * @param name
	 *            associated with the entity
	 */
	public Event(String name) {
		super(name);
		setType(EventTypes.EVENT);
	}

	/**
	 * Create an event
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 */
	public Event(String name, String tid) {
		super(name, tid);
		setType(EventTypes.EVENT);
	}

	/**
	 * Create an event
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 * @param timeMs
	 *            timestamp associated with the entity
	 */
	public Event(String name, String tid, long timeMs) {
		super(name, tid, timeMs);
		setType(EventTypes.EVENT);
	}

	/**
	 * Create an event
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 * @param time
	 *            timestamp associated with the entity
	 */
	public Event(String name, String tid, Date time) {
		super(name, tid, time);
		setType(EventTypes.EVENT);
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
	@JsonProperty("msg-text")
	public String getMsgText() {
		return msgText;
	}

	public Event setMsgText(String msgText) {
		this.msgText = msgText;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("msg-tag")
	public String getMsgTag() {
		return msgTag;
	}

	public Trackable setMsgTag(String msgTag) {
		this.msgTag = msgTag;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("msg-size")
	public Integer getMsgSize() {
		if (msgText != null) {
			return msgText.length();
		} else {
			return 0;
		}
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
	public long getMsgAgeUsec() {
		return msgAgeUsec;
	}

	public Event setMsgAgeUsec(long ageUsec) {
		this.msgAgeUsec = ageUsec;
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
}
