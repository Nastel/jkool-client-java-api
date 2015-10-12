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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.Property;
import com.nastel.jkool.api.utils.ApiException;
import com.nastel.jkool.api.utils.JsonUtil;


/**************************************************************************************************************************
 * This example code portrays the most basic and simplistic use of jKool.  It uses only it's most basic fields to store data. 
 * More advanced fields are demonstrated in the other code examples.  This code will portray how to import basic weather 
 * data into jKool. 
 * WHEN USING THIS API IN REAL CODE, YOU WILL REPLACE HARDCODED VALUES WITH YOUR APPLICATION VARIABLES.
 * ***********************************************************************************************************************/

public class Weather {
	
	public static void main(String[] args) {
		try
		{

			String basePath = "http://data.jkoolcloud.com:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
			Response response = null;

			// Create the Event
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
			event.setEventName("August 3 Weather");
			event.setMsgText("This event contains weather information for August 9th.");
			event.setMsgSize(74);
			event.setTimeUsec("1444318021840858");
			
			// Create some custom fields
			Property propertyTempHigh = new Property();
			propertyTempHigh.setName("TempHigh");
			propertyTempHigh.setType("String");
			propertyTempHigh.setValue("95");
			
			Property propertyTempLow = new Property();
			propertyTempLow.setName("TempLow");
			propertyTempLow.setType("String");
			propertyTempLow.setValue("83");
			
			Property propertyHumidityMax = new Property();
			propertyHumidityMax.setName("HumidityMax");
			propertyHumidityMax.setType("String");
			propertyHumidityMax.setValue("95");
			
			Property propertyHumidityMin = new Property();
			propertyHumidityMin.setName("HumidityMin");
			propertyHumidityMin.setType("String");
			propertyHumidityMin.setValue("74");
			
			Property propertySeaLevelMax = new Property();
			propertySeaLevelMax.setName("SeaLevelMax");
			propertySeaLevelMax.setType("String");
			propertySeaLevelMax.setValue("31");
			
			Property propertySeaLevelMin = new Property();
			propertySeaLevelMin.setName("SealevelMin");
			propertySeaLevelMin.setType("String");
			propertySeaLevelMin.setValue("29");
			
			// Put the custom fields in a list
			List<Property> properties = new ArrayList<Property>();
			properties.add(propertySeaLevelMax);
			properties.add(propertySeaLevelMin);
			properties.add(propertyTempHigh);
			properties.add(propertyTempLow);
			properties.add(propertyHumidityMax);
			properties.add(propertyHumidityMin);
			
			// Attach this list of custom fields to the event
			event.setProperties(properties);
			
			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			response = target.path("event").request().header("token", "yourtoken").post(Entity.entity(serialize(event), "application/json"));
			response.close();	
			
			// **********************************************************************************
			// And continue creating events for all of the days you wish to report weather on. 
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
