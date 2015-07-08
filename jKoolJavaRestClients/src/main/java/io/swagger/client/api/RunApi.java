package io.swagger.client.api;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.EventActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.ClientResponse;

public class RunApi {
	
	
	
	
	public static void main(String[] args) {
		try
		{
			
			Builder builder;
			Client client = Client.create();
			ClientResponse response = null;
			String basePath = "http://11.0.0.40:6580/jKool/JKool_Service/rest";
			
			
			EventActivity event = new EventActivity();
			event.setCompCode("SUCCESS");
			//event.setCompCodeNo(0);
			event.setIdCount("3");
			event.setTrackingId("3175921a-1107-11e3-b8b0-600292390f04");
			event.setPid(5455); 
			event.setTid(3);
			event.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			event.setSourceInfo("Cathy3's source");
			event.setSourceUrl("https://www.sample.com/orders/parts");
			event.setSeverity("SUCCESS");
			//event.setSeverityNo(4);
			event.setType("EVENT");
			//event.setTypeNo(4);
			event.setReasonCode(0);
			event.setLocation("New York, NY");
			event.setOperation("ReceiveOrder");
			event.setUser("Cathy111");
			String todaysDate = (new Date()).getTime() + "000";
			event.setTimeUsec(todaysDate);
			event.setStartTimeUsec(todaysDate);
			event.setEndTimeUsec(todaysDate);
			event.setElapsedTimeUsec(593);
			event.setSnapCount(0);
			event.setMsgText("AMAZON ProductId=28372373, Title: Crash Proof: Author: Robert Prechter Jr.");
			event.setMsgSize(74);
			event.setCharset("windows-1252");
			event.setEncoding("none");
			event.setResource("order/parts");
			event.setMsgMimeType("text/plain");
			event.setCorrId("OrderId:123@1434115730580807@1");
			event.setMsgTag("CathysMsg");
			event.setException("CathysException");
			event.setWaitTimeUsec("CathysWait");
			event.setMsgAge(9999);
			event.setSource("CathysSource");
			event.setParentTrackId("3175921a-1107-11e3-b8b0-600292390999");
			
			//Map<String, String> queryParams = new HashMap<String, String>();
			//Map<String, String> headerParams = new HashMap<String, String>();
			//Map<String, String> formParams = new HashMap<String, String>();
		
			//headerParams.put("token", "2xLE44s5NICfXhVqNhzrkRQrb46tyHhM");
			
			//ApiClient apiClient = new ApiClient();
			//apiClient.invokeAPI(apiClient.getBasePath(), "POST", queryParams, event, headerParams, null, "application/json",  "application/json",  null);
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", "2xLE44s5NICfXhVqNhzrkRQrb46tyHhM");
			response = builder.type("application/json").post(ClientResponse.class, serialize(event));
			
			EventActivity activity = new EventActivity();
			activity.setCompCode("SUCCESS");
			//event.setCompCodeNo(0);
			activity.setIdCount("3");
			activity.setTrackingId("3175921a-1107-11e3-b8b0-600292390999");
			activity.setPid(5455); 
			activity.setTid(3);
			activity.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			activity.setSourceInfo("Cathy3's source");
			activity.setSourceUrl("https://www.sample.com/orders/parts");
			activity.setSeverity("SUCCESS");
			//activity.setSeverityNo(4);
			activity.setType("ACTIVITY");
			//activity.setTypeNo(4);
			//activity.setParentTrackId("12345");
			activity.setReasonCode(0);
			activity.setLocation("New York, NY");
			activity.setOperation("ReceiveOrderActivity");
			activity.setUser("Cathy111");
			activity.setTimeUsec(todaysDate);
			activity.setStartTimeUsec(todaysDate);
			activity.setEndTimeUsec(todaysDate);
			activity.setElapsedTimeUsec(593);
			activity.setSnapCount(0);
			activity.setCharset("windows-1252");
			activity.setEncoding("none");
			activity.setResource("order/parts");
			activity.setCorrId("OrderId:123@1434115730580999@1");
			activity.setException("CathysException999");
			activity.setWaitTimeUsec("CathysWait");
			activity.setStatus("END");
			activity.setSource("CathysSource");
			
			//queryParams = new HashMap<String, String>();
			//headerParams = new HashMap<String, String>();
			//formParams = new HashMap<String, String>();
					
			//apiClient.invokeAPI(apiClient.getBasePath(), "POST", queryParams, activity, headerParams, null, "application/json",  "application/json",  null);
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", "2xLE44s5NICfXhVqNhzrkRQrb46tyHhM");
			response = builder.type("application/json").post(ClientResponse.class, serialize(activity));
			
			
			
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
