package com.jkoolcloud.rest.api.jkool_java_rest.sample.devops;

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
import java.util.Arrays;

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.jkool_java_rest.model.CompCodes;
import com.jkoolcloud.rest.api.jkool_java_rest.model.Event;
import com.jkoolcloud.rest.api.jkool_java_rest.model.EventTypes;
import com.jkoolcloud.rest.api.jkool_java_rest.model.Severities;
import com.jkoolcloud.rest.api.jkool_java_rest.utils.ApiException;
import com.jkoolcloud.rest.api.jkool_java_rest.utils.JsonUtil;
import com.jkoolcloud.rest.api.jkool_java_rest.utils.jKoolSend;

/**************************************************************************************************************************
 * In this example, we will demonstrate a DevOps use of jKool. This example will demonstrate two advanced aspects
 * of jKool:
 * 1) How to make use of the many pre-defined fields jKool uses to store DevOps data.
 * 2) Correlating events - in this example, instead of grouping events via the activity, we will rely on jKool to deduce
 *    associations via the correlator id's.
 *
 * In this example we are portraying three messaging queues residing in three different data center locations throughout the United States.
 * As messages are passed from one data center to the next data center, associations within the data are maintained via the
 * "correlator id's".
 *
 * Although this example does not demonstrate it, properties and/or snapshots could be added.
 * WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION VARIABLES INSTEAD OF HARDCODED VALUES.
 * ***********************************************************************************************************************/


public class DevOpsAppl1 {

	public static void main(String[] args) {
		try
		{

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			jKoolSend jkSend = new jKoolSend("0bb480b6-582a-42e7-aeb0-3bd9ee40f4ee");

			// Create the first event which is a message received event representing a message received in a hypothetical
			// messaging queue residing in New York.
			Event event = new Event().setSourceUrl("https://www.sample.com/orders/parts")
					.setSeverity(Severities.INFO)
					.setType(EventTypes.RECEIVE)
					.setPid(5432)
					.setTid(4)
					.setCompCode(CompCodes.SUCCESS)
					.setReasonCode(0)
					.setLocation("New York, NY")
					.setUser("webuser")
					.setStartTimeUsec(formatter.parse("11-Aug-2016 01:15:00"))
					.setEndTimeUsec(formatter.parse("11-Aug-2016 01:15:30"))
					.setElapsedTimeUsec(30)
					.setMsgText("OrderId=28372373 received.")
					.setMsgEncoding("none")
					.setMsgCharset("windows-1252")
					.setCorrId(Arrays.asList("CorrId:123"))
					.setResource("ORDERS.QUEUE")
					.setMsgMimeType("text/plain")
					.setMsgAgeUsec(0)
					.setException(null)                                
	                .setMsgTag(null)
	                .setParentTrackId(null)
	                .setWaitTimeUsec(0)
	                .setEventName("ReceiveOrder")
	                .setSnapshots(null)
	                .setAppl("WebOrders")
	                .setServer("WebServerNY")
	                .setNetAddr("172.16.257.34")
	                .setDataCenter("DCNY")
	                .setGeoAddr("40.803692,-73.402157");

			// Stream the event
			// (token is the token that was assigned to you when you purchased jKool).
			Response response = jkSend.post(event);
			response.close();


			// Create the next event which is a message sent event representing a message sent from a hypothetical
			// messaging queue residing in New York to a hypothetical messaging queue residing in Los Angeles (DevOpsAppl2 class)
			event = new Event()
			        .setSourceUrl("https://www.sample.com/orders/parts")
			        .setSeverity(Severities.INFO)
			        .setType(EventTypes.SEND)
			        .setPid(5432)
			        .setTid(4)
			        .setCompCode(CompCodes.SUCCESS)
			        .setReasonCode(0)
			        .setLocation("New York, NY")
			        .setUser("webuser")
			        .setStartTimeUsec(formatter.parse("11-Aug-2016 01:15:30"))
			        .setEndTimeUsec(formatter.parse("11-Aug-2016 01:15:30"))
			        .setElapsedTimeUsec(0)
			        .setMsgText("Order Processed ProductId=28372373")
			        .setMsgEncoding("none")
			        .setMsgCharset("windows-1252")
			        .setCorrId(Arrays.asList("CorrId:123"))
			        .setResource("PAYMENT.QUEUE")
			        .setMsgMimeType("text/plain")
			        .setMsgAgeUsec(0) 
			        .setException(null)
			        .setMsgTag(null)
			        .setParentTrackId(null)
			        .setWaitTimeUsec(0)
			        .setEventName("ProcessOrder")
			        .setSnapshots(null)
			        .setAppl("WebOrders")
			        .setServer("WebServerNY")
			        .setNetAddr("172.16.257.34")
			        .setDataCenter("DCNY")
			        .setGeoAddr("40.803692,-73.402157");                       


					// Stream the event
			        // (token is the token that was assigned to you when you purchased jKool.
					response = jkSend.post(event);
					response.close();


		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}


}
