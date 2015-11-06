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
import java.util.UUID;

import com.nastel.jkool.api.model.Activity;
import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.Property;
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
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 * ***********************************************************************************************************************/

public class MovieEventsWithCustomProperties {
	
	public static void main(String[] args) {
		try
		{

			String basePath = "http://data.jkoolcloud.com:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
			Response response = null;
			String movieDate = "03-Aug-2015 01:15:00";
					
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
			event.setTimeUsec(movieDate);
			event.setMsgText(null);
			event.setMsgSize(0);

			event.setProperties(properties);
			event.setSnapshots(null);

		
			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			response = target.path("event").request().header("token", "yourtoken").post(Entity.entity(serialize(event), "application/json"));
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
