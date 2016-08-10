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

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class defines an activity -- a group of related events, such 
 * as transaction.
 * 
 * @author cathy
 */
@ApiModel(description = "")
public class Activity extends Trackable {

	public Activity() {
		super();
		setType(EventTypes.ACTIVITY);
	}

	public Activity(String name) {
		super(name);
		setType(EventTypes.ACTIVITY);
	}

	public Activity(String name, String tid) {
		super(name, tid);
		setType(EventTypes.ACTIVITY);
	}

	public Activity(String name, String tid, long timeMs) {
		super(name, tid, timeMs);
		setType(EventTypes.ACTIVITY);
	}

	public Activity(String name, String tid, Date time) {
		super(name, tid, time);
		setType(EventTypes.ACTIVITY);
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("status")
	public String getStatus() {
		return exception == null? "END": "EXCEPTION";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Activity {\n");
		sb.append("  trackingId: ").append(trackingId).append("\n");
		sb.append("  status: ").append(getStatus()).append("\n");
		sb.append("  user: ").append(getUser()).append("\n");
		sb.append("  operation: ").append(getName()).append("\n");
		sb.append("  timeUsec: ").append(getTimeUsec()).append("\n");

		sb.append("}\n");
		return sb.toString();
	}
}
