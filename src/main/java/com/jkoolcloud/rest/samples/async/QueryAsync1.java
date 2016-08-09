package com.jkoolcloud.rest.samples.async;

import com.jkoolcloud.rest.api.service.JKQueryAsync;

public class QueryAsync1 {
	 public static void main(String[] args) {
	        try 
	        {
	        	 JKQueryAsync jkQueryAsync = new JKQueryAsync("your_access_token");
	        	 jkQueryAsync.connect();
	        	 MyJKResultCallback myJKResultCallback = new MyJKResultCallback();	        	 
	        	 jkQueryAsync.call("get events", myJKResultCallback, "5");
	        	 //jkQueryAsync.close();
	        	 Thread.sleep(15000000);
	        } 
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        }
	    }
}
