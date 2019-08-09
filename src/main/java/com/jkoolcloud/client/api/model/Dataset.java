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
import java.util.List;

public class Dataset extends Snapshot {

	/**
	 * Create a Dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 */
	public Dataset(String category, String name) {
		super(category, name);
		this.type = EvType.DATASET;
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param timeMs
	 *            timestamp associated with the entity
	 */
	public Dataset(String category, String name, long timeMs) {
		super(category, name, timeMs);
		this.type = EvType.DATASET;
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param props
	 *            a variable list of properties
	 */
	public Dataset(String category, String name, List<Property> props) {
		this(category, name, System.currentTimeMillis());
		addProperty(props);
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param props
	 *            a variable list of properties
	 */
	public Dataset(String category, String name, Property... props) {
		this(category, name, System.currentTimeMillis());
		addProperty(props);
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param timeMs
	 *            timestamp associated with the entity
	 * @param props
	 *            list of properties
	 */
	public Dataset(String category, String name, long timeMs, List<Property> props) {
		this(category, name, timeMs);
		addProperty(props);
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param timeMs
	 *            timestamp associated with the entity
	 * @param props
	 *            a variable list of properties
	 */
	public Dataset(String category, String name, long timeMs, Property... props) {
		this(category, name, timeMs);
		addProperty(props);
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param time
	 *            timestamp associated with the entity
	 * @param props
	 *            a list of properties
	 */
	public Dataset(String category, String name, Date time, List<Property> props) {
		this(category, name, time.getTime());
		addProperty(props);
	}

	/**
	 * Create a dataset
	 * 
	 * @param category
	 *            dataset category
	 * @param name
	 *            associated with the dataset
	 * @param time
	 *            timestamp associated with the entity
	 * @param props
	 *            a variable list of properties
	 */
	public Dataset(String category, String name, Date time, Property... props) {
		this(category, name, time.getTime());
		addProperty(props);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" {\n");
		sb.append("  type: ").append(this.getType()).append("\n");
		sb.append("  category: ").append(this.getCategory()).append("\n");
		sb.append("  name: ").append(this.getName()).append("\n");
		sb.append("  timeUsec: ").append(this.getTimeUsec()).append("\n");
		sb.append("  properties: ").append(this.getProperties()).append("\n");
		sb.append("}\n");

		return sb.toString();
	}
}
