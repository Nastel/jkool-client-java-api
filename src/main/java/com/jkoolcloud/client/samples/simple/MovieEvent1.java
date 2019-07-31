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
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import com.jkoolcloud.client.api.model.Event;
import com.jkoolcloud.client.api.service.JKStream;

/**************************************************************************************************************************
 * This example demonstrates how to create a simple movie events
 * 
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 ***********************************************************************************************************************/

public class MovieEvent1 {

	public static void main(String[] args) {
		try {
			JKStream jkSend = new JKStream(System.getProperty("jk.access.token", "access-token"));

			// Create the Event
			// Attach it's properties
			Event event = new Event();
			event.setMsgText("Casablanca is playing.").setAppl("WebOrders")
					.setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
					.setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2)).setSourceUrl("http://www.movies.com")
					.setLocation("New York, NY").setName("Casablanca");

			Response response = jkSend.post(event);
			response.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
