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

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;

/**
 * This class implements a WebSocket message and connection handler.
 * 
 * @author albert
 * @see JKWSHandler
 */
class WSClientHandler implements JKWSHandler {

	private JKQueryAsync jkagent;

	/**
	 * WebSocket message & connection handler
	 * 
	 * @param qagent
	 *            connection handle
	 */
	protected WSClientHandler(JKQueryAsync qagent) {
		jkagent = qagent;
	}

	@Override
	public void onMessage(JKWSClient client, String message) {
		JsonReader reader = Json.createReader(new StringReader(message));
		JsonObject jsonMessage = reader.readObject();
		String subid = jsonMessage.getString(JKQueryAsync.JK_SUBID_KEY, null);
		jkagent.handleResponse(subid, jsonMessage);
	}

	@Override
	public void onClose(JKWSClient client, Session userSession, CloseReason reason) {
		synchronized (jkagent.conHandlers) {
			for (JKConnectionHandler ch : jkagent.conHandlers) {
				ch.close(jkagent, reason);
			}
		}
	}

	@Override
	public void onError(JKWSClient client, Session userSession, Throwable ex) {
		synchronized (jkagent.conHandlers) {
			for (JKConnectionHandler ch : jkagent.conHandlers) {
				ch.error(jkagent, ex);
			}
		}
	}

	@Override
	public void onOpen(JKWSClient client, Session userSession) {
		synchronized (jkagent.conHandlers) {
			for (JKConnectionHandler ch : jkagent.conHandlers) {
				ch.open(jkagent);
			}
		}
	}
}