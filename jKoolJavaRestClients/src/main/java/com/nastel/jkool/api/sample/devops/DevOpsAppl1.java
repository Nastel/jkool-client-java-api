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

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.nastel.jkool.api.model.CompCodes;
import com.nastel.jkool.api.model.Event;
import com.nastel.jkool.api.model.EventTypes;
import com.nastel.jkool.api.model.Severities;
import com.nastel.jkool.api.utils.ApiException;
import com.nastel.jkool.api.utils.JsonUtil;

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

			// Setup connection to jKool
			String basePath = "http://data.jkoolcloud.com:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
			Response response = null;

			// Create the first event which is a message received event representing a message received in a hypothetical 
			// messaging queue residing in New York.
			Event event = new Event("05d537d1-6dd1-11e5-9767-600292390111", 
					                                       // trackingId
	                "https://www.sample.com/orders/parts", // sourceUrl
	                Severities.SUCCESS,                    // severity
	                EventTypes.RECEIVE,                    // type
	                5432,                                  // pid (process id)
	                4,                                     // tid (thread id)
	                CompCodes.SUCCESS,                     // compCode (completion code)
	                0,                                     // reasonCode
	                "New York, NY",                        // location
	                "webuser",                             // user
	                "11-Aug-2015 01:15:00",                // timeUsec 
	                "11-Aug-2015 01:15:00",                // startTimeUsec
	                "11-Aug-2015 01:15:00",                // endTimeUsec
	                460,                                   // elapsedTimeUsec
	                "ProductId=28372373 Title: Crash Proof: Author: Robert Prechter Jr.", 
	                                                       // msgText
	                74,                                    // msgSize
	                "none",                                // msgEncoding
	                "windows-1252",                        // msgCharset
	                Arrays.asList("CorrId:123"),      
	                                                       // Correlator Id.
	                "order/parts",                         // resource
	                "text/plain",                          //msgMimeType
	                0,                                     // msgAge
	                null,                                  // exception
	                null,                                  // msgTag
	                null,                                  // parentTrackId (jKool will compute this)
	                0,                                     // waitTimeUsec
	                "ReceiveOrder",                        // eventName
	                null,                                  // snapshots (none in this example)
	    			"WebOrders",						   // appl name (comprises source-fqn)
	    			"WebServerNY",	                       // server (comprises source-fqn)
	    			"11.0.0.2",                            // network address (comprises source-fqn)
	    			"DCNY",								   // data center (comprises source-fqn)
	    			"New York, NY");                       // geo location (comprises source-fqn)
		
			// Stream the event 
			// (token is the token that was assigned to you when you purchased jKool).
			response = target.path("event").request().header("token", "yourtoken").post(Entity.entity(serialize(event), "application/json"));
			response.close();	
			
			// Create the next event which is a message sent event representing a message sent from a hypothetical 
			// messaging queue residing in New York to a hypothetical messaging queue residing in Los Angeles (DevOpsAppl2 class)
			event = new Event("05d537d2-6dd1-11e5-9767-600292390f02", 
					                                        // trackingId
	                                                        // sourceFqn
	                "https://www.sample.com/orders/parts",  // sourceUrl
	                Severities.SUCCESS,                     // severity
	                EventTypes.SEND,                        // type
	                5432,                                   // pid (process id)
	                4,                                      // tid (thread id)
	                CompCodes.SUCCESS,                      // compCode (completion code)
	                0,                                      // reasonCode
	                "New York, NY",                         // location
	                "webuser",                              // user
	                "11-Aug-2015 01:15:00",                 // timeUsec 
	                "11-Aug-2015 01:15:00",                 // startTimeUsec
	                "11-Aug-2015 01:15:00",                 // endTimeUsec
	                823,                                    // elapsedTimeUsec
	                "Order Processed ProductId=28372373",   // msgText
	                73,                                     // msgSize
	                "none",                                 // msgEncoding
	                "windows-1252",                         // msgCharset
	                Arrays.asList("CorrId:123"),            // Correlator Id.
	                "PROCESS.ORDERS.QUEUE",                 // resource
	                "text/plain",                           // msgMimeType
	                0,                                      // msgAge
	                null,                                   // exception
	                null,                                   // msgTag
	                null,                                   // parentTrackId (jKool will compute this)
	                0,                                      // waitTimeUsec
	                "ProcessOrder",                         // eventName
	                null,                                   // snapshots (none in this example)
	    			"WebOrders",						   // appl name (comprises source-fqn)
	    			"WebServerNY",	                       // server (comprises source-fqn)
	    			"11.0.0.2",                            // network address (comprises source-fqn)
	    			"DCNY",								   // data center (comprises source-fqn)
	    			"New York, NY");                       // geo location (comprises source-fqn)
	

					// Stream the event 
			        // (token is the token that was assigned to you when you purchased jKool.
					response = target.path("event").request().header("token", "yourtoken").post(Entity.entity(serialize(event), "application/json"));
					response.close();
			
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
