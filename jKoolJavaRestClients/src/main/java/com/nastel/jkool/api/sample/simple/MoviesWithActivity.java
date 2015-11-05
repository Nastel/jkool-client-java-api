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
 * This example demonstrates how to create movie events and attach them to an activity which holds all of the movies 
 * playing in a given week.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL REPLACE HARDCODED VALUES WITH YOUR APPLICATION VARIABLES.
 * ***********************************************************************************************************************/

public class MoviesWithActivity {
	
	public static void main(String[] args) {
		try
		{

			String basePath = "http://localhost:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
			Response response = null;
			String todaysDate = (new Date()).getTime() + "000";
			
			// Create the activity that the events will be attached to
			Activity activity = new Activity();
			String activityUuid = UUID.randomUUID().toString();
			activity.setTrackingId(activityUuid);
			activity.setActivityName("August Week 3 Movies");  // also referred to as "operation"
			activity.setTimeUsec(todaysDate);
			activity.setStatus("END");
			activity.setAppl("WebOrders");
			activity.setServer("WebServer100");
			activity.setNetAddr("11.0.0.2");
			activity.setDataCenter("DC1");
			activity.setGeoAddr("New York, NY");			
			// Create some custom fields
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
			
			List<Property> properties = new ArrayList<Property>();
			properties.add(propertyGenre);
			properties.add(propertyPrice);
			properties.add(propertyName);

			// Create the Event
			// Attach it's properties
			// Attach the event to its parent activity 
			Event event = new Event();
			String eventUuid = UUID.randomUUID().toString();
			event.setTrackingId(eventUuid);
			event.setAppl("WebOrders");
			event.setServer("WebServer100");
			event.setNetAddr("11.0.0.2");
			event.setDataCenter("DCNY");
			event.setGeoAddr("New York, NY");			
			event.setSourceUrl("http://www.movies.com");
			event.setLocation("New York, NY");
			event.setEventName("Casablanca 8/3 at 1PM");
			event.setTimeUsec(todaysDate);
			event.setMsgText(null);
			event.setMsgSize(0);
			// This attaches the event to the activity.
			event.setParentTrackId(activityUuid); 
			
			event.setProperties(properties);
			event.setSnapshots(null);

		
			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			response = target.path("event").request().header("token", "cathystoken").post(Entity.entity(serialize(event), "application/json"));
			response.close();	
			
			// **************************************************************************************
			// And continue creating events for all of the movies playing in the third week of August. 
			// **************************************************************************************
			
			// ......
			
			// Stream the activity after all events for that activity have been streamed. 
			// (token is the token that was assigned to you when you purchased jKool.
			response = target.path("activity").request().header("token", "cathystoken").post(Entity.entity(serialize(activity), "application/json"));
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
