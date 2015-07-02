package io.swagger.client.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.swagger.client.ApiClient;
import io.swagger.client.model.EventActivity;

public class RunApi {
	
	public static void main(String[] args) {
		try
		{
			EventActivity event = new EventActivity();
			event.setCompCode("SUCCESS");
			event.setCompCodeNo(0);
			event.setElapsedTimeUsec(500);
			event.setIdCount("3");
			event.setTrackingId("3175921a-1107-11e3-b8b0-600292390f03");
			event.setPid(5455); 
			event.setTid(3);
			event.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			event.setSourceInfo("Cathy3's source");
			event.setSourceUrl("https://www.sample.com/orders/parts");
			event.setSeverity("SUCCESS");
			event.setSeverityNo(4);
			event.setType("RECEIVE");
			event.setTypeNo(4);
			event.setReasonCode(0);
			event.setLocation("New York, NY");
			event.setOperation("ReceiveOrder");
			event.setUser("Cathy3");
			String todaysDate = (new Date()).getTime() + "000";
			event.setTimeUsec(todaysDate);
			event.setStartTimeUsec(todaysDate);
			event.setEndTimeUsec(todaysDate);
			event.setElapsedTimeUsec(593);
			event.setSnapCount(0);
			event.setMsg("AMAZON ProductId=28372373, Title: Crash Proof: Author: Robert Prechter Jr.");
			event.setMsgCount(74);
			event.setCharset("windows-1252");
			event.setEncoding("none");
			event.setResource("order/parts");
			event.setMimeType("text/plain");
			event.setCorrId("OrderId:123@1434115730580806@1");
			
			Map<String, String> queryParams = new HashMap<String, String>();
			Map<String, String> headerParams = new HashMap<String, String>();
			Map<String, String> formParams = new HashMap<String, String>();
			
			headerParams.put("token", "abc12345");
			
			ApiClient apiClient = new ApiClient();
			apiClient.invokeAPI(apiClient.getBasePath(), "POST", queryParams, event, headerParams, null, "application/json",  "application/json",  null);
		}
		catch (Exception e)
		{
			
		}
	}

}
