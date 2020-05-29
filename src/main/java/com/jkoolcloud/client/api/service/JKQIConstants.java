/*
 * Copyright 2014-2019 JKOOL, LLC.
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
package com.jkoolcloud.client.api.service;

public interface JKQIConstants {
	static final String JKOOL_TOKEN = System.getProperty("jkool.api.token");

	static final String JKOOL_STREAM_URL = System.getProperty("jkool.stream.url", "https://data.jkoolcloud.com/JESL");
	static final String JKOOL_QUERY_URL = System.getProperty("jkool.query.url",
			"https://jkool.jkoolcloud.com/jkool-service/jkql");
	static final String JKOOL_WEBSOCK_URL = System.getProperty("jkool.websock.url",
			"wss://jkool.jkoolcloud.com/jkool-service/jkqlasync");

	// HTTP header keys
	static final String CONTENT_TYPE = "content-type";
	static final String X_API_KEY = "X-API-Key";
	static final String X_REFERER = "Referer";
	static final String X_API_HOSTNAME = "X-API-Host-Name";
	static final String X_API_HOSTADDR = "X-API-Host-Addr";
	static final String X_API_RUNTIME = "X-API-Runtime";
	static final String X_API_VERSION = "X-API-Version";
	static final String X_API_TOKEN = "token";

	static final String JK_EVENT_KEY = "event";
	static final String JK_ACTIVITY_KEY = "activity";
	static final String JK_DATASET_KEY = "dataset";
	static final String JK_SNAPSHOT_KEY = "snapshot";
	static final String JK_LOG_KEY = "log";

	static final String JK_TOKEN_KEY = "jk_token";
	static final String JK_QUERY_KEY = "jk_query";
	static final String JK_MAX_ROWS_KEY = "jk_maxrows";
	static final String JK_TIME_ZONE_KEY = "jk_tz";
	static final String JK_REPO_KEY = "jk_repo";
	static final String JK_DATE_KEY = "jk_date";
	static final String JK_TRACE_KEY = "jk_trace";

	static final String JK_SUBID_KEY = "jk_subid";
	static final String JK_ERROR_KEY = "jk_error";
	static final String JK_CALL_KEY = "jk_call";
	static final String JK_RESPONSE_KEY = "jk_response";

	static final String JK_CALL_CANCEL = "unsubscribe";

	static final String JK_SUB_UUID_PREFIX = "$sub/";
	static final String JK_SUB_QUERY_PREFIX = "subscribe to ";
	static final String JK_SUB_QUERY_PREFIX2 = "sub to ";
	static final String JK_UNSUB_QUERY_PREFIX = "unsubscribe from ";
	static final String JK_SEARCH_QUERY_PREFIX = "get events where message contains \"%s\"";

	static final int DEFAULT_MAX_ROWS = 100;
	static final int DEFAULT_WAIT_TIME = 20000;
	static final int DEFAULT_RETRY_TIME = 0;
}
