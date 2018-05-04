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
package com.jkoolcloud.client.api.service;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.CloseReason;

/**
 * This class implements a simple {@code JKConnectionHandler} with
 * trace messages.
 * 
 * @author albert
 */
public class JKTraceConnectionHandler implements JKConnectionHandler {
	
	PrintStream out;
	boolean trace = true;
	Throwable lastError;
	AtomicLong errCount = new AtomicLong(0);

	/**
	 * Create a trace connection handler
	 * with enabled trace flag.
	 * 
	 * @param out output print stream
	 */
	public JKTraceConnectionHandler(PrintStream out) {
		this(out, true);
	}
	
	/**
	 * Create a trace connection handler
	 * 
	 * @param out output print stream
	 * @param flag trace flag
	 */
	public JKTraceConnectionHandler(PrintStream out, boolean flag) {
		this.out = out;
		setTrace(flag);
	}
	
	@Override
	public void open(JKQueryAsync async) {
		if (out != null && trace) {
			out.println("Connection open: handle=" + async);
		}
	}

	@Override
	public void error(JKQueryAsync async, Throwable ex) {
		lastError = ex;
		errCount.incrementAndGet();
		if (out != null && trace) {
			out.println("Connection error: " + async + ", reason=" + ex);
			ex.printStackTrace(out);
		}
	}

	@Override
	public void close(JKQueryAsync async, CloseReason reason) {
		if (out != null && trace) {
			out.println("Connection closed: " + reason + ", handle=" + async);
		}
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
	 * Get last error
	 * 
	 * @return last error
	 */
	public Throwable getLastError() {
		return lastError;
	}
	
	/**
	 * Enable/disable trace mode
	 * 
	 * @param flag trace flag
	 * @return self
	 */
	public JKTraceConnectionHandler setTrace(boolean flag) {
		this.trace = flag;
		return this;
	}
}
