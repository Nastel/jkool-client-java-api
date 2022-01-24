/*
 * Copyright 2014-2021 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkoolcloud.client.samples.query;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jkoolcloud.client.api.service.JKQuery;
import com.jkoolcloud.client.api.utils.JKCmdOptions;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;

/**************************************************************************************************************************
 * This example demonstrates how to retrieve data from jKool via JKQL using {@code jKoolQuery.call()}
 ***********************************************************************************************************************/

public class QExample {
	public static void main(String[] args) throws ProcessingException {
		Properties props = new Properties();
		props.setProperty(JKCmdOptions.PROP_URI, JKQuery.JKOOL_QUERY_URL);
		JKCmdOptions options = new JKCmdOptions(QExample.class, args, props);
		if (options.usage != null) {
			System.out.println(options.usage);
			System.exit(-1);
		}
		options.print();

		try (JKQuery jkQuery = new JKQuery(options.token)) {
			Response res = jkQuery.setTrace(options.trace).call(options.query);
			int status = res.getStatus();
			String json = res.readEntity(String.class);
			System.out.println(String.format("Status: %d\nResponse:\n%s", status, formatJson(json)));
			res.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static String formatJson(String input) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(input, Object.class);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
	}
}
