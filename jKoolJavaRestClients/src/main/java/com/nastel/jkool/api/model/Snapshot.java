package com.nastel.jkool.api.model;

import java.util.*;

import io.swagger.annotations.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nastel.jkool.api.model.Property;


@ApiModel(description = "")
public class Snapshot  {
  
  private String parentId = null;
  private String fqn = null;
  private String category = null;
  private String name = null;
  private Integer count = null;
  private String timeUsec = null;
  private String severity = null;
  private String severityNo = null;
  private String type = null;
  private String typeNo = null;
  private List<Property> properties;
  private String trackId = null;
  private String source = null;
  private String sourcdFqn = null;
  private String sourceInfo = null;
  private String sourceUrl = null;
  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("parent-id")
  public String getParentId() {
    return parentId;
  }
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("fqn")
  public String getFqn() {
    return fqn;
  }
  public void setFqn(String fqn) {
    this.fqn = fqn;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
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
  @JsonProperty("count")
  public Integer getCount() {
    return count;
  }
  public void setCount(Integer count) {
    this.count = count;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("time-usec")
  public String getTimeUsec() {
    return timeUsec;
  }
  public void setTimeUsec(String timeUsec) {
    this.timeUsec = timeUsec;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("severity")
  public String getSeverity() {
    return severity;
  }
  public void setSeverity(String severity) {
    this.severity = severity;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("severity-no")
  public String getSeverityNo() {
    return severityNo;
  }
  public void setSeverityNo(String severityNo) {
    this.severityNo = severityNo;
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
  @JsonProperty("type-no")
  public String getTypeNo() {
    return typeNo;
  }
  public void setTypeNo(String typeNo) {
    this.typeNo = typeNo;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  @JsonProperty("properties")
 // public HashMap<String, Property> getProperties() {
  public List<Property> getProperties() {
    return properties;
  }
  //public void setProperties(HashMap<String, Property> properties) {
  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("track-id")
    public String getTrackId() {
    	return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("source")
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("source-fqn")
	public String getSourcdFqn() {
		return sourcdFqn;
	}
	public void setSourcdFqn(String sourcdFqn) {
		this.sourcdFqn = sourcdFqn;
	}
	
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("source-info")
	public String getSourceInfo() {
		return sourceInfo;
	}
	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}
	
	/**
	 **/
	@ApiModelProperty(value = "")
	@JsonProperty("sourceUrl")	
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
@Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Snapshot {\n");
    
    sb.append("  parentId: ").append(parentId).append("\n");
    sb.append("  fqn: ").append(fqn).append("\n");
    sb.append("  category: ").append(category).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  count: ").append(count).append("\n");
    sb.append("  timeUsec: ").append(timeUsec).append("\n");
    sb.append("  severity: ").append(severity).append("\n");
    sb.append("  severityNo: ").append(severityNo).append("\n");
    sb.append("  type: ").append(type).append("\n");
    sb.append("  typeNo: ").append(typeNo).append("\n");
    sb.append("  properties: ").append(properties).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
