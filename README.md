# JKQL Streaming & Query API Using REST

-----------------------

**NOTE:** `jkool-client-api` version `0.3.3` migrated from `javax.ws.rs` to `jakarta.ws.rs` API.

-----------------------

JKQL Streaming & Query API allows you to send events, metrics, transactions to and run queries against your data repository. You will need
"access token” with streaming permission to store data and "access token" with query permission to run queries. Tokens are associated with
your repository and user profile. The API uses HTTP(s) and WebSockets protocols and responses are JSON.

Other language bindings can be generated with the Swagger Code Generator using the Swagger yaml file found it the "swagger" folder.

Please be aware the the Swagger yaml file is documenting every field that can be passed via Restful API. When using this Java Helper API,
many fields will have default values.

## Concepts and Terminology

You can find more info in [jKool Streaming Guide](https://www.jkoolcloud.com/download/jkool-model.pdf). JKQL streaming supports the
following data collection types:
| Type | Description |
|------|-------------|
|Event|basic time series element containing time, message, severity and other fields associated with event|
|Activity|a group of events and other activities (e.g. transactions)|
|Snapshot|categorized collection of properties (name, value, type) at a "point in time"|
|Dataset|user defined set of data elements with user defined columns|
|Property|name, value pair. Properties can be associated with events, activities and snapshots|

This Git repository contains a Swagger yaml file. Open this file in a Swagger Editor and you will have detailed documentation of each field
that comprises the above mentioned data.

## How to build

To use this sample code please do one of the following:

* Build this project on your own by using these Maven build configurations:
    * To build the project, run Maven goals `clean package`
    * To build the project and install to local repo, run Maven goals `clean install`
    * To make distributable release assemblies use one of profiles: `pack-bin` or `pack-all`:
        * containing only binary (including `test` package) distribution: run `mvn -P pack-bin`
        * containing binary (including `test` package), `source` and `javadoc` distribution: run `mvn -P pack-all`
    * To make Maven required `source` and `javadoc` packages, use profile `pack-maven`
    * To make Maven central compliant release having `source`, `javadoc` and all signed packages, use `maven-release` profile

  Release assemblies are built to `/build` directory.
* Add the following into your Maven pom file:

```pom
    <dependency>
        <groupId>com.jkoolcloud.client.api</groupId>
        <artifactId>jkool-client-api</artifactId>
        <version>0.3.3</version>
    </dependency>
```

## Streaming using over HTTPS

Streaming allows developers to send time series data such as events, metrics, transactions, logs using JSON/HTTPS. You will need your access
token with streaming permission. This token ensures that the streaming data goes to the repository associated with the access token.

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

Please note that this example code depicts streaming in real-time. Therefore the start date of the event will default to the current
date/time and the end date will default to the start date plus the elapsed time. You can however control start/end dates. For example:

```java
    event.setTime(System.currentTimeMillis()).setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2));
```

Optionally add any user defined properties using `Property` class:

```java
    Property customerName = new Property("Name", "John Smith");
    Property customerAge = new Property("Age", 26, ValueType.VALUE_TYPE_AGE_YEAR);
    Property customerTemp = new Property("Temp", 98.6, ValueType.VALUE_TYPE_TEMP_F);
    event.addProperty(customerName, customerAge, customerTemp);
```

Properties can be grouped and categorized using `Snapshot` class:

```java
    // create a categorized snapshot (envelope)
    Snapshot customer = new Snapshot("CustomerData", "General");
    Property customerName = new Property("Name", "John Smith");
    Property customerAge = new Property("Age", 26, ValueType.VALUE_TYPE_AGE_YEAR);
    Property customerTemp = new Property("Temp", 98.6, ValueType.VALUE_TYPE_TEMP_F);
    customer.addProperty(customerName, customerAge, customerTemp);
    // add snapshot to event
    event.addSnapshot(customer);
```

Finally, invoke the post method on the `JKStream` object, passing it the event you wish to stream:

```java
    JKStream jkSend = new JKStream("yourtoken");
    Event event = new Event("Casablanca");
    event.setAppl("WebOrders").setServer(InetAddress.getLocalHost().getHostName())
        .setNetAddr(InetAddress.getLocalHost().getHostAddress()).setDataCenter("DCNY")
        .setElapsedTimeUsec(TimeUnit.HOURS.toMicros(2)).setLocation("New York, NY")
        .setMsgText("Casablanca is playing.");

    // create custom properties
    Property customerName = new Property("Name", "John Smith");
    Property customerAge = new Property("Age", 26, ValueType.VALUE_TYPE_AGE_YEAR);
    Property customerTemp = new Property("Temp", 98.6, ValueType.VALUE_TYPE_TEMP_F);

    event.addProperty(customerName, customerAge, customerTemp);
    Response response = jkSend.post(event);
    response.close();
```

The `JKStream` formats the `event` into JSON and sends it to https://data.jkoolcloud.com/JESL.

### Running JKQL (Synchronously)

In addition to streaming, data can also be retrieved from jKool via Rest. To do this, make use of the jKool Query Language (JKQL). Please
see [JKQL Reference Guide](https://www.nastel.com/wp-content/uploads/2020/03/Nastel_jKQL_User_Guide.pdf). Use the `JKQuery` to run JKQL
synchronously. Use your access token along with the JKQL query. Below is an example:

```java
    JKQuery jkQuery = new JKQuery("yourtoken");
    Response response = jkQuery.call("get number of events for today");
    Map<String, Object> jsonResponse = response.readEntity(Map.class);
    response.close();
```

All returned JKQL responses are JSON.

### Running JKQL (Asynchronously)

Developers can also invoke JKQL queries asynchronously using callbacks. To do this, make use of the `JKQueryAsync`. Below is an example.
This example makes use of two connection handlers: 1) for tracing connection events and 2) for retrying connection during failures.

