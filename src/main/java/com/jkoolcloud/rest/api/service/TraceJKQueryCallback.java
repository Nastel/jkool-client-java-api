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

import java.io.PrintStream;

import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import com.jkoolcloud.rest.api.utils.JKUtils;

public class TraceJKQueryCallback implements JKQueryCallback {
	PrintStream out;
	boolean trace = true;
	String json_path;
	
	public TraceJKQueryCallback(PrintStream out) {
		this(out, true);
	}
	
	public TraceJKQueryCallback(PrintStream out, boolean flag) {
		this.out = out;
		setTrace(flag);
	}
	
	public TraceJKQueryCallback(PrintStream out, String jsonPath, boolean flag) {
		this.out = out;
		this.json_path = jsonPath;
		setTrace(flag);
	}
	
	@Override
	public void handle(JKQueryHandle qhandle, JsonObject response, Throwable ex) {
		if (ex != null) {
			out.println("Error on handle=" + qhandle + ", error=" + ex.getMessage());
			ex.printStackTrace(out);
		} else {
			if (json_path == null) {
				out.println(JKUtils.prettyPrint(response));
			} else {
				JsonValue jsonPath = JKUtils.getJsonValue(json_path, response);
				if (jsonPath == null) {
					out.print("\"" + json_path + "\" not found");
				} else if (jsonPath instanceof JsonStructure){
					out.println(JKUtils.prettyPrint((JsonStructure)jsonPath));
				} else {
					out.format("%s = %s, %s", json_path, jsonPath, jsonPath.getValueType());
				}
			}
		}
	}
	
	public TraceJKQueryCallback setTrace(boolean flag) {
		this.trace = flag;
		return this;
	}
}
