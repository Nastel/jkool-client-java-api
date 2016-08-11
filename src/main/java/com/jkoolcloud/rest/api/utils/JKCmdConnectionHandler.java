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

import javax.websocket.CloseReason;

import com.jkoolcloud.rest.api.service.JKConnectionHandler;
import com.jkoolcloud.rest.api.service.JKQueryAsync;

public class JKCmdConnectionHandler implements JKConnectionHandler {

	@Override
	public void open(JKQueryAsync async) {
		System.out.println("Connection open: handle=" + async);
	}

	@Override
	public void error(JKQueryAsync async, Throwable ex) {
		System.err.println("Connection error: " + async + ", reason=" + ex);
		ex.printStackTrace();
	}

	@Override
	public void close(JKQueryAsync async, CloseReason reason) {
		System.out.println("Connection closed: " + reason + ", handle=" + async);
	}
}
