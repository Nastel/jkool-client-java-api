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
package com.jkoolcloud.rest.api.utils;

import javax.json.JsonObject;

import com.jkoolcloud.rest.api.service.JKQueryCallback;
import com.jkoolcloud.rest.api.service.JKQueryHandle;

public class JKCmdCallbackHandler implements JKQueryCallback {

	@Override
	public void handle(JKQueryHandle qhandle, JsonObject response, Throwable ex) {
		if (ex != null) {
			System.err.println("Error for query=" + qhandle + ", error=" + ex.getMessage());
			ex.printStackTrace();
		} else {
			System.out.println(JKUtils.prettyPrint(response));
		}
	}
}