package com.jkoolcloud.rest.samples.async;

import java.net.URI;
import java.net.URISyntaxException;

import com.jkoolcloud.rest.api.service.WebsocketClient;

public class QueryAsync {
	 public static void main(String[] args) {
	        try {
	            final WebsocketClient clientEndPoint = new WebsocketClient(new URI("ws://11.0.0.40:8080/jKool/jkqlasync"));

	            clientEndPoint.addMessageHandler(new WebsocketClient.MessageHandler() {
	                public void handleMessage(String message) {
	                    System.out.println(message);
	                }
	            });
	            
	            clientEndPoint.sendMessage("{\"query\":\"get events\", \"maxResultRows\":\"5\", \"username\":\"test3\",\"password\":\"pwtest3\",\"repositoryId\":\"R_test385529$O_test3\"}");
	            Thread.sleep(5000);

	        } catch (URISyntaxException ex) {
	            System.err.println("URISyntaxException exception: " + ex.getMessage());
	        }
	        catch (Exception e)
	        {
	        	
	        }
	    }
}
