package com.jkoolcloud.rest.samples.messaging;

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
import java.util.Arrays;

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.model.CompCodes;
import com.jkoolcloud.rest.api.model.Event;
import com.jkoolcloud.rest.api.model.EventTypes;
import com.jkoolcloud.rest.api.model.Severities;
import com.jkoolcloud.rest.api.service.JKStream;

/**************************************************************************************************************************
 * In this example, we will demonstrate a Rest API usage. This example will
 * demonstrate two advanced aspects of jKool: 1) How to make use of the many
 * pre-defined fields jKool uses to store DevOps data. 2) Correlating events -
 * in this example, instead of grouping events via the activity, we will rely on
 * jKool to deduce associations via the correlator id's.
 *
 * In this example we are portraying three messaging queues residing in three
 * different data center locations throughout the United States. As messages are
 * passed from one data center to the next data center, associations within the
 * data are maintained via the "correlator id's".
 *
 * Although this example does not demonstrate it, properties and/or snapshots
 * could be added. WHEN USING THIS API IN REAL CODE, YOU WILL USE APPLICATION
 * VARIABLES INSTEAD OF HARDCODED VALUES.
 ***********************************************************************************************************************/

public class RestSample2 {

	public static void main(String[] args) {
		try {
			JKStream jkSend = new JKStream("your-access-token");

			// Create the first event which is a message received event
			// representing a message received in a
			// hypothetical
			// messaging queue residing in New York.
			Event event = new Event();
			event.setMsgText("OrderId=28372373 payment processed.").setMsgEncoding("none").setMsgMimeType("text/plain")
					.setMsgAgeUsec(0).setMsgCharset("windows-1252").setSourceUrl("https://www.sample.com/orders/parts")
					.setSeverity(Severities.INFO).setType(EventTypes.RECEIVE).setCompCode(CompCodes.SUCCESS)
					.setReasonCode(0).setLocation("Los Angeles, CA").setUser("ebay-proc").setElapsedTimeUsec(45938)
					.setCorrId(Arrays.asList("CorrId:123")).setResource("PAYMENT.QUEUE").setWaitTimeUsec(0)
					.setName("ReceivePayment").setAppl("WebPayments")
					.setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DC1")
					.setGeoAddr("34.05223,-118.24368");

			// Stream the event
			// (token is the token that was assigned to you when you purchased
			// jKool).
			Response response = jkSend.post(event);
			response.close();

			// Create the next event which is a message sent event representing
			// a message sent from a hypothetical
			// messaging queue residing in New York to a hypothetical messaging
			// queue residing in Los Angeles
			// (RestSample2 class)
			event = new Event();
			event.setMsgText("Verify ProductId=28372373").setMsgEncoding("none").setMsgCharset("windows-1252")
					.setMsgMimeType("text/plain").setMsgAgeUsec(0).setSourceUrl("https://www.sample.com/orders/parts")
					.setSeverity(Severities.INFO).setType(EventTypes.SEND).setCompCode(CompCodes.SUCCESS)
					.setReasonCode(0).setLocation("Los Angeles, CA").setUser("webuser").setElapsedTimeUsec(107)
					.setCorrId(Arrays.asList("CorrId:123")).setResource("SHIPPING.QUEUE").setName("ProcessPayment")
					.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
					.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCCA")
					.setGeoAddr("34.05223,-118.24368");

			// Stream the event
			// (token is the token that was assigned to you when you purchased
			// jKool.
			response = jkSend.post(event);
			response.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
