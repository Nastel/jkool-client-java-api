package com.jkoolcloud.rest.samples.getdata;

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

import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.jkoolcloud.rest.api.utils.jKoolQuery;

/**************************************************************************************************************************
 * This example demonstrates how to retrieve data from jKool via JKQL
 * 
 * ***********************************************************************************************************************/

public class GetData1 {

	public static void main(String[] args) {
		try {
			jKoolQuery jkRetrieve = new jKoolQuery("your-access-token");
			HttpResponse response = jkRetrieve.get("get%20events");
			response.getEntity();
			EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
