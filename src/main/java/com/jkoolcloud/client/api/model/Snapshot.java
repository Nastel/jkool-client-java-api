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
package com.jkoolcloud.client.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class defines a snapshot, which is a collection of properties at a
 * particular moment in time.
 * 
 * @author cathy
 */
@ApiModel(description = "")
public class Snapshot implements Validated {

	private String category;
	private String name;
	private long timeUsec;
	private List<Property> properties = new ArrayList<Property>();
	private EventTypes type = EventTypes.SNAPSHOT;

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 */
	public Snapshot(String category, String name) {
		this(category, name, System.currentTimeMillis());
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param timeMs
	 *            timestamp associated with the entity
	 */
	public Snapshot(String category, String name, long timeMs) {
		this.category = category;
		this.name = name;
		this.timeUsec = timeMs * 1000;
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param props
	 *            a variable list of properties
	 */
	public Snapshot(String category, String name, List<Property> props) {
		this(category, name, System.currentTimeMillis());
		addProperty(props);
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param props
	 *            a variable list of properties
	 */
	public Snapshot(String category, String name, Property... props) {
		this(category, name, System.currentTimeMillis());
		addProperty(props);
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param timeMs
	 *            timestamp associated with the entity
	 * @param props
	 *            list of properties
	 */
	public Snapshot(String category, String name, long timeMs, List<Property> props) {
		this(category, name, timeMs);
		addProperty(props);
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param timeMs
	 *            timestamp associated with the entity
	 * @param props
	 *            a variable list of properties
	 */
	public Snapshot(String category, String name, long timeMs, Property... props) {
		this(category, name, timeMs);
		addProperty(props);
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param time
	 *            timestamp associated with the entity
	 * @param props
	 *            a list of properties
	 */
	public Snapshot(String category, String name, Date time, List<Property> props) {
		this(category, name, time.getTime());
		addProperty(props);
	}

	/**
	 * Create a snapshot
	 * 
	 * @param category
	 *            snapshot category
	 * @param name
	 *            associated with the snapshot
	 * @param time
	 *            timestamp associated with the entity
	 * @param props
	 *            a variable list of properties
	 */
	public Snapshot(String category, String name, Date time, Property... props) {
		this(category, name, time.getTime());
		addProperty(props);
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
	 * @return category
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	/**
	 * Assign snapshot category
	 *
	 * @param category
	 *            snapshot category
	 * @return self
	 */
	public Snapshot setCategory(String category) {
		this.category = category;
		return this;
	}

	/**
	 * @return name
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Assign snapshot name
	 *
	 * @param name
	 *            snapshot name
	 * @return self
	 */
	public Snapshot setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return start time in microseconds
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
	 * @return type
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public EventTypes getType() {
		return type;
	}

	/**
	 * @return properties list
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("properties")
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * Add snapshot properties
	 *
	 * @param props
	 *            list of properties
	 * @return self
	 */
	public Snapshot addProperty(List<Property> props) {
		this.properties.addAll(props);
		return this;
	}

	/**
	 * Add snapshot properties
	 *
	 * @param props
	 *            list of properties
	 * @return self
	 */
	public Snapshot addProperty(Property... props) {
		if (props != null) {
			return addProperty(Arrays.asList(props));
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Snapshot {\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  category: ").append(category).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  timeUsec: ").append(timeUsec).append("\n");
		sb.append("  properties: ").append(properties).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
