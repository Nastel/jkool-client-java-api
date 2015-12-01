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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.nastel.jkool.api.model.Activity;
import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.EventTypes;
import com.nastel.jkool.api.model.Property;
import com.nastel.jkool.api.model.Snapshot;
import com.nastel.jkool.api.utils.ApiException;
import com.nastel.jkool.api.utils.JsonUtil;

/**************************************************************************************************************************
 * This example code uses the same data as the prior Movie example code.  However it also demonstrates how to make use of Snapshots.  
 * In this example, snapshots are being used to capture the weather at the time the movie was playing.  
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 * ***********************************************************************************************************************/

public class MovieEventsWithCustomPropertiesAndActivityAndSnapshot {
	
	public static void main(String[] args) {
		try
		{

			String basePath = "http://data.jkoolcloud.com:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
			Response response = null;
			String movieDate = "03-Aug-2015 01:15:00";
			String startOfWeekDate = "03-Aug-2015 00:00:00";
			String endOfWeekDate = "09-Aug-2015 00:00:00";
			

			// Create the activity that the events will be attached to
			Activity activity = new Activity();
			String activityUuid = UUID.randomUUID().toString();
			activity.setTrackingId(activityUuid);
			activity.setActivityName("August Week 3 Movies");  // also referred to as "operation"
			activity.setEndTime(endOfWeekDate);
			activity.setStartTime(startOfWeekDate);
			activity.setStatus("END");
			activity.setAppl("WebMovies");
			activity.setServer("WebServer100");
			activity.setNetAddr("11.0.0.2");
			activity.setDataCenter("DCNY");
			activity.setGeoAddr("New York, NY");	
			
			// Create some snapshot custom fields
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
			
			// Attach the custom fields to snapshots 
			Snapshot snapshotTemp = new Snapshot();
			snapshotTemp.setCategory("Land");
			snapshotTemp.setName("Temperature");
			snapshotTemp.setType("SNAPSHOT");
			snapshotTemp.setTimeUsec(movieDate);
			snapshotTemp.setProperties(propertiesTemp);
			
			Snapshot snapshotHumidity = new Snapshot();
			snapshotHumidity.setCategory("Land");
			snapshotHumidity.setName("Humidity");
			snapshotHumidity.setType("SNAPSHOT");
			snapshotHumidity.setTimeUsec(movieDate);
			snapshotHumidity.setProperties(propertiesHumidity);
			
			Snapshot snapshotSeaLevel = new Snapshot();
			snapshotSeaLevel.setCategory("Sea");
			snapshotSeaLevel.setName("SeaLevel");
			snapshotSeaLevel.setType("SNAPSHOT");
			snapshotSeaLevel.setTimeUsec(movieDate);
			snapshotSeaLevel.setProperties(propertiesSeaLevel);
			
			//Create some event custom fields
			Property propertyName = new Property();
			propertyName.setName("MovieName");
			propertyName.setType("String");
			propertyName.setValue("Casablanca");
			
			Property propertyPrice = new Property();
			propertyPrice.setName("MoviePrice");
			propertyPrice.setType("Double");
			propertyPrice.setValue("10.50");
			
			Property propertyGenre = new Property();
			propertyGenre.setName("MovieGenre");
			propertyGenre.setType("String");
			propertyGenre.setValue("Drama");
			
			Property propertyTime = new Property();
			propertyTime.setName("MovieTime");
			propertyTime.setType("String");
			propertyTime.setValue("August 3, 2015 at 1PM");
			
			List<Property> propertiesMovie = new ArrayList<Property>();
			propertiesMovie.add(propertyGenre);
			propertiesMovie.add(propertyPrice);
			propertiesMovie.add(propertyName);
			propertiesMovie.add(propertyTime);

			// Create the Event
			// Attach it's snapshots
			// Attach it's properties
			// Attach the event to its parent activity 
			Event event = new Event();
			String eventUuid = UUID.randomUUID().toString();
			event.setTrackingId(eventUuid);
			event.setAppl("WebOrders");
			event.setServer("WebServer100");
			event.setNetAddr("11.0.0.2");
			event.setDataCenter("DC1");
			event.setGeoAddr("New York, NY");			
			event.setSourceUrl("http://www.movies.com");
			event.setLocation("New York, NY");
			event.setEventName("Cassablanca playing August 3rd at 1PM");
			event.setTimeUsec(movieDate);
			event.setMsgText(null);
			event.setMsgSize(0);
			// This attaches the event to the activity.
			event.setParentTrackId(activityUuid); 
			event.setType(EventTypes.EVENT); // Temporary - will be eliminated after next rollout
			
			// Attach the event's properties
			event.setProperties(propertiesMovie);
			
			// This attaches the snapshots to the event.
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
			
			// **************************************************************************************
			// And continue creating events for all of the movies playing in the third week of August. 
			// **************************************************************************************
			
			// ......
			
			// Stream the activity. 
			// (token is the token that was assigned to you when you purchased jKool.
			response = target.path("activity").request().header("token", "yourtoken").post(Entity.entity(serialize(activity), "application/json"));
			response.close();

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
