# jKoolRestClients
jKool Rest Clients

This GIT repository contains Java helper classes that will help you to get up and running very quickly with the jKool Streaming API. You will need a streaming  “token” in order to stream. This token will be associated with a repository that will be assigned to you when you sign-up for jKool.  The token is passed in the request header.

How to Stream data into jKool:
There are four types of data that can be streamed into jKool. They are:
* Events: The main piece of data. Events contain pre-defined jKool fields and can also contain your own custom fields (as snapshots and properties, explained below)
* Activities: A way of organizing events into groupings (categories)
* Snapshots: A way of organizing custom fields into groupings (categories)
* Properties: Your custom fields.

This repository contains a Swagger yaml file. Open this file in a Swagger Editor and you will have detailed documentation of each field that comprises the above mentioned data.

** Important note … this helper code is extremely simple.  Please be advised that jKool will handle the most simple of use cases to the most complex use cases. For example, it is built with the ability to correlate events and track transactions among multiple applications.  This can be used for complex system analysis, for instance - to monitor system performance. For details on complex streaming, please see our full documentation at https://www.jkoolcloud.com/wiki/index.php/Main_Page.

To use this helper code please do the following:
* Create a Client object. This helper code is using RestEasy to do this.
* Import the provided jKool objects residing in the "model" directory into your code.
* Populate these jKool objects with your data. A very simple example of doing this is in the RunApi.java class contained in the "api" directory.
* As seen in RunApi, invoke the post request on the client object sending the objects over as request entities.

That's it!! Any problems or concerns, please email us at support@jkoolcloud.com.




