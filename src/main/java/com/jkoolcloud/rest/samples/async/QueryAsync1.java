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

import com.jkoolcloud.rest.api.service.JKQueryAsync;

public class QueryAsync1 {
	public static void main(String[] args) {
		try {
			JKQueryAsync jkQueryAsync = new JKQueryAsync(
					System.getProperty("jk.ws.url", "ws://localhost:8080/jKool/jkqlasync"),
					System.getProperty("jk.access.token", "access-token"));
			jkQueryAsync.setConnectionHandler(new MyConnectionHandler()).setDefaultResponseHandler(new MyJKQueryCallback())
					.connect();
			jkQueryAsync.callAsync("get events", new MyJKQueryCallback());

			Thread.sleep(15000000);
			jkQueryAsync.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
