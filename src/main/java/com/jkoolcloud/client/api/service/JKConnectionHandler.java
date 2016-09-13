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

import javax.websocket.CloseReason;

/**
 * Implementations of this interface defines a way to handle
 * WebSocket connection events.
 * 
 * @author albert
 */
public interface JKConnectionHandler {
	
	/**
	 * Called when connection is open
	 * 
	 * @param async connection handle
	 */
	void open(JKQueryAsync async);
	
	/**
	 * Called when connection encounters an error
	 * 
	 * @param async connection handle
	 * @param ex exception associated with the error
	 */
	void error(JKQueryAsync async, Throwable ex);
	
	/**
	 * Called when connection is closed
	 * 
	 * @param async connection handle
	 * @param reason reason why connection was closed
	 */	
	void close(JKQueryAsync async, CloseReason reason);
}
