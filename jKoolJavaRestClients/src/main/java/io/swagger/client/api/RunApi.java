package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.EventActivity;
import io.swagger.client.model.Property;
import io.swagger.client.model.Snapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class RunApi {
	
	
	
	
	public static void main(String[] args) {
		try
		{
			
			Builder builder;
			Client client = Client.create();
			ClientResponse response = null;
			String basePath = "http://11.0.0.40:6580/jKool/JKool_Service/rest";
			String todaysDate = (new Date()).getTime() + "000";
			
			Property property1 = new Property();
			property1.setName("property1");
			property1.setType("String");
			property1.setValue("value1");
			
			Property property2 = new Property();
			property2.setName("property2");
			property2.setType("String");
			property2.setValue("value2");
			
			HashMap<String, Property> properties = new HashMap<String,Property>();
			properties.put("property1",property1);
			properties.put("property2",property2);
			
			// Post a snapshot
			Snapshot snapshot = new Snapshot();
			snapshot.setCategory("TestCategory");
			snapshot.setCount(2);
			snapshot.setFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			snapshot.setName("TestSnapshot");
			snapshot.setParentId("3175921a-1107-11e3-b8b0-600292390f04");
			snapshot.setSeverity("SUCCESS");
			snapshot.setSourcdFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			snapshot.setSource("TestSource");
			snapshot.setSourceInfo("TestSourceInfo");
			snapshot.setSourceUrl("TestUrl");
			snapshot.setTimeUsec(todaysDate);
			String snapshotUuid = UUID.randomUUID().toString();
			snapshot.setTrackId(snapshotUuid);
			snapshot.setType("SNAPSHOT");
			snapshot.setProperties(properties);
			
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", "a7b89207-6669-49e4-b05a-2b7eed3173ec");
			response = builder.type("application/json").post(ClientResponse.class, serialize(snapshot));
			
			// Post an Event
			EventActivity event = new EventActivity();
			event.setCompCode("SUCCESS");
			event.setIdCount("3");
			event.setTrackingId("3175921a-1107-11e3-b8b0-600292390f04");
			event.setPid(5455); 
			event.setTid(3);
			event.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			event.setSourceInfo("Cathy3's source");
			event.setSourceUrl("https://www.sample.com/orders/parts");
			event.setSeverity("SUCCESS");
			event.setType("EVENT");
			event.setReasonCode(0);
			event.setLocation("New York, NY");
			event.setOperation("ReceiveOrder");
			event.setUser("Cathy111");
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
			event.setMsgTag("TestMsg");
			event.setException("TestException");
			event.setWaitTimeUsec("TestWait");
			event.setMsgAge(9999);
			event.setSource("TestSource");
			event.setParentTrackId("3175921a-1107-11e3-b8b0-600292390999");
			List<Snapshot> snapshots = new ArrayList<Snapshot>();
			snapshots.add(snapshot);
			
		
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", "a7b89207-6669-49e4-b05a-2b7eed3173ec");
			response = builder.type("application/json").post(ClientResponse.class, serialize(event));
			
			// Post an activity
			EventActivity activity = new EventActivity();
			activity.setCompCode("SUCCESS");
			activity.setIdCount("3");
			activity.setTrackingId("3175921a-1107-11e3-b8b0-600292390999");
			activity.setPid(5455); 
			activity.setTid(3);
			activity.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			activity.setSourceInfo("Cathy3's source");
			activity.setSourceUrl("https://www.sample.com/orders/parts");
			activity.setSeverity("SUCCESS");
			activity.setType("ACTIVITY");
			activity.setReasonCode(0);
			activity.setLocation("New York, NY");
			activity.setOperation("ReceiveOrderActivity");
			activity.setUser("TestUser");
			activity.setTimeUsec(todaysDate);
			activity.setStartTimeUsec(todaysDate);
			activity.setEndTimeUsec(todaysDate);
			activity.setElapsedTimeUsec(593);
			activity.setSnapCount(0);
			activity.setCharset("windows-1252");
			activity.setEncoding("none");
			activity.setResource("order/parts");
			activity.setCorrId("OrderId:123@1434115730580999@1");
			activity.setException("TestException");
			activity.setWaitTimeUsec("TestWait");
			activity.setStatus("END");
			activity.setSource("TestSource");
	
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", "a7b89207-6669-49e4-b05a-2b7eed3173ec");
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
