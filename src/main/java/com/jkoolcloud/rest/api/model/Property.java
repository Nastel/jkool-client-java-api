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

import io.swagger.annotations.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "")
public class Property {

	private String name;
	private String type;
	private Object value;
	private String valueType;

	public Property() {
	}

	public Property(String name, Object value) {
		this(name, getDataType(value), value, null);
	}

	public Property(String name, Object value, String valueType) {
		this(name, getDataType(value), value, valueType);
	}

	public Property(String name, String type, String value) {
		this(name, type, value, null);
	}

	public Property(String name, String type, Object value, String valueType) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.valueType = valueType;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public Property setName(String name) {
		this.name = name;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public Property setType(String type) {
		this.type = type;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("value")
	public String getValue() {
		return value != null? String.valueOf(value): null;
	}

	public Property setValue(Object value) {
		this.value = value;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("value-type")
	public String getValueType() {
		return valueType;
	}

	public Property setValueType(String valueType) {
		this.valueType = valueType;
		return this;
	}

	/**
	 * Obtain the language independent value data type of the property
	 * 
	 * @return string representation of the value data type
	 */
	public static String getDataType(Object value) {
		if (value instanceof String) {
			return "string";
		} else if (value instanceof Long) {
			return "long";
		} else if (value instanceof Integer) {
			return "int";
		} else if (value instanceof Double) {
			return "double";
		} else if (value instanceof Float) {
			return "float";
		} else if (value instanceof Boolean) {
			return "bool";
		} else if (value instanceof Byte) {
			return "byte";
		} else if (value instanceof Short) {
			return "short";
		} else if (value instanceof Character) {
			return "char";
		} else if (value instanceof Date) {
			return "date";
		} else if (value != null) {
			return value.getClass().getSimpleName();
		} else {
			return "none";
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Property {\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  type: ").append(type).append("\n");
		sb.append("  value: ").append(value).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
