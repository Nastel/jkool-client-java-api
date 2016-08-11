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
package com.jkoolcloud.rest.samples.query;

import javax.ws.rs.core.Response;

import com.jkoolcloud.rest.api.service.JKQuery;
import com.jkoolcloud.rest.api.utils.JKCmdOptions;

/**************************************************************************************************************************
 * This example demonstrates how to retrieve data from jKool via JKQL
 * using {@code jKoolQuery.call()}
 ***********************************************************************************************************************/

public class QueryData2 {
	public static void main(String[] args) {
		try {
			JKCmdOptions options = new JKCmdOptions(args);
			if (options.usage != null) {
				System.out.println(options.usage);
				System.exit(-1);
			}
			JKQuery jkQuery = new JKQuery(System.getProperty("jk.access.token", options.token));
			Response response = jkQuery.call(options.query);
			System.out.println(response.readEntity(String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