```java
    // setup WebSocket connection and connect
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    // retry connection handler
    jkQuery.addConnectionHandler(new JKRetryConnectionHandler(5000, TimeUnit.MILLISECONDS));
    // trace connection handler
    jkQuery.addConnectionHandler(new JKTraceConnectionHandler(System.out, true));
    ...
    jkQuery.connect();
```

The next step is to setup default callback handlers (optional but recommended). Default callback handlers are called for responses not
associated with any specific query or subscription.

```java
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    // setup a default response handler for responses not associated with any specific query
    jkQuery.addDefaultCallbackHandler(new JKTraceQueryCallback(System.out, true));
    jkQuery.connect(); // connect stream with WebSocket interface
```

Next execute your query. All response will be delegated to all default callback handlers, because no callback has been associated with this
query:

```java
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    // run query in async mode without a callback (use default response handlers)
    jkQuery.callAsync("get number of events for today");
    ...
    jkQuery.close(); // close connection
```

Alternatively you can execute a query with a specific callback instance. All responses associated with this query will be routed to the
callback instance specified in the `JKQueryAsync.callAsync(...)` call.

```java
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    // run query in async mode with a specific callback
    JKStatementAsync query = jkQuery.callAsync("get events", new MyJKQueryCallback());
    query.awaitOnDone(10000, TimeUnit.MILLISECONDS); // optional wait 10s for query to finish
    ...
    query.close(); // close query statement
    jkQuery.close(); // close connection
```

`MyJKQueryCallback.onResponse()` is called when for every response to the query -- there maybe one or more responses depending on the query.
`MyJKQueryCallback.onClose()` is called when the handle is closed due to `JKStatementAsync.close()`.
`MyJKQueryCallback.onDone()` is called when the handle will never be called again. This happens when the query is cancelled using
`JKQueryAsync.cancelAsync()` call or when all responses associated with a specific query have been delivered.

```java
public class MyJKQueryCallback implements JKQueryCallback {
    @Override
    public void onResponse(JKStatementAsync qHandle, JsonObject response, Throwable ex) {
        System.out.println("response: handle=" + qHandle + ", response=" + response);
        if (ex != null) {
            System.out.println("error: handle=" + qHandle + ", error=" + ex);
        }
    }

    @Override
    public void onClose(JKStatementAsync qHandle) {
        if (trace) {
            out.println("Closed handle=" + qHandle);
        }
    }

    @Override
    public void onDone(JKStatementAsync qHandle) {
        if (trace) {
            out.println("Done handle=" + qHandle);
        }
    }
}
```

`jkQueryAsync.callAsync()` returns a query statement (instance of `JKStatementAsync`), which can be used later to cancel subscriptions.
Cancelling an active query subscription attempts to stop any streaming traffic associated with a specific subscription. Cancellation is also
issued asynchronously and any responses that are still in transit will be routed to the default response handler specified
by `addDefaultCallbackHandler()` call.

```java
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    // run query in async mode with a callback
    JKStatementAsync qhandle = jkQuery.callAsync("get number of events for today", new MyJKQueryCallback());
    ...
    // attempt to cancel subscription to the query results
    qhandle.cancelAsync(qhandle);
```

