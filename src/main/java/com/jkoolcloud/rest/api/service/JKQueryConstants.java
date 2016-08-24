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
package com.jkoolcloud.rest.api.service;

public interface JKQueryConstants {
	static final String QUERY_KEY = "query";
	static final String QUERY_ENDPOINT = "jkql";
	static final String CONTENT_TYPE = "content-type";
	static final String JKOOL_TOKEN = System.getProperty("jkool.api.token");

	static final String JKOOL_STREAM_URL = System.getProperty("jkool.stream.url", "https://data.jkoolcloud.com/JESL");
	static final String JKOOL_QUERY_URL = System.getProperty("jkool.query.url", "https://jkool.jkoolcloud.com/jKool/");
	static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url", "wss://jkool.jkoolcloud.com/jkwebsocket/jkqlasync");

	static final String JK_TOKEN_KEY 	= "token";
	static final String JK_QUERY_KEY 	= "jk_query";
	static final String JK_SUBID_KEY 	= "jk_subid";
	static final String JK_ERROR_KEY 	= "jk_error";
	static final String JK_CALL_KEY 	= "jk_call";
	static final String JK_RESPONSE_KEY = "jk_response";
	
	static final String JK_CALL_CANCEL = "unsubscribe";
	static final String JK_MAX_ROWS_KEY = "maxResultRows";

	static final String JK_SUB_UUID_PREFIX = "$sub/";
	static final String JK_SUB_QUERY_PREFIX = "subscribe to ";
	static final String JK_UNSUB_QUERY_PREFIX = "unsubscribe ";	

	static final int DEFAULT_MAX_ROWS = 100;
	static final String JK_SEARCH_QUERY_PREFIX = "get events where message contains \"%s\"";
}
