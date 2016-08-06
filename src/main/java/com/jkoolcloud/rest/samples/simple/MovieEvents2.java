package com.jkoolcloud.rest.samples.simple;

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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.model.Event;
import com.jkoolcloud.rest.api.model.Property;
import com.jkoolcloud.rest.api.service.JKStream;

/**************************************************************************************************************************
 * This example demonstrates how to create movie events with custom properties.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD
 * OF HARDCODED VALUES.
 ***********************************************************************************************************************/

public class MovieEvents2 {

	public static void main(String[] args) {
		try {
			JKStream jkSend = new JKStream("your-access-token");

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
			Event event = new Event();
			event.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
					.setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2)).setGeoAddr("40.803692,-73.402157")
					.setSourceUrl("http://www.movies.com").setLocation("New York, NY").setName("Casablanca")
					.setProperties(properties);

			// Stream the event (token is the token that was assigned to you
			// when you purchased jKool.
			Response response = jkSend.post(event);
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
