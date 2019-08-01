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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class defines an activity -- a group of related events, such as transaction.
 * 
 * @author cathy
 */
@Schema(description = "")
public class Activity extends Trackable {

	/**
	 * Create an activity
	 * 
	 */
	public Activity() {
		super();
		setType(EvType.ACTIVITY);
	}

	/**
	 * Create an activity
	 * 
	 * @param name
	 *            associated with the entity
	 */
	public Activity(String name) {
		super(name);
		setType(EvType.ACTIVITY);
	}

	/**
	 * Create an activity
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 */
	public Activity(String name, String tid) {
		super(name, tid);
		setType(EvType.ACTIVITY);
	}

	/**
	 * Create an activity
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 * @param timeMs
	 *            timestamp associated with the entity
	 */
	public Activity(String name, String tid, long timeMs) {
		super(name, tid, timeMs);
		setType(EvType.ACTIVITY);
	}

	/**
	 * Create an activity
	 * 
	 * @param name
	 *            associated with the entity
	 * @param tid
	 *            tracking id associates with the entity
	 * @param time
	 *            timestamp associated with the entity
	 */
	public Activity(String name, String tid, Date time) {
		super(name, tid, time);
		setType(EvType.ACTIVITY);
	}

	/**
	 * @return activity status string
	 **/
	@Schema(description = "")
	@JsonProperty("status")
	public String getStatus() {
		return exception == null ? "END" : "EXCEPTION";
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
