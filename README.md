# jKoolRestClients

###Why jKool Rest Clients?
This jKool Java Rest Client contains Java helper classes that will help you to get up and running very quickly with the jKool Streaming API. You will need a streaming  “token” in order to stream. This token will be associated with a repository that will be assigned to you when you sign-up for jKool.  The token is passed in the request header. We will be providing additional clients to use with other programming languages. Please check back. If you need them soon, they can be automatically generated with the Swagger Code Generator using the Swagger yaml file found it the "swagger" directory.  

###How to Stream data into jKool
There are four types of data that can be streamed into jKool. They are:
* Events: The main piece of data. Events contain pre-defined jKool fields and can also contain your own custom fields (as snapshots and properties, explained below)
* Activities: A way of organizing events into groupings.
* Snapshots: A way of representing custom data at a "point in time".
* Properties: Your custom data.

(Very detailed Data Model Documentation is soon to come)

###Field level documentation
This repository contains a Swagger yaml file. Open this file in a Swagger Editor and you will have detailed documentation of each field that comprises the above mentioned data.

###Important note
This helper code is extremely simple.  Please be advised that jKool will handle the most simple of use cases to the most complex use cases. For example, it is built with the ability to correlate events and track transactions among multiple applications.  This can be used for complex system analysis, for instance - to monitor system performance. We will be providing user documentation shortly that will explain in detail how to take advantage of complex jKool streaming and analysis.

###Using this helper code
To use this helper code please do the following:
* Import the required libraries residing inthe "libs" directory into your code.
* Create a Client object. This helper code is using RestEasy to do this. For example, please see the following in any of the sample code:
```java
			String basePath = "http://data.jkoolcloud.com:6580/JESL";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(basePath);
```
* Import the provided jKool objects residing in the "model" directory into your code.
* Populate these jKool objects with your data. For example, please see the following in the sample code:
```java
			Event event = new Event();
			event.setCompCode("SUCCESS");
			String eventUuid = UUID.randomUUID().toString();
			event.setTrackingId(eventUuid);
			event.setServer("WebServer100");
			event.setNetAddr("11.0.0.2");
			event.setDataCenter("DCNY");
			event.setGeoAddr("New York, NY");
			event.setSourceUrl("http://www.movies.com");
			event.setLocation("New York, NY");
			event.setEventName("Casablanca 8/3 at 1PM");
			event.setTimeUsec(movieDate);
			...
```
* Invoke the post request on the client object sending the objects over as request entities. As part of this request, put your token in the header.  For example, please see the following in any of the sample code. 
```java
			response = target.path("event").request().header("token", "yourtoken").post(Entity.entity(serialize(event), "application/json"));
			response.close();	
```

The Rest Client will properly format the entity into JSON format.

That's it!! Any problems or concerns, please email us at support@jkoolcloud.com.


###Streaming with Curl
Data can also be streamed into jKool using Curl. Below is an example:

```
curl -i -H "Content-Type:application/json" -H "token:YOURTOKEN" -X POST https://test.jkoolcloud.com:6585/JESL/event -d '{"operation":"streamingwithcurl","type":"EVENT","start-time-usec":1457524800000000,"end-time-usec":1457524800000000,"msg-text":"Testing Curl","source-fqn":"APPL=TestingCurl#SERVER=CurlServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=52.52437,13.41053"}'
```java

