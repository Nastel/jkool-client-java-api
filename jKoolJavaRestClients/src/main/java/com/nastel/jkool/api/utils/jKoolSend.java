package com.nastel.jkool.api.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.nastel.jkool.api.model.Activity;
import com.nastel.jkool.api.model.Event;

public class jKoolSend {
	
	public static String basePath = "https://data.jkoolcloud.com/JESL";
	public static Client client = ClientBuilder.newClient();
	public static WebTarget target = client.target(basePath);
	
	public static Response post(Event event, String token) {
		try
		{
			return target.path("event").request().header("token", token).post(Entity.entity(serialize(event), "application/json"));
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
			return null;
		}
	}
	
	public static Response post(Activity activity, String token) {
		try
		{
			return target.path("activity").request().header("token", token).post(Entity.entity(serialize(activity), "application/json"));
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
			return null;
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
