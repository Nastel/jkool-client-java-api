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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "")
public class Snapshot implements Validated {

	private String category;
	private String name;
	private long timeUsec;
	private List<Property> properties;
	private EventTypes type = EventTypes.SNAPSHOT;

	public Snapshot(String category, String name) {
		this(category, name, System.currentTimeMillis());
	}

	public Snapshot(String category, String name, long timeMs) {
		this.category = category;
		this.name = name;
		this.timeUsec = timeMs * 1000;
	}

	public Snapshot(String category, String name, List<Property> properties) {
		this(category, name, System.currentTimeMillis());
		this.properties = properties;
	}
	
	public Snapshot(String category, String name, long timeMs, List<Property> properties) {
		this(category, name, timeMs);
		this.properties = properties;
	}

	public Snapshot(String category, String name, Date time, List<Property> properties) {
		this(category, name, time.getTime());
		this.properties = properties;
	}

	/**
	 * Validate fields of this entity
	 *
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		return category != null && name != null && (timeUsec > 0) && (properties != null && properties.size() > 0);
	}

	/**
	 * /**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	public Snapshot setCategory(String category) {
		this.category = category;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public Snapshot setName(String name) {
		this.name = name;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("time-usec")
	public long getTimeUsec() {
		return timeUsec;
	}

	public Snapshot setTimeUsec(Date timeUsec) {
		this.timeUsec = timeUsec.getTime() * 1000;
		return this;
	}

	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public EventTypes getType() {
		return type;
	}


	/**
   **/
	@ApiModelProperty(value = "")
	@JsonProperty("properties")
	public List<Property> getProperties() {
		return properties;
	}

	public Snapshot setProperties(List<Property> properties) {
		this.properties = properties;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Snapshot {\n");
		sb.append("  category: ").append(category).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  properties: ").append(properties).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
