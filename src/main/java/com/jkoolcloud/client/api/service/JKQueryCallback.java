/*
 * Copyright 2014-2022 JKOOL, LLC.
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
package com.jkoolcloud.client.api.service;

import jakarta.json.JsonObject;

/**
 * Implementations of this interface defines a query response callback for async queries.
 * 
 * @author albert
 */
public interface JKQueryCallback {
	/**
	 * Method called when the callback will no longer be called and all responses for this callback has been received.
	 * 
	 * @param qhandle
	 *            connection handle from which message is received
	 */
	void onDone(JKStatementAsync qhandle);

	/**
	 * Method called when the callback will no longer be called because statement is closed
	 * 
	 * @param qhandle
	 *            connection handle from which message is received
	 */
	void onClose(JKStatementAsync qhandle);

	/**
	 * Method called before call/query is executed
	 * 
	 * @param qhandle
	 *            connection handle from which message is received
	 * @param query
	 *            JSON query
	 */
	void onCall(JKStatementAsync qhandle, JsonObject query);

	/**
	 * Method called when a response to a query is received
	 * 
	 * @param qhandle
	 *            connection handle from which message is received
	 * @param response
	 *            JSON response received
	 * @param ex
	 *            if not null signifies an exception that occurred
	 */
	void onResponse(JKStatementAsync qhandle, JsonObject response, Throwable ex);
}
