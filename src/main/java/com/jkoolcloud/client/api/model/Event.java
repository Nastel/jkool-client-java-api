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
package com.jkoolcloud.client.api.model;

import java.nio.charset.Charset;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class defines an event which has a timestamp and a message. Example: log event.
 * 
 * @author cathy
 */
@Schema(description = "")
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
		setType(EvType.EVENT);
	}

	/**
	 * Create an event
	 * 
	 * @param name
	 *            associated with the entity
	 */
	public Event(String name) {
		super(name);
		setType(EvType.EVENT);
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
		setType(EvType.EVENT);
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
		setType(EvType.EVENT);
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
		setType(EvType.EVENT);
	}

	@Schema(description = "")
	@JsonProperty("encoding")
	public String getEncoding() {
		return msgEncoding;
	}

	public Event setEncoding(String encoding) {
		this.msgEncoding = encoding;
		return this;
	}

	@Schema(description = "")
	@JsonProperty("charset")
	public String getCharset() {
		return msgCharset;
	}

	public Event setCharset(String charset) {
		this.msgCharset = charset;
		return this;
	}

	@Schema(description = "")
	@JsonProperty("msg-text")
	public String getMsgText() {
		return msgText;
	}

	public Event setMsgText(String msgText) {
		this.msgText = msgText;
		return this;
	}

	@Schema(description = "")
	@JsonProperty("msg-tag")
	public String getMsgTag() {
		return msgTag;
	}

	public Event setMsgTag(String msgTag) {
		this.msgTag = msgTag;
		return this;
	}

	@Schema(description = "")
	@JsonProperty("msg-size")
	public Integer getMsgSize() {
		if (msgText != null) {
			return msgText.length();
		} else {
			return 0;
		}
	}

	@Schema(description = "")
	@JsonProperty("mime-type")
	public String getMsgMimeType() {
		return msgMimeType;
	}

	public Event setMsgMimeType(String msgMimeType) {
		this.msgMimeType = msgMimeType;
		return this;
	}

	@Schema(description = "")
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
