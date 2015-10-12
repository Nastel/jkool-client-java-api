package com.nastel.jkool.api.sample.simple;

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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.nastel.jkool.api.model.Activity;
import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.Property;
import com.nastel.jkool.api.model.Snapshot;
import com.nastel.jkool.api.utils.ApiException;
import com.nastel.jkool.api.utils.JsonUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**************************************************************************************************************************
 * This example code uses the same data as the Weather example code.  However it demonstrates how to make use of Activities 
 * and Snapshots.  Activities are used to group events. Snapshots are used to categorize custom fields.  It is the intent of 
 * this code to give you a clear understanding of how to use activities and snapshots and to help you identify the use 
 * cases they apply to. 
 * WHEN USING THIS API IN REAL CODE, YOU WILL REPLACE HARDCODED VALUES WITH YOUR APPLICATION VARIABLES.
 * ***********************************************************************************************************************/

public class WeatherWithSnapshotsAndActivity {
	
	public static void main(String[] args) {
		try
		{

			String basePath = "http://data.jkoolcloud.com:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
			Response response = null;
			String todaysDate = (new Date()).getTime() + "000";
			
			// Create the activity that the events will be attached to
			Activity activity = new Activity();
			String activityUuid = UUID.randomUUID().toString();
			activity.setTrackingId(activityUuid);
			activity.setActivityName("August Week 3 Weather");  // also referred to as "operation"
			activity.setTimeUsec(todaysDate);
			activity.setStatus("END");
			activity.setAppl("WebOrders");
			activity.setServer("WebServer100");
			activity.setNetAddr("11.0.0.2");
			activity.setDataCenter("DC1");
			activity.setGeoAddr("New York, NY");			
			// Create some custom fields
			Property propertyTempHigh = new Property();
			propertyTempHigh.setName("TempHigh");
			propertyTempHigh.setType("String");
			propertyTempHigh.setValue("95");
			
			Property propertyTempLow = new Property();
			propertyTempLow.setName("TempLow");
			propertyTempLow.setType("String");
			propertyTempLow.setValue("83");
			
			List<Property> propertiesTemp = new ArrayList<Property>();
			propertiesTemp.add(propertyTempHigh);
			propertiesTemp.add(propertyTempLow);
			
			Property propertyHumidityMax = new Property();
			propertyHumidityMax.setName("HumidityMax");
			propertyHumidityMax.setType("String");
			propertyHumidityMax.setValue("95");
			
			Property propertyHumidityMin = new Property();
			propertyHumidityMin.setName("HumidityMin");
			propertyHumidityMin.setType("String");
			propertyHumidityMin.setValue("74");
			
			List<Property> propertiesHumidity = new ArrayList<Property>();
			propertiesHumidity.add(propertyHumidityMax);
			propertiesHumidity.add(propertyHumidityMin);
			
			Property propertySeaLevelMax = new Property();
			propertySeaLevelMax.setName("SeaLevelMax");
			propertySeaLevelMax.setType("String");
			propertySeaLevelMax.setValue("31");
			
			Property propertySeaLevelMin = new Property();
			propertySeaLevelMin.setName("SealevelMin");
			propertySeaLevelMin.setType("String");
			propertySeaLevelMin.setValue("29");
			
			List<Property> propertiesSeaLevel = new ArrayList<Property>();
			propertiesSeaLevel.add(propertySeaLevelMax);
			propertiesSeaLevel.add(propertySeaLevelMin);
			
			// Attach the custom fields to snapshots (snapshots categorize custom fields)
			Snapshot snapshotTemp = new Snapshot();
			snapshotTemp.setCategory("Land");
			snapshotTemp.setName("Temperature");
			snapshotTemp.setType("SNAPSHOT");
			snapshotTemp.setTimeUsec(todaysDate);
			snapshotTemp.setProperties(propertiesTemp);
			
			Snapshot snapshotHumidity = new Snapshot();
			snapshotHumidity.setCategory("Land");
			snapshotHumidity.setName("Humidity");
			snapshotHumidity.setType("SNAPSHOT");
			snapshotHumidity.setTimeUsec(todaysDate);
			snapshotHumidity.setProperties(propertiesHumidity);
			
			Snapshot snapshotSeaLevel = new Snapshot();
			snapshotSeaLevel.setCategory("Sea");
			snapshotSeaLevel.setName("SeaLevel");
			snapshotSeaLevel.setType("SNAPSHOT");
			snapshotSeaLevel.setTimeUsec(todaysDate);
			snapshotSeaLevel.setProperties(propertiesSeaLevel);

			// Create the Event
			// Attach it's snapshots
			// Attach the event to its parent activity 
			Event event = new Event();
			String eventUuid = UUID.randomUUID().toString();
			event.setTrackingId(eventUuid);
			event.setAppl("WebOrders");
			event.setServer("WebServer100");
			event.setNetAddr("11.0.0.2");
			event.setDataCenter("DC1");
			event.setGeoAddr("New York, NY");			
			event.setSourceUrl("http://www.wunderground.com");
			event.setLocation("New York, NY");
			event.setEventName("August 22 Weather");
			event.setTimeUsec(todaysDate);
			event.setMsgText("This event contains weather information for August 22th.");
			event.setMsgSize(74);
			// This attaches the event to the activity.
			event.setParentTrackId(activityUuid); 
			
			// This attaches the event to its snapshots.
			snapshotHumidity.setParentId(eventUuid);
			snapshotTemp.setParentId(eventUuid);
			snapshotSeaLevel.setParentId(eventUuid);
			List<Snapshot> snapshots = new ArrayList<Snapshot>(); 
			snapshots.add(snapshotTemp);
			snapshots.add(snapshotHumidity);
			snapshots.add(snapshotSeaLevel);
			event.setSnapshots(snapshots);

		
			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			response = target.path("event").request().header("token", "yourtoken").post(Entity.entity(serialize(event), "application/json"));
			response.close();	
			
			// **********************************************************************************
			// And continue creating events for all of the days in the third week of August. 
			// **********************************************************************************
			
			// ......
			
			// Stream the activity after all events for that activity have been streamed. 
			// (token is the token that was assigned to you when you purchased jKool.
			response = target.path("activity").request().header("token", "yourtoken").post(Entity.entity(serialize(activity), "application/json"));
			response.close();
			
			// **********************************************************************************
			// Ditto the above to do the third week of August 
			// **********************************************************************************
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}
	
	 /**
	   * Serialize the given Java object into JSON string.
	   */
	  public static String serialize(Object obj) throws ApiException {
	    try {
	      if (obj != null)
	        return JsonUtil.getJsonMapper().writeValueAsString(obj);
	      else
	        return null;
	    }
	    catch (Exception e) {
	      throw new ApiException(500, e.getMessage());
	    }
	  }

}
