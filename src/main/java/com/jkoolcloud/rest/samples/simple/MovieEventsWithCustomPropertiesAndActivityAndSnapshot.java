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

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.model.Activity;
import com.jkoolcloud.rest.api.model.Event;
import com.jkoolcloud.rest.api.model.Property;
import com.jkoolcloud.rest.api.model.Snapshot;
import com.jkoolcloud.rest.api.utils.jKoolStream;

/**************************************************************************************************************************
 * This example code uses the same data as the prior Movie example code. However it also demonstrates how to make use of
 * Snapshots. In this example, snapshots are being used to capture the weather at the time the movie was playing.
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 * ***********************************************************************************************************************/

public class MovieEventsWithCustomPropertiesAndActivityAndSnapshot {

	public static void main(String[] args) {
		try {
			jKoolStream jkSend = new jKoolStream("your-access-token");

			// Create the activity that the events will be attached to
			Activity activity = new Activity()
			        .setActivityName("August Week 3 Movies")
			        // also referred to as "operation"
			        .setAppl("WebMovies").setServer(InetAddress.getLocalHost().getHostName())
			        .setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
			        .setGeoAddr("40.803692,-73.402157");

			// Create some snapshot custom fields
			Property propertyTempHigh = new Property("TempHigh", "String", "95");
			Property propertyTempLow = new Property("TempLow", "String", "83");

			List<Property> propertiesTemp = new ArrayList<Property>();
			propertiesTemp.add(propertyTempHigh);
			propertiesTemp.add(propertyTempLow);

			Property propertyHumidityMax = new Property("HumidityMax", "String", "95");
			Property propertyHumidityMin = new Property("HumidityMin", "String", "74");

			List<Property> propertiesHumidity = new ArrayList<Property>();
			propertiesHumidity.add(propertyHumidityMax);
			propertiesHumidity.add(propertyHumidityMin);

			Property propertySeaLevelMax = new Property("SeaLevelMax", "String", "31");
			Property propertySeaLevelMin = new Property("SealevelMin", "String", "29");

			List<Property> propertiesSeaLevel = new ArrayList<Property>();
			propertiesSeaLevel.add(propertySeaLevelMax);
			propertiesSeaLevel.add(propertySeaLevelMin);

			// Attach the custom fields to snapshots
			Snapshot snapshotTemp = new Snapshot("Land", "Temperature", propertiesTemp);
			Snapshot snapshotHumidity = new Snapshot("Land", "Humidity", propertiesHumidity);
			Snapshot snapshotSeaLevel = new Snapshot("Sea", "SeaLevel", propertiesSeaLevel);

			// Create some event custom fields
			Property propertyName = new Property();
			propertyName.setName("MovieName");
			propertyName.setType("String");
			propertyName.setValue("Casablanca");

			Property propertyPrice = new Property("MoviePrice", "Double", "10.50");
			Property propertyGenre = new Property("MovieGenre", "String", "Drama");
			Property propertyTime = new Property("MovieTime", "String", "August 3, 2015 at 1PM");

			List<Property> propertiesMovie = new ArrayList<Property>();
			propertiesMovie.add(propertyGenre);
			propertiesMovie.add(propertyPrice);
			propertiesMovie.add(propertyName);
			propertiesMovie.add(propertyTime);

			// Create the Event
			// Attach it's snapshots
			// Attach it's properties
			// Attach the event to its parent activity
			Event event = new Event().setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
			        .setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DC1")
			        .setGeoAddr("(40.803692,-73.402157)").setSourceUrl("http://www.movies.com")
			        .setLocation("New York, NY").setEventName("Cassablanca playing August 3rd at 1PM")
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
