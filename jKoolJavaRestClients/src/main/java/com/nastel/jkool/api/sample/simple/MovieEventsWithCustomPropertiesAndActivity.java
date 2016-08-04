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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.nastel.jkool.api.model.Activity;
import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.Property;
import com.nastel.jkool.api.utils.jKoolSend;

/**************************************************************************************************************************
 * This example demonstrates how to create movie events and attach them to an activity which holds all of the movies 
 * playing in a given week.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 * ***********************************************************************************************************************/

public class MovieEventsWithCustomPropertiesAndActivity {
	
	public static void main(String[] args) {
		try
		{
			String movieDate = "03-Aug-2016 01:15:00";
			String startOfWeekDate = "03-Aug-2016 00:00:00";
			String endOfWeekDate = "09-Aug-2016 00:00:00";
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			String token = "yourtoken";
			
			// Create the activity that the events will be attached to
			Activity activity = new Activity()
				.setActivityName("August Week 3 Movies")  // also referred to as "operation"
				.setStartTime(formatter.parse(startOfWeekDate))
				.setEndTime(formatter.parse(endOfWeekDate))
				.setStatus("END")
				.setAppl("WebOrders")
				.setServer("WebServer100")
				.setNetAddr("11.0.0.2")
				.setDataCenter("DC1")
				.setGeoAddr("40.803692,-73.402157");		
			
			// Create some custom fields
			Property propertyName = new Property("MovieName", "String","Casablanca",null);			
			Property propertyPrice = new Property("MoviePrice", "Double", "10.50", null);			
			Property propertyGenre = new Property("MovieGenre", "String", "Drama", null);
			
			List<Property> properties = new ArrayList<Property>();
			properties.add(propertyGenre);
			properties.add(propertyPrice);
			properties.add(propertyName);

			// Create the Event
			// Attach it's properties
			// Attach the event to its parent activity 
			Event event = new Event()
			.setAppl("WebOrders")
			.setServer("WebServer100")
			.setNetAddr("11.0.0.2")
			.setDataCenter("DCNY")
			.setGeoAddr("40.803692,-73.402157")		
			.setSourceUrl("http://www.movies.com")
			.setLocation("New York, NY")
			.setEventName("Casablanca 8/3 at 1PM")
			.setTimeUsec(formatter.parse(movieDate))
			.setMsgText(null)
			// This attaches the event to the activity.
			.setParentTrackId(activity.getTrackingId())
			.setProperties(properties)
			.setSnapshots(null);
			

		
			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			Response response = jKoolSend.post(event, token);
			response.close();
			
			// **************************************************************************************
			// And continue creating events for all of the movies playing in the third week of August. 
			// **************************************************************************************
			
			// ......
			
			// Stream the activity 
			// (token is the token that was assigned to you when you purchased jKool.
			response = jKoolSend.post(activity, token);
			response.close();

		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}
	


}
