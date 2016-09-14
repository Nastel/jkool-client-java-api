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

import io.swagger.annotations.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class implements a property with name, value, data type & value type.
 * 
 * @author cathy
 */
@ApiModel(description = "")
public class Property implements Validated {

	private String name;
	private Object value;
	private String dataType;
	private String valueType;

	/**
	 * Create an empty property
	 * 
	 */
	public Property() {
	}

	/**
	 * Create a property
	 * 
	 * @param name property name
	 * @param value property value
	 */
	public Property(String name, Object value) {
		this(name, value, ValueType.VALUE_TYPE_NONE, getDataType(value));
	}

	/**
	 * Create a property
	 * 
	 * @param name property name
	 * @param value property value
	 * @param valueType property value type {@link ValueTypes}
	 * @see ValueTypes
	 */
	public Property(String name, Object value, String valueType) {
		this(name, value, getDataType(value), valueType);
	}

	/**
	 * Create a property
	 * 
	 * @param name property name
	 * @param value property value
	 * @param valueType property value type {@link ValueTypes}
	 * @param dataType property value data type
	 * @see ValueTypes
	 */
	public Property(String name, Object value, String valueType, String dataType) {
		this.name = name;
		this.value = value;
		this.dataType = dataType;
		this.valueType = valueType;
	}

	/**
	 * Validate fields of this entity
	 *
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		return name != null && dataType != null && value != null;
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
		return dataType;
	}

	/**
	 * Set property value data type
	 * 
	 * @param dataType property value data type
	 * @return self
	 */
	public Property setType(String dataType) {
		this.dataType = dataType;
		return this;
	}

	/**
	**/
	@ApiModelProperty(value = "")
	@JsonProperty("value")
	public String getValue() {
		return value != null? String.valueOf(value): null;
	}

	/**
	 * Set property value
	 * 
	 * @param value property value
	 * @return self
	 */
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

	/**
	 * Set property value type
	 * 
	 * @param valueType property value type {@link ValueTypes}
	 * @return self
	 * @see ValueTypes
	 */
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
		sb.append("  dtype: ").append(dataType).append("\n");
		sb.append("  vtype: ").append(valueType).append("\n");
		sb.append("  value: ").append(value).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}
