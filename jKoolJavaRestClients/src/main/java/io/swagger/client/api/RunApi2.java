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
import java.util.Map;
import java.util.UUID;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class RunApi2 {
	
	public static void main(String[] args) {
		try
		{
			Builder builder;
			Client client = Client.create();
			ClientResponse response = null;
			String basePath = "http://11.0.0.40:6580/jKool/JKool_Service/rest";
			String todaysDate = (new Date()).getTime() + "000";
			
			// Post the activity
			EventActivity activity = new EventActivity();
			String activityUuid = UUID.randomUUID().toString();
			activity.setTrackingId(activityUuid);
			activity.setEventName("August Week 2 Weather");
			activity.setTimeUsec(todaysDate);
			activity.setStatus("END");
			activity.setType("ACTIVITY");
			activity.setEvents(null);
			activity.setSnapshots(null);
			activity.setSnapCount(0);
			activity.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
	
	
			builder = client.resource(basePath).accept("application/json");
			// This is the token that was assigned to you when you purchased jKool.
			builder = builder.header("token", "cathystoken ");
			response = builder.type("application/json").post(ClientResponse.class, serialize(activity));
			
			// Create some custom fields
			Property propertyTempHigh = new Property();
			propertyTempHigh.setName("TempHigh");
			propertyTempHigh.setType("String");
			propertyTempHigh.setValue("95");
			
			Property propertyTempLow = new Property();
			propertyTempLow.setName("TempLow");
			propertyTempLow.setType("String");
			propertyTempLow.setValue("83");
			
			List<Property> propertiesTemp = new ArrayList<Property>();
			propertiesTemp.add(propertyTempHigh);
			propertiesTemp.add(propertyTempLow);
			
			Property propertyHumidityMax = new Property();
			propertyHumidityMax.setName("HumidityMax");
			propertyHumidityMax.setType("String");
			propertyHumidityMax.setValue("95");
			
			Property propertyHumidityMin = new Property();
			propertyHumidityMin.setName("HumidityMin");
			propertyHumidityMin.setType("String");
			propertyHumidityMin.setValue("74");
			
			List<Property> propertiesHumidity = new ArrayList<Property>();
			propertiesHumidity.add(propertyHumidityMax);
			propertiesHumidity.add(propertyHumidityMin);
			
			Property propertySeaLevelMax = new Property();
			propertySeaLevelMax.setName("SeaLevelMax");
			propertySeaLevelMax.setType("String");
			propertySeaLevelMax.setValue("31");
			
			Property propertySeaLevelMin = new Property();
			propertySeaLevelMin.setName("SealevelMin");
			propertySeaLevelMin.setType("String");
			propertySeaLevelMin.setValue("29");
			
			List<Property> propertiesSeaLevel = new ArrayList<Property>();
			propertiesSeaLevel.add(propertySeaLevelMax);
			propertiesSeaLevel.add(propertySeaLevelMin);
			
			// Attach the custom fields to snapshots 
			Snapshot snapshotTemp = new Snapshot();
			snapshotTemp.setCategory("Land");
			snapshotTemp.setCount(2);
			snapshotTemp.setName("Temperature");
			snapshotTemp.setType("SNAPSHOT");
			snapshotTemp.setTimeUsec(todaysDate);
			snapshotTemp.setTrackId(UUID.randomUUID().toString());
			snapshotTemp.setParentId("3175921a-1107-11e3-b8b0-600292390f04");
			snapshotTemp.setProperties(propertiesTemp);
			
			Snapshot snapshotHumidity = new Snapshot();
			snapshotHumidity.setCategory("Land");
			snapshotHumidity.setCount(2);
			snapshotHumidity.setName("Humidity");
			snapshotHumidity.setType("SNAPSHOT");
			snapshotHumidity.setTimeUsec(todaysDate);
			snapshotHumidity.setTrackId(UUID.randomUUID().toString());
			snapshotHumidity.setParentId("3175921a-1107-11e3-b8b0-600292390f04");
			snapshotHumidity.setProperties(propertiesHumidity);
			
			Snapshot snapshotSeaLevel = new Snapshot();
			snapshotSeaLevel.setCategory("Sea");
			snapshotSeaLevel.setCount(2);
			snapshotSeaLevel.setName("SeaLevel");
			snapshotSeaLevel.setType("SNAPSHOT");
			snapshotSeaLevel.setTimeUsec(todaysDate);
			snapshotSeaLevel.setTrackId(UUID.randomUUID().toString());
			snapshotSeaLevel.setParentId("3175921a-1107-11e3-b8b0-600292390f04");
			snapshotSeaLevel.setProperties(propertiesSeaLevel);

			// Create the Event
			// Attach it's snapshots
			// Attach the event to its parent activity 
			EventActivity event = new EventActivity();
			event.setCompCode("SUCCESS");
			event.setTrackingId("3175921a-1107-11e3-b8b0-600292390f04");
			event.setPid(5455); 
			event.setTid(3);
			event.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			event.setSourceInfo("Weather Website");
			event.setSourceUrl("http://www.wunderground.com");
			event.setSeverity("SUCCESS");
			event.setType("EVENT");
			event.setReasonCode(0);
			event.setLocation("New York, NY");
			event.setEventName("August 9 Weather");
			event.setUser("jsmith");
			event.setTimeUsec(todaysDate);
			event.setStartTimeUsec(todaysDate);
			event.setEndTimeUsec(todaysDate);
			event.setElapsedTimeUsec(0);
			event.setSnapCount(1);
			event.setMsgText("This event contains weather information for August 9th.");
			event.setMsgSize(74);
			event.setCharset("windows-1252");
			event.setEncoding("none");
			event.setResource("order/parts");
			event.setMsgMimeType("text/plain");
			event.setCorrId("OrderId:123@1434115730580807@1");
			event.setMsgTag("TestMsg");
			event.setException("None");
			event.setWaitTimeUsec("TestWait");
			event.setMsgAge(0);
			// This attaches the event to the activity.
			event.setParentTrackId(activityUuid); 
			// This attaches its snapshots
			List<Snapshot> snapshots = new ArrayList<Snapshot>(); 
			snapshots.add(snapshotTemp);
			snapshots.add(snapshotHumidity);
			snapshots.add(snapshotSeaLevel);
			event.setSnapshots(snapshots);
		
			// Post the event.
			builder = client.resource(basePath).accept("application/json");
			builder = builder.header("token", "cathystoken");
			response = builder.type("application/json").post(ClientResponse.class, serialize(event));
			
			// **********************************************************************************
			// And continue to do the same for all of the days in the second week of August!! :)
			// **********************************************************************************
			
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
