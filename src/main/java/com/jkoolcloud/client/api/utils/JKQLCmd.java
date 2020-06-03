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
package com.jkoolcloud.client.api.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.jkoolcloud.client.api.service.JKQueryAsync;
import com.jkoolcloud.client.api.service.JKRetryConnectionHandler;
import com.jkoolcloud.client.api.service.JKStatementAsync;
import com.jkoolcloud.client.api.service.JKTraceConnectionHandler;
import com.jkoolcloud.client.api.service.JKTraceQueryCallback;

public class JKQLCmd {
	public static void main(String[] args) {
		JKCmdOptions options = new JKCmdOptions(JKQLCmd.class, args);
		if (options.usage != null) {
			System.out.println(options.usage);
			System.exit(-1);
		}
		options.print();
		JKTraceQueryCallback callback = new JKTraceQueryCallback(System.out, options.json_path, options.trace);
		try (JKQueryAsync jkQueryAsync = new JKQueryAsync(System.getProperty("jk.ws.uri", options.uri), System.getProperty("jk.access.token", options.token))) 
		{
			// setup WebSocket connection and connect
			if (options.retryTimeMs > 0) {
				jkQueryAsync.addConnectionHandler(new JKRetryConnectionHandler(options.retryTimeMs, TimeUnit.MILLISECONDS));
			}
			jkQueryAsync.setTimeZone(options.timezone)
				.setDateFilter(options.daterange)
				.setRepoId(options.reponame)
				.setTrace(options.trace);

			jkQueryAsync.addConnectionHandler(new JKTraceConnectionHandler(System.out, options.trace));
			jkQueryAsync.addDefaultCallbackHandler(callback);
			jkQueryAsync.connect();
			

			// run query in async mode with a callback
			JKStatementAsync qhandle = jkQueryAsync.callAsync(options.query, options.maxRows, callback);
			Runtime.getRuntime().addShutdownHook(new VMStop(qhandle));
			System.out.println("Running query=[" + qhandle.getQuery() + "]");
			System.out.println("Prepared statement=[" + qhandle + "]");
			if (!qhandle.isSubscribe()) {
				// standard query only one response expected
				qhandle.awaitOnDone(options.waitTimeMs, TimeUnit.MILLISECONDS);
			} else {
				// streaming query, so lets collect responses until timeout
				Thread.sleep(options.waitTimeMs);
				System.out.println("Cancelling query=[" + qhandle.getQuery() + "], id=" + qhandle.getId() + ", mid=" + qhandle.getLastMsgId());
				qhandle = jkQueryAsync.cancelAsync(qhandle);
				if (qhandle != null) {
					qhandle.awaitOnDone(options.waitTimeMs, TimeUnit.MILLISECONDS);
				}
			}
		} catch (Throwable e) {
			System.err.println("Failed to execute: " + options.toString());
			e.printStackTrace();
		} finally {
			System.out.println("Stats: msg.recvd=" + callback.getMsgCount() + ", err.count=" + callback.getErrorCount());
		} 
	}
}

class VMStop extends Thread {
	JKStatementAsync handle;
	
	protected VMStop(JKStatementAsync h) {
		handle = h;
	}
	
	@Override
	public void run() {
		try {
			if (handle.isConnected() && handle.isSubscribe()) {
				System.out.println("VM Stopping. Cancel subscription on handle=" + handle);
				handle.cancelAsync();
				handle.awaitOnDone(5000, TimeUnit.MILLISECONDS);
			}
			handle.close();
			handle.getQueryAsync().close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("VM Stopped. Handle=" + handle + ", handle.count=" + handle.getQueryAsync().getHandleCount());			
		}
	}
}