JKQL queries can also be executed using prepared JKQL statements as follows:

```java
   JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
   JKStatementAsync query = jkQuery.prepare("get number of events for today", new MyJKQueryCallback());
   query.callAsync(100); // call with specified max rows for responses
   query.awaitOnDone(10000, TimeUnit.MILLISECONDS); // wait for completion for 10 seconds
```

### Connection Event Handling

Connection handlers can be used to intercept and handle WebSocket connection events such as open, close, error:

```java
public class MyConnectionHandler implements JKConnectionHandler {
    @Override
    public void error(JKQueryAsync async, Throwable ex) {
        System.err.println("error: " + async + ", error=" + ex);
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

Connection handlers can be associated with a JKQL connection handle `JKQueryAsync` as follows:

```java
    // setup jKool WebSocket connection and connect
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    jkQueryAsync.addConnectionHandler(new MyConnectionHandler());
    ...
    jkQueryAsync.connect();
```

### Subscribing to Real-time Event Streams

Developers can also subscribe to live data streams using `JKQueryAsync` class. Subscriptions are based continuous queries submitted by the
client and run on the jKool servers. The results of the query are emitted as data becomes available and streamed back to the client call
back handler instance of `JKQueryCallback`. See example below:

```java
    // setup WebSocket connection and connect
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    jkQuery.addConnectionHandler(new JKRetryConnectionHandler(5000, TimeUnit.MILLISECONDS));
    jkQuery.addConnectionHandler(new MyConnectionHandler());

    // setup a default response handler for responses not associated with any specific query
    jkQuery.addDefaultCallbackHandler(new MyJKQueryCallback());
    jkQuery.connect(); // connect stream with WebSocket interface

    // run subscription query in async mode with a callback
    JKStatementAsync qhandle = jkQuery.subAsync("events where severity > 'INFO'", new MyJKQueryCallback());
    ...
```

The code above is equivalent to the JKQL statement `subscribe to events where severity > 'INFO'`. `MyJKQueryCallback()` gets called as the
query matches incoming streams. All pattern stream matching is done on the jKool server side. `subscribe` query runs on real-time streams
only and never on past data. Use `get` queries to get past data.

### Running JKQL Searches on Message Content

`JKQueryAsync` class provides a helper method to run pattern matches against event message content. See below:

```java
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    ...
    // run search query in async mode with a callback
    JKStatementAsync qhandle = jkQuery.searchAsync("failure", 10, new MyJKQueryCallback());
    ...
```

The code above is equivalent to the JKQL statement `get events where message contains "failure"`, where 10 is the maximum number of matching
rows to return (default is 100). The example above can be implemented as:

```java
    JKQueryAsync jkQuery = new JKQueryAsync("yourtoken");
    ...
    // run query in async mode with a callback
    JKStatementAsync qhandle = jkQuery.callAsync("get events where message contains \"failure\"", 10, new MyJKQueryCallback());
    ...
