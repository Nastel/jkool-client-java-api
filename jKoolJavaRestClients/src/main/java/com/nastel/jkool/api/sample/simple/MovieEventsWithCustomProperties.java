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

import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.Property;
import com.nastel.jkool.api.utils.ApiException;
import com.nastel.jkool.api.utils.JsonUtil;
import com.nastel.jkool.api.utils.jKoolSend;

/**************************************************************************************************************************
 * This example demonstrates how to create movie events with custom properties.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 * ***********************************************************************************************************************/

public class MovieEventsWithCustomProperties {
	
	public static void main(String[] args) {
		try
		{

			String movieDate = "03-Aug-2016 01:15:00";
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			String token = "0bb480b6-582a-42e7-aeb0-3bd9ee40f4ee";
					
			// Create some custom fields
			Property propertyName = new Property("MovieName", "String", "Casablanca", null);
			Property propertyPrice = new Property("MoviePrice", "Double", "10.50", null);
			Property propertyGenre = new Property("MovieGenre", "String", "Drama", null);
			
			List<Property> properties = new ArrayList<Property>();
			properties.add(propertyGenre);
			properties.add(propertyPrice);
			properties.add(propertyName);

			// Create the Event
			// Attach it's properties
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
			.setProperties(properties)
			.setSnapshots(null);

			// Stream the event (token is the token that was assigned to you when you purchased jKool.
			Response response = jKoolSend.post(event, token);
			response.close();
			


		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}
	


}
