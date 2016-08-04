package com.jkoolcloud.rest.api.jkool_java_rest.sample.simple;

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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.jkool_java_rest.model.Activity;
import com.jkoolcloud.rest.api.jkool_java_rest.model.Event;
import com.jkoolcloud.rest.api.jkool_java_rest.model.Property;
import com.jkoolcloud.rest.api.jkool_java_rest.model.Snapshot;
import com.jkoolcloud.rest.api.jkool_java_rest.utils.ApiException;
import com.jkoolcloud.rest.api.jkool_java_rest.utils.JsonUtil;
import com.jkoolcloud.rest.api.jkool_java_rest.utils.jKoolSend;

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
			String movieDate = "03-Aug-2016 01:15:00";
			String startOfWeekDate = "03-Aug-2016 00:00:00";
			String endOfWeekDate = "09-Aug-2016 00:00:00";
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			jKoolSend jkSend = new jKoolSend("0bb480b6-582a-42e7-aeb0-3bd9ee40f4ee");

			// Create the activity that the events will be attached to
			Activity activity = new Activity()
			.setActivityName("August Week 3 Movies") // also referred to as "operation"
			.setEndTime(formatter.parse(endOfWeekDate))
			.setStartTime(formatter.parse(startOfWeekDate))
			.setStatus("END")
			.setAppl("WebMovies")
			.setServer("WebServer100")
			.setNetAddr("11.0.0.2")
			.setDataCenter("DCNY")
			.setGeoAddr("40.803692,-73.402157");
			
			// Create some snapshot custom fields
			Property propertyTempHigh = new Property("TempHigh", "String", "95", null);
			Property propertyTempLow = new Property("TempLow","String","83", null);
		
			List<Property> propertiesTemp = new ArrayList<Property>();
			propertiesTemp.add(propertyTempHigh);
			propertiesTemp.add(propertyTempLow);
			
			Property propertyHumidityMax = new Property("HumidityMax", "String", "95", null);
			Property propertyHumidityMin = new Property("HumidityMin", "String", "74", null);

			List<Property> propertiesHumidity = new ArrayList<Property>();
			propertiesHumidity.add(propertyHumidityMax);
			propertiesHumidity.add(propertyHumidityMin);
			
			Property propertySeaLevelMax = new Property("SeaLevelMax","String","31",null );
			Property propertySeaLevelMin = new Property("SealevelMin", "String", "29", null);
			
			List<Property> propertiesSeaLevel = new ArrayList<Property>();
			propertiesSeaLevel.add(propertySeaLevelMax);
			propertiesSeaLevel.add(propertySeaLevelMin);
			
			// Attach the custom fields to snapshots 
			Snapshot snapshotTemp = new Snapshot("Land", "Temperature", formatter.parse(movieDate), propertiesTemp);			
			Snapshot snapshotHumidity = new Snapshot("Land","Humidity",formatter.parse(movieDate), propertiesHumidity);
			Snapshot snapshotSeaLevel = new Snapshot("Sea", "SeaLevel", formatter.parse(movieDate), propertiesSeaLevel);
			
			//Create some event custom fields
			Property propertyName = new Property();
			propertyName.setName("MovieName");
			propertyName.setType("String");
			propertyName.setValue("Casablanca");
			
			Property propertyPrice = new Property("MoviePrice", "Double", "10.50", null);
			Property propertyGenre = new Property("MovieGenre", "String", "Drama", null);			
			Property propertyTime = new Property("MovieTime", "String", "August 3, 2015 at 1PM", null);
			
			List<Property> propertiesMovie = new ArrayList<Property>();
			propertiesMovie.add(propertyGenre);
			propertiesMovie.add(propertyPrice);
			propertiesMovie.add(propertyName);
			propertiesMovie.add(propertyTime);

			// Create the Event
			// Attach it's snapshots
			// Attach it's properties
			// Attach the event to its parent activity 
			String eventUuid = UUID.randomUUID().toString();
			Event event = new Event()
		    .setTrackingId(eventUuid)
			.setAppl("WebOrders")
			.setServer("WebServer100")
			.setNetAddr("11.0.0.2")
			.setDataCenter("DC1")
			.setGeoAddr("(40.803692,-73.402157)")			
			.setSourceUrl("http://www.movies.com")
			.setLocation("New York, NY")
			.setEventName("Cassablanca playing August 3rd at 1PM")
			.setTimeUsec(formatter.parse(movieDate))
			.setMsgText(null)
			// This attaches the event to the activity.
			.setParentTrackId(activity.getTrackingId())
			// Attach the event's properties
			.setProperties(propertiesMovie);
			
			// This attaches the snapshots to the event.
			List<Snapshot> snapshots = new ArrayList<Snapshot>(); 
			snapshots.add(snapshotTemp);
			snapshots.add(snapshotHumidity);
			snapshots.add(snapshotSeaLevel);
			event.setSnapshots(snapshots);

		
			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			Response response = jkSend.post(event);
			response.close();

			
			// **************************************************************************************
			// And continue creating events for all of the movies playing in the third week of August. 
			// **************************************************************************************
			
			// ......
			
			// Stream the activity. 
			// (token is the token that was assigned to you when you purchased jKool.
			response = jkSend.post(activity);
			response.close();


		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}
	


}