```

### Running JKQL from Command Line

You can run JKQL from command line using a helper class `JKQLCmd` below. Run all commands from the root `jkool-client-api-<version>`
directory. `JKQLCmd` uses Secure WebSocket/JSON interface to run JKQL.

```sh
    java -cp ./*:./lib/* com.jkoolcloud.client.api.utils.JKQLCmd -token access-token -query "get events" -wait 30000
```

Running message content searches:

```sh
    java -cp ./*:./lib/* com.jkoolcloud.client.api.utils.JKQLCmd -token access-token -search "failure" -wait 30000
```

Command line arguments can be specified via a property file, where any command line argument overrides values specified in the property
file:

```sh
    java java -cp ./*:./lib/* com.jkoolcloud.client.api.utils.JKQLCmd -file cmd.properties -query "get number of events for today"
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

### Running JKQL using Curl

REST can be used to retrieve data natively (without helper classes) out of your repository using `curl`. Note that you can specify your
token in the HTTP header (`X-API-Key`) as well instead of specifying it as a query parameter (`jk_token`). Access tokens must have
query/read permission, streaming tokens don't have query access by default.

Example using `jk_token` parameter to pass access token:

```sh
curl -i -d "jk_token=access-token&jk_query=get number of events" -X POST https://jkool.jkoolcloud.com/jkool-service/jkql
```
Example using (`X-API-Key`) to pass access token: 
```sh
curl -i -H "X-API-Key: Access-Token" -d "jk_query=get number of events" -X POST https://jkool.jkoolcloud.com/jkool-service/jkql
```
Below is a list of supported query parameters: 
| Parameter | Required | Default| Description |
|-----------|----------|--------|-------------|
|`jk_token`|Yes|None|API access token|
|`jk_query`|Yes|None|query statement to run|
|`jk_subid`|No|Auto|query request correlator (GUID)|
|`jk_tz`|No|Server TZ|timezone to be used for timestamps|
|`jk_locale`|No|Server Locale|locale to be used for date/time and number formats|
|`jk_date`|No|today|date range for the query|
|`jk_maxrows`|No|100|maximum rows to be fetched|
|`jk_trace`|No|false|enable query trace during execution|
|`jk_timeout`|No|60000|max query timeout in ms|
|`jk_range`|No|None|query range for`find` queries only|
|`jk_slow`|No|5000|Time in ms beyond which query is considered slow|

Below are common JSON response fields:
| Field | Description |
|-----------|---------|
|`jk_call`|query call verb|
|`jk_query`|query associated with the response|
|`jk_ccode`|query response completion code|
|`jk_error`|query error message if fails|
|`jk_subid`|query correlator associated with the request|
|`jk_elapsed_ms`|elapsed time to execute the query (ms)|

Example of a failed response:

```json
{
    "jk_call": "get",
    "jk_ccode": "ERROR",
    "jk_elapsed_ms": 8,
    "jk_subid": "f41194b0-5b09-4464-890b-36fd66c01738",
    "jk_error": "com.nastel.jkool.jkql.admin.JKQLSecurityException: Undefined access token 'X', stmt: get number of logs"
}
```

Example of a successful response:

```json
{
    "rows-found": 798,
    "row-count": 1,
    "total-row-count": 1,
    "data-date-range": "1590206401731372 TO 1590248453356930",
    "query-date-filter": "1590206400000000 TO 1590292799999999",
    "timezone": "Eastern Daylight Time",
    "status": "SUCCESS",
    "time": 1590248630137306,
    "item-type": "Log",
    "colhdr": [
        "NumberOf"
    ],
    "coltype": {
        "NumberOf": "INTEGER"
    },
    "collabel": {
        "NumberOf": "NumberOf"
    },
    "rows": [
        {
            "NumberOf": 798
        }
    ],
    "overallStatistics": {
        "jkql_parse.time.usec": 19,
        "jkql_statement.count": 1,
        "json_string.time.usec": 46,
        "raw_result_post_process.time.usec": 0,
        "request_wait.time.usec": 69,
        "rows_found.count": 798,
        "rows_returned.count": 1,
        "solr_request_build.time.usec": 88,
        "solr_request_elapsed.time.usec": 11000,
        "solr_request_exec.time.usec": 11905,
        "solr_request_qtime.time.usec": 0,
        "solr_result_proc.time.usec": 12,
        "total_exec.time.usec": 47707
    }
}
```

### Streaming with Curl

Data can be streamed using `curl`. Below is an example:

```sh
curl -i -H "Content-Type:application/json" -H "X-API-Key:YOURTOKEN" -X POST https://data.jkoolcloud.com/JESL/event -d '{"operation":"streamingwithcurl","type":"EVENT","start-time-usec":1457524800000000,"end-time-usec":1457524800000000,"msg-text":"Example curl streaming","source-fqn":"APPL=TestingCurl#SERVER=CurlServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=52.52437,13.41053"}'
```

### Streaming with Python

Streaming data using Python "requests" object. Below is an example:

```python
import requests
headers = {'X-API-Key': 'YOURTOKEN'}
payload={'operation':'streamingwithpython','type':'EVENT','start-time-usec':1457524800000000,'end-time-usec':1457524800000000,'msg-text':'Example Python Streaming','source-fqn':'APPL=TestingCurl#SERVER=CurlServer100#NETADDR=11.0.0.2#DATACENTER=DC1#GEOADDR=52.52437,13.41053'}
resp = requests.post('https://data.jkoolcloud.com/JESL/event', headers=headers, json=payload)
```

### Note on Timestamps

Timestamp fields such as `time-usec`, `start-time-usec` and `end-time-usec` are measured in microseconds (usec.), between the current time
and midnight, January 1, 1970 UTC. Most language environments don't return such time in microsecond precision, in which case you would have
to compute it by obtaining current time in milliseconds and convert to microseconds (e.g. `System.currentTimeMillis() * 1000`).
