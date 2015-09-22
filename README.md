# jKoolRestClients

###Why jKool Rest Clients?
This jKool Java Rest Client contains Java helper classes that will help you to get up and running very quickly with the jKool Streaming API. You will need a streaming  “token” in order to stream. This token will be associated with a repository that will be assigned to you when you sign-up for jKool.  The token is passed in the request header. We will be providing additional clients to use with other programming languages. Please check back. If you need them soon, they can be automatically generated with the Swagger Code Generator using the Swagger yaml file found it the "swagger" directory.  

###How to Stream data into jKool
There are four types of data that can be streamed into jKool. They are:
* Events: The main piece of data. Events contain pre-defined jKool fields and can also contain your own custom fields (as snapshots and properties, explained below)
* Activities: A way of organizing events into groupings (categories)
* Snapshots: A way of organizing custom fields into groupings (categories)
* Properties: Your custom fields.

###Field level documentation
This repository contains a Swagger yaml file. Open this file in a Swagger Editor and you will have detailed documentation of each field that comprises the above mentioned data.

###Important note
This helper code is extremely simple.  Please be advised that jKool will handle the most simple of use cases to the most complex use cases. For example, it is built with the ability to correlate events and track transactions among multiple applications.  This can be used for complex system analysis, for instance - to monitor system performance. For details on complex streaming, please see our full documentation at https://www.jkoolcloud.com/wiki/index.php/Main_Page.

###Using this helper code
To use this helper code please do the following:
* Create a Client object. This helper code is using RestEasy to do this. For example, please see the following in RunApi.java:
```java
			String basePath = "http://data.jkoolcloud.com:6580/jKool/JKool_Service/rest";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
```
* Import the provided jKool objects residing in the "model" directory into your code.
* Populate these jKool objects with your data. For example, please see the following in RunApi.java:
```java
			Event event = new Event();
			event.setCompCode("SUCCESS");
			String eventUuid = UUID.randomUUID().toString();
			event.setTrackingId(eventUuid);
			event.setSourceFqn("APPL=WebOrders#SERVER=WebServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=New York, NY");
			event.setSourceUrl("http://www.wunderground.com");
			event.setSeverity("SUCCESS");
			event.setReasonCode(0);
			event.setLocation("New York, NY");
			event.setEventName("August 31 Weather");
			event.setUser("testuser");
			event.setTimeUsec(todaysDate);
			event.setStartTimeUsec(todaysDate);
			event.setEndTimeUsec(todaysDate);
			...
```
* Invoke the post request on the client object sending the objects over as request entities.  For example, please see the following in RunApi.java:
```java
			response = target.path("event").request().header("token", "cathystoken").post(Entity.entity(serialize(event), "application/json"));
			response.close();	
```

That's it!! Any problems or concerns, please email us at support@jkoolcloud.com.




