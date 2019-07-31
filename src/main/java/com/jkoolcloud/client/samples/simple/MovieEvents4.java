package com.jkoolcloud.client.samples.simple;

/*
 * Copyright 2014-2019 JKOOL, LLC.
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

import com.jkoolcloud.client.api.model.*;
import com.jkoolcloud.client.api.service.JKStream;

/**************************************************************************************************************************
 * This example code uses the same data as the prior Movie example code. However it also demonstrates how to make use of
 * Snapshots. In this example, snapshots are being used to capture the weather at the time the movie was playing.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 ***********************************************************************************************************************/

public class MovieEvents4 {

	public static void main(String[] args) {
		try {
			JKStream jkSend = new JKStream(System.getProperty("jk.access.token", "access-token"));

			// Create the activity that the events will be attached to
			Activity activity = new Activity();
			activity.setName("Weekly Movies")
					// also referred to as "operation"
					.setAppl("WebMovies").setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
					.setGeoAddr("40.803692,-73.402157");

			// Create some snapshot custom fields
			Property propertyTempHigh = new Property("TempHigh", 95);
			Property propertyTempLow = new Property("TempLow", 83);

			List<Property> propertiesTemp = new ArrayList<> ();
			propertiesTemp.add(propertyTempHigh);
			propertiesTemp.add(propertyTempLow);

			Property propertyHumidityMax = new Property("HumidityMax", 95, ValueType.VALUE_TYPE_GAUGE);
			Property propertyHumidityMin = new Property("HumidityMin", 74, ValueType.VALUE_TYPE_GAUGE);

			List<Property> propertiesHumidity = new ArrayList<> ();
			propertiesHumidity.add(propertyHumidityMax);
			propertiesHumidity.add(propertyHumidityMin);

			Property propertySeaLevelMax = new Property("SeaLevelMax", 31, ValueType.VALUE_TYPE_GAUGE);
			Property propertySeaLevelMin = new Property("SealevelMin", 29, ValueType.VALUE_TYPE_GAUGE);

			List<Property> propertiesSeaLevel = new ArrayList<> ();
			propertiesSeaLevel.add(propertySeaLevelMax);
			propertiesSeaLevel.add(propertySeaLevelMin);

			// Attach the custom fields to snapshots (categorized properties)
			Snapshot snapshotTemp = new Snapshot("Land", "Temperature", propertiesTemp);
			Snapshot snapshotHumidity = new Snapshot("Land", "Humidity", propertiesHumidity);
			Snapshot snapshotSeaLevel = new Snapshot("Sea", "SeaLevel", propertiesSeaLevel);

			// Create some event custom fields
			Property propertyName = new Property();
			propertyName.setName("MovieName");
			propertyName.setType("String");
			propertyName.setValue("Casablanca");

			Property propertyGenre = new Property("MovieGenre", "Drama");
			Property propertyPrice = new Property("MoviePrice", 10.50, ValueType.VALUE_TYPE_CURRENCY_USD);
			Property propertyTime = new Property("MovieTime", "August 3, 2018 at 1PM", ValueType.VALUE_TYPE_TIMESTAMP);

			// Create the Event
			// Attach it's snapshots
			// Attach it's properties
			// Attach the event to its parent activity
			Event event = new Event();
			event.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DC1")
					.setGeoAddr("(40.803692,-73.402157)").setSourceUrl("http://www.movies.com")
					.setLocation("New York, NY").setName("Cassablanca").setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2))
					// This attaches the event to the activity.
					.setParentTrackId(activity.getTrackingId())
					// Attach the event's properties
					.addProperty(propertyGenre, propertyPrice, propertyName, propertyTime)
					// Attach the event's snapshots
					.addSnapshot(snapshotTemp, snapshotHumidity, snapshotSeaLevel);

			// Stream the event (token is the token that was assigned to you
			// when you purchased jKool.
			Response response = jkSend.post(event);
			response.close();

			// **************************************************************************************
			// And continue creating events for all of the movies playing in the
			// third week of August.
			// **************************************************************************************

			// ......

			// Stream the activity.
			// (token is the token that was assigned to you when you purchased
			// jKool.
			response = jkSend.post(activity);
			response.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
