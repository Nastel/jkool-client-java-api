/*
 * Copyright 2014-2023 JKOOL, LLC.
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

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;

import com.jkoolcloud.client.api.utils.JKUtils;

import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

/**
 * This class implements a simple {@code JKQueryCallback} with trace messages.
 * 
 * @see JKQueryCallback
 * @author albert
 */
public class JKTraceQueryCallback implements JKQueryCallback {
	PrintStream out;
	String json_path;
	boolean trace = true;
	Throwable lastError;

	AtomicLong msgCount = new AtomicLong(0);
	AtomicLong errCount = new AtomicLong(0);

	/**
	 * Create a trace query callback instance
	 * 
	 */
	public JKTraceQueryCallback() {
		this(System.out, true);
	}

	/**
	 * Create a trace query callback instance
	 * 
	 * @param out
	 *            output print stream
	 * 
	 */
	public JKTraceQueryCallback(PrintStream out) {
		this(out, true);
	}

	/**
	 * Create a trace query callback instance
	 * 
	 * @param out
	 *            output print stream
	 * @param flag
	 *            flag
	 * 
	 */
	public JKTraceQueryCallback(PrintStream out, boolean flag) {
		this.out = out;
		setTrace(flag);
	}

	/**
	 * Create a trace query callback instance
	 * 
	 * @param out
	 *            output print stream
	 * @param jsonPath
	 *            json path to get from the response
	 * @param flag
	 *            flag
	 * 
	 */
	public JKTraceQueryCallback(PrintStream out, String jsonPath, boolean flag) {
		this.out = out;
		this.json_path = jsonPath;
		setTrace(flag);
	}

	/**
	 * Enable/disable trace mode
	 * 
	 * @param flag
	 *            trace flag
	 * @return self
	 */
	public JKTraceQueryCallback setTrace(boolean flag) {
		this.trace = flag;
		return this;
	}

	/**
	 * Get error count
	 * 
	 * @return obtain error count
	 */
	public long getErrorCount() {
		return errCount.get();
	}

	/**
	 * Get received message count
	 * 
	 * @return obtain error count
	 */
	public long getMsgCount() {
		return msgCount.get();
	}

	/**
	 * Get last error
	 * 
	 * @return last error
	 */
	public Throwable getLastError() {
		return lastError;
	}

	@Override
	public void onCall(JKStatementAsync qhandle, JsonObject jsonCall) {
		if (trace) {
			out.println("Query JSON: ");
			out.println(JKUtils.prettyPrint(jsonCall));
		}
	}

	@Override
	public void onResponse(JKStatementAsync qhandle, JsonObject response, Throwable ex) {
		if (ex != null) {
			lastError = ex;
			errCount.incrementAndGet();
			out.println("\nOn Error(" + errCount.get() + "):");
			if (response != null) {
				out.println(JKUtils.prettyPrint(response));
			} else {
				out.println("Error: handle=" + qhandle + ", error=" + ex.getMessage());
				ex.printStackTrace(out);
			}
		} else {
			msgCount.incrementAndGet();
			out.println("\nOn Response(" + msgCount.get() + "):");
			if (json_path == null) {
				out.println(JKUtils.prettyPrint(response));
			} else {
				JsonValue jsonPath = JKUtils.getJsonValue(json_path, response);
				if (jsonPath == null) {
					out.print("\"" + json_path + "\" not found");
				} else if (jsonPath instanceof JsonStructure) {
					out.println(JKUtils.prettyPrint((JsonStructure) jsonPath));
				} else {
					out.format("%s = %s, %s", json_path, jsonPath, jsonPath.getValueType());
				}
			}
		}
	}

	@Override
	public void onDone(JKStatementAsync qhandle) {
		if (trace) {
			out.println("Done: handle=" + qhandle);
		}
	}

	@Override
	public void onClose(JKStatementAsync qhandle) {
		if (trace) {
			out.println("Closed: handle=" + qhandle);
		}
	}
}
