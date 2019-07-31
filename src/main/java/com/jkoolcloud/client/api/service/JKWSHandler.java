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

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * This interface defines handling of messages and other WebSocket events.
 * 
 * @author albert
 * @see JKWSClient
 */
public interface JKWSHandler {
	void onMessage(JKWSClient client, String message);

	void onOpen(JKWSClient client, Session userSession);

	void onClose(JKWSClient client, Session userSession, CloseReason reason);

	void onError(JKWSClient client, Session userSession, Throwable ex);
}
