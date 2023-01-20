/*
 * Copyright 2014-2023 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkoolcloud.client.samples.simple;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.jkoolcloud.client.api.model.Activity;
import com.jkoolcloud.client.api.model.Event;
import com.jkoolcloud.client.api.model.Property;
import com.jkoolcloud.client.api.model.ValueType;
import com.jkoolcloud.client.api.service.JKStream;

import jakarta.ws.rs.core.Response;

/**************************************************************************************************************************
 * This example demonstrates how to create movie events and attach them to an activity which holds all of the movies
 * playing in a given week.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 ***********************************************************************************************************************/

public class MovieEvents3 {

	public static void main(String[] args) {
		try (JKStream jkSend = new JKStream(System.getProperty("jk.access.token", "access-token"))) {

			// Create the activity that the events will be attached to
			Activity activity = new Activity();

			activity.setName("Weekly Movies").setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DC1")
					.setGeoAddr("40.803692,-73.402157");

			// Create some custom fields
			Property propertyName = new Property("MovieName", "Casablanca");
			Property propertyGenre = new Property("MovieGenre", "Drama");
			Property propertyPrice = new Property("MoviePrice", 10.50, ValueType.VALUE_TYPE_CURRENCY_USD);

			// Create the Event
			// Attach it's properties
			// Attach the event to its parent activity
			Event event = new Event();
			event.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
					.setGeoAddr("40.803692,-73.402157").setSourceUrl("http://www.movies.com")
					.setLocation("New York, NY").setName("Casablanca").setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2))
					// This attaches the event to the activity.
					.setParentTrackId(activity.getTrackingId()).addProperty(propertyGenre, propertyPrice, propertyName);

			// Stream the event (token is the token that was assigned to you
			// when you purchased jKool.
			Response response = jkSend.post(event);
			response.close();

			// **************************************************************************************
			// And continue creating events for all of the movies playing in the
			// third week of August.
			// **************************************************************************************

			// ......

			// Stream the activity
			// (token is the token that was assigned to you when you purchased
			// jKool.
			response = jkSend.post(activity);
			response.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
