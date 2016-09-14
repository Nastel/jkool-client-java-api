# jKool Streaming & Query API

###Why jKool Streaming & Query API?
jKool Streaming & Query API allows you to stream events, metrics, transactions as well as execute queries against jKool streams. You will need a streaming  "access token” in order to stream & query your data. This token is associated with a repository assigned to you when you sign-up for jKool. Other language bindings can be generated with the Swagger Code Generator using the Swagger yaml file found it the "swagger" folder. Please be aware the the Swagger yaml file is documenting every field that can be passed via Restful API. When using this Java Helper API, many fields will be defaulted or automatically computed for you. 

###jKool Streaming Concepts and Documentation
You can find very comprehensive documentation on jKool Data Types and Concepts in our [jKool Streaming Guide](https://www.jkoolcloud.com/download/jkool-model.pdf). There are four types of timeseries data types that can be streamed to jKool. They are:
* *Event*: Represents a basic time series element containing time, message, severity and other fields associated with event.
* *Activity*: Represents a group of events and other activities (e.g. transactions).
* *Snapshot*: categorized metrics (name, value, type) at a "point in time". Snapshots can be associated with events, activities.
* *Property*: simple metric (name, value pair). Properties can be associated with events, activities and snapshots. 

This Git repository contains a Swagger yaml file. Open this file in a Swagger Editor and you will have detailed documentation of each field that comprises the above mentioned data.

###Streaming Examples
To use this sample code please do the following:
* Run `mvn install` on the project. This will generate `jkool-client-api-<version>` jar file. This jar file can be found in the target directory. Be advised that when running from the command line (as documented below), run from the `build` directory that Maven will assemble. This `build` directory will be at the same level as the directory you run Maven from. 
* Import `jkool-client-api-<version>` jar file as well as all `lib` depedencies into your own project.
* See samples to get a good understanding on how to use the this client API.
* Instantiate `JKStream` class. You will need to pass it the access token you received when you signed up for jKool. This token will grant you access to stream and also ensure that the data goes to the repository associated with the access token.
```java
	JKStream jkSend = new JKStream("yourtoken");
```
Create an event and populate the fields you wish to stream. For example:
```java
	Event event = new Event("Casablanca");
	event.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
	        .setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
	        .setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2)).setLocation("New York, NY")
	        .setMsgText("Casablanca is playing.");

```
Please note that this example code depicts streaming in real-time. Therefore the start date of the event will default to the current date/time and the end date will default to the start date plus the elapsed time. You can however control start/end dates. For example:
```java
	event.setTime(System.currentTimeMillis()).setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2));
```
Optionally add any user defined defined properties using:
```java
	Property customerName = new Property("Name", "John Smith");
	Property customerAge = new Property("Age", 26, ValueType.VALUE_TYPE_AGE_YEAR);
	event.addProperty(customerName, customerAge);
```
Finally, invoke the post method on the `JKStream` object, passing it the event you wish to stream:
```java
	JKStream jkSend = new JKStream("yourtoken");
	Event event = new Event("Casablanca");
	event.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
		.setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
		.setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2)).setLocation("New York, NY")
		.setMsgText("Casablanca is playing.");
	Property customerName = new Property("Name", "John Smith");
	Property customerAge = new Property("Age", 26, ValueType.VALUE_TYPE_AGE_YEAR);
	event.addProperty(customerName, customerAge);
	Response response = jkSend.post(event);
	response.close();
```
The Client API formats the entity into JSON format and streams it to jKool over default `https` protocol.

###Running jKool Queries (Synchronously)
In addition to streaming, data can also be retrieved from jKool via Rest. To do this, make use of the jKool Query Language (JKQL). Please see [JKQL Documentation](https://www.jkoolcloud.com/download/jKQL%20User%20Guide.pdf). Use the `JKQuery` to run JKQL queries synchronously. Simply pass in your access token along with the JKQL query.  Below is an example:

```java
	JKQuery jkQuery = new JKQuery("yourtoken");
	Response response = jkQuery.call("get number of events for today");
	Map<String, Object> jsonResponse = response.readEntity(Map.class);
	response.close();
```
All returned JKQL responses are JSON.

###Running jKool Queries (Asynchronously)
Developers can also invoke JKQL queries asynchronously using callbacks. To do this, make use of the `JKQueryAsync`. Below is an example. Please note that this example is demonstrating adding a connection handler that will do a trace and a connection handler that will retry the connection every so many milliseconds if it should fail.
```java
	// setup jKool WebSocket connection and connect
	JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
	// retry connection handler
	jkQueryAsync.addConnectionHandler(new JKRetryConnectionHandler(5000, TimeUnit.MILLISECONDS));
	// trace connection handler
	jkQueryAsync.addConnectionHandler(new JKTraceConnectionHandler(System.out, true));
```
The next step is to setup default callback handlers (optional but recommended). Default callback handlers are called for responses not associated with any specific query or subscription. 
```java
	// setup a default response handler for responses not associated with any specific query
	jkQueryAsync.addDefaultCallbackHandler(new JKTraceQueryCallback(System.out, true));
	jkQueryAsync.connect(); // connect stream with WebSocket interface
```
Next execute your query. All response will be delegated to all default callback handlers, because no callback has been associated with this query :
```java
	// run query in async mode without a callback (use default response handlers)
	jkQueryAsync.callAsync("get number of events for today");
	...
	jkQueryAsync.close(); // close connection
```
Alternatively you can execute a query with a specific callback instance. All responses associated with this query will be routed to the callback instance specified in the `jkQueryAsync.callAsync(...)` call.
```java
	// run query in async mode with a specific callback
	JKQueryHandle qhandle = jkQueryAsync.callAsync("get events", new MyJKQueryCallback());
	qhandle.awaitOnDead(10000, TimeUnit.MILLISECONDS); // optional wait 10s for query to finish
	...
	jkQueryAsync.close(); // close connection
```
`MyJKQueryCallback.handle()` is called when for every response to the query -- there maybe one or more responses depending on the query. `MyJKQueryCallback.dead()` is called when the handle will never be called again. This happens when the query is cancelled using `JKQueryAsync.cancelAsync()` call or when all responses associated with a specific query have been delivered.
```java
public class MyJKQueryCallback implements JKQueryCallback {
	@Override
	public void handle(JKQueryHandle qhandle, JsonObject response, Throwable ex) {
		System.out.println("response: handle=" + qhandle + ", response=" + response);
		if (ex != null) {
			System.out.println("error: handle=" + qhandle + ", error=" + ex);
		}	
	}

	@Override
	public void dead(JKQueryHandle qhandle) {
		if (trace) {
			out.println("Dead handle=" + qhandle + ", dead=" + qhandle.isDead());
		}
	}
}
```
`jkQueryAsync.callAsync()` returns a query handler (instance of `JKQueryHandle`), which can be used later to cancel subscriptions.
Cancelling an active query subscription attempts to stop any streaming traffic associated with a specific subscription.
Cancellation is also issued asynchronously and any responses that are still in transit will be routed to the default response handler specified by `addDefaultCallbackHandler()` call.
```java
	// run query in async mode with a callback
	JKQueryHandle qhandle = jkQueryAsync.callAsync("get number of events for today", new MyJKQueryCallback());
	...
	// attempt to cancel subscription to the query results
	qhandle.cancelAsync(qhandle);
```
JKQL queries can aslo be executed using prepared JKQL statements as follows:
```java
	JKStatement jkql = jkQueryAsync.prepare("get number of events for today", new MyJKQueryCallback());
	JKQueryHandle qhandle = jkql.call(100); // call with specified max rows for responses
```
### Connection Event Handling
Customized connection handlers can be used to intercept and handle WebSocket connection events such as open, close, error:
```java
public class MyConnectionHandler implements JKConnectionHandler {
	@Override
	public void error(JKQueryAsync async, Throwable ex) {
		System.err.println("error:: " + async + ", error=" + ex);
		ex.printStackTrace();
	}

	@Override
	public void close(JKQueryAsync async, CloseReason reason) {
		System.out.println("close: " + async + ", reason=" + reason);
	}

	@Override
	public void open(JKQueryAsync async) {
		System.out.println("open: " + async);
	}
}

```
###Subscribing to real-time event streams
Developers can also subscribe to live data streams using `JKQueryAsync` class. Subscriptons are based continous queries submitted by the client and run on the jKool servers. The results of the query are emmitted as data becomes available and streamed back to the client call back handler instance of `JKQueryCallback`. See example below:
```java
	// setup jKool WebSocket connection and connect
	JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
	jkQueryAsync.addConnectionHandler(new JKRetryConnectionHandler(5000, TimeUnit.MILLISECONDS));
	jkQueryAsync.addConnectionHandler(new MyConnectionHandler());
		
	// setup a default response handler for responses not associated with any specific query
	jkQueryAsync.addDefaultCallbackHandler(new MyJKQueryCallback());
	jkQueryAsync.connect(); // connect stream with WebSocket interface
		
	// run subscription query in async mode with a callback
	JKQueryHandle qhandle = jkQueryAsync.subAsync("events where severity > 'INFO'", new MyJKQueryCallback());
	...
```
The code above is equivalent to the JKQL statement `subscribe to events where severity > 'INFO'`. `MyJKQueryCallback()` gets called as the query matches incoming streams. All pattern stream matching is done on the jKool server side. `subscribe` query runs on real-time streams only and never on past data. Use `get` queries to get past data.

###Running jKool searches on message content
`JKQueryAsync` class provides a helper method to run pattern macthes against event message content. See below:
```java
	// run search query in async mode with a callback
	JKQueryHandle qhandle = jkQueryAsync.searchAsync("failure", 10, new MyJKQueryCallback());
	...
```
The code above is equivalent to the JKQL statement `get events where message contains "failure"`, where 10 is the maximum number of matching rows to return (default is 100). The example above can be implemented as:
```java
	// run query in async mode with a callback
	JKQueryHandle qhandle = jkQueryAsync.callAsync("get events where message contains \"failure\"", 10, new MyJKQueryCallback());
	...
```
###Running jKool queries from command line
You can run jKool queries from command line using a helper class `JKQLCmd` below. Please run all commands from the 'build' directory that Maven will create.
```sh
	java -jar ./lib/jkool-client-api-<version>.jar -token access-token -query "get events" -wait 30000
```
Running message payload searches:
```sh
	java -jar ./lib/jkool-client-api-<version>.jar -token access-token -search "failure" -wait 30000
```
Command line arguments can be specified via a property file, where any command line argument overrides values specified in the property file:
```sh
	java -jar ./lib/jkool-client-api-<version>.jar -file cmd.properties -query "get number of events for today"
```
Below is a sample property file containing `JKQLCmd` command line arguments (`token` should have your jKool API access token):
```properties
token=your-access-token
uri=wss://jkool.jkoolcloud.com/jkool-service/jkqlasync
query=get number of events
trace=true
wait=15000
maxrows=100
retry=0
#jpath=jk_response/rows-found
```

###Streaming with Curl
Data can also be streamed natively (without helper classes) into jKool using Curl. Below is an example:

```java
curl -i -H "Content-Type:application/json" -H "token:YOURTOKEN" -X POST https://data.jkoolcloud.com/JESL/event -d '{"operation":"streamingwithcurl","type":"EVENT","start-time-usec":1457524800000000,"end-time-usec":1457524800000000,"msg-text":"Example Curl Streaming","source-fqn":"APPL=TestingCurl#SERVER=CurlServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=52.52437,13.41053"}'
```

###Streaming with Python
Data can also be streamed natively (without helper classes) into jKool using Python. To do so, you will need to use the Python "Request". Details on the Python Request can be found here - http://docs.python-requests.org/en/latest/user/quickstart/. Below is an example of using it to stream into jKool:

```java
import requests
headers = {'token': 'YOURTOKEN'}
payload={'operation':'streamingwithpython','type':'EVENT','start-time-usec':1457524800000000,'end-time-usec':1457524800000000,'msg-text':'Example Python Streaming','source-fqn':'APPL=TestingCurl#SERVER=CurlServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=52.52437,13.41053'}
r = requests.post('https://data.jkoolcloud.com/JESL/event', headers=headers, json=payload)
```
###Query jKool using Curl

Rest can be used to retrieve data natively (without helper classes) out of jKool uing Curl. Below is an example. Please note that you also have the option of putting the token in the header instead of specifying it as a parameter. 

```java
curl -i -X GET 'https://jkool.jkoolcloud.com/jkool-service/jkql?jk_query=get%20events&jk_token=YOUR-TOKEN'
```
### Note on time stamps
Time stamp fields such as `time-usec`, `start-time-usec` and `end-time-usec` are measured in microseconds (usec), between the current time and midnight, January 1, 1970 UTC. Most language environments don't return such time in microsecond precision, in which case you would have to compute it by obtaining current time in milliseconds and convert to microseconds (e.g. `System.currentTimeMillis() * 1000`).
