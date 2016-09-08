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
	void open(JKQueryAsync async);
	void error(JKQueryAsync async, Throwable ex);
	void close(JKQueryAsync async, CloseReason reason);
}
