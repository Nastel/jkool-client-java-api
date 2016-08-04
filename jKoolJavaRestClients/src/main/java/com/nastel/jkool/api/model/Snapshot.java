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
import com.nastel.jkool.api.model.Property;

@ApiModel(description = "")
public class Snapshot {

	private String category = null;
	private String name = null;
	private long timeUsec;
	private String type = null;
	private List<Property> properties;

	public Snapshot() {
		this.timeUsec = System.currentTimeMillis() * 1000;
	}

	public Snapshot(String category, String name) {
		this(category, name, System.currentTimeMillis());
	}

	public Snapshot(String category, String name, long timeMs) {
		this.category = category;
		this.name = name;
		this.timeUsec = timeMs * 1000;
	}

	public Snapshot(String category, String name, Date time, List<Property> properties) {
		this(category, name, time.getTime());
		this.properties = properties;
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
	public Long getTimeUsec() {
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
	public String getType() {
		return "SNAPSHOT";
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
