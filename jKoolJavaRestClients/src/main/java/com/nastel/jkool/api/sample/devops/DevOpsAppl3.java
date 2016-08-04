package com.nastel.jkool.api.sample.devops;

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

import com.nastel.jkool.api.model.CompCodes;
import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.EventTypes;
import com.nastel.jkool.api.model.Severities;
import com.nastel.jkool.api.utils.ApiException;
import com.nastel.jkool.api.utils.JsonUtil;
import com.nastel.jkool.api.utils.jKoolSend;

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


public class DevOpsAppl3 {

	public static void main(String[] args) {
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			String token = "yourtoken";

			// Create the first event which is a message received event representing a message received in a hypothetical
			// messaging queue residing in New York.
			Event event = new Event()				                                     
	                      .setSourceUrl("https://www.sample.com/orders/parts")
	                      .setSeverity(Severities.INFO)
	                      .setType(EventTypes.RECEIVE)
	                      .setPid(432)
	                      .setTid(64)
	                      .setCompCode(CompCodes.SUCCESS)
	                      .setReasonCode(0)
	                      .setLocation("Charlotte, NC")
	                      .setUser("system")
	                      .setStartTimeUsec(formatter.parse("11-Aug-2016 01:16:15"))
	                      .setEndTimeUsec(formatter.parse("11-Aug-2016 01:16:30"))
	                      .setElapsedTimeUsec(15)                                 
	                      .setMsgText("OrderId=28372373 shipped.")
	                      .setMsgEncoding("none")
	                      .setMsgCharset("windows-1252")
	                      .setCorrId(Arrays.asList("CorrId:123"))
	                      .setResource("SHIPPING.QUEUE")
	                      .setMsgMimeType("text/plain")
	                      .setMsgAge(0)
	                      .setException(null)
	                      .setMsgTag(null)
	                      .setParentTrackId(null)
	                      .setWaitTimeUsec(0)
	                      .setEventName("ReceiveAndProcessShipping")
	                      .setSnapshots(null)
	                      .setAppl("WebShipping")
	                      .setServer("WebServerNC")
	                      .setNetAddr("172.16.297.11")
	                      .setDataCenter("DCNC")
	                      .setGeoAddr("35.22709,-80.84313");

			// Stream the event
			// (token is the token that was assigned to you when you purchased jKool).
			Response response = jKoolSend.post(event, token);
			response.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}



}
