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
import io.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonProperty;


@ApiModel(description = "")
public class Property  {
  
  private String name = null;
  private String type = null;
  private String value = null;
  private String valueType = null;

  public Property() {

}
  
public Property(String name, String type, String value, String valueType) {
	super();
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
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("value")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("value-type")
  public String getValueType() {
	return valueType;
  }
  public void setValueType(String valueType) {
	this.valueType = valueType;
  }
  
@Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Property {\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  value: ").append(value).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
