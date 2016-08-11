/*
 * Copyright 2014-2015 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkoolcloud.rest.samples.async;

import java.util.concurrent.TimeUnit;

import com.jkoolcloud.rest.api.service.JKQueryAsync;
import com.jkoolcloud.rest.api.service.JKQueryHandle;
import com.jkoolcloud.rest.api.utils.JKCmdOptions;

public class RunQueryAsync {
	public static void main(String[] args) {
		try {
			JKCmdOptions options = new JKCmdOptions(args);
			if (options.usage != null) {
				System.out.println(options.usage);
				System.exit(-1);
			}
			options.print(System.out);
			
			// setup jKool WebSocket connection and connect
			JKQueryAsync jkQueryAsync = new JKQueryAsync(
					System.getProperty("jk.ws.uri", options.uri),
					System.getProperty("jk.access.token", options.token));
			jkQueryAsync.setConnectionHandler(new MyConnectionHandler());
			jkQueryAsync.setDefaultResponseHandler(new MyJKQueryCallback());
			jkQueryAsync.connect();
			
			// run query in async mode with a callback
			JKQueryHandle qhandle = jkQueryAsync.callAsync(options.query, new MyJKQueryCallback());
			System.out.println("callAsync: query.handle=" + qhandle);
			
			// wait for response to come, or do something else
			qhandle.awaitOnCallback(options.waitTimeMs, TimeUnit.MILLISECONDS);
			
			// attempt to cancel subscription to the query results
			if (qhandle.isSubscribeQuery()) {
				qhandle = jkQueryAsync.cancelAsync(qhandle);
				System.out.println("cancelAsync: query.handle=" + qhandle);
				qhandle.awaitOnCallback(options.waitTimeMs, TimeUnit.MILLISECONDS);
			}
			// close async connection, done
			jkQueryAsync.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}