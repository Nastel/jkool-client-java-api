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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 */
@ClientEndpoint
public class WebsocketClient {

	Session userSession = null;
	private JKMessageHandler messageHandler;

	public WebsocketClient(String uri, JKMessageHandler handler) throws URISyntaxException {
		this(new URI(uri), handler);
	}

	public WebsocketClient(URI uri, JKMessageHandler handler) {
		try {
			setMessageHandler(handler);
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, uri);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Callback hook for Connection open events.
	 *
	 * @param userSession
	 *            the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("opening websocket");
		this.userSession = userSession;
		if (this.messageHandler != null) {
			this.messageHandler.onOpen(this, userSession);
		}
	}

	/**
	 * Callback hook for Connection error events.
	 *
	 * @param userSession
	 *            the userSession which is opened.
	 */
	@OnError
	public void onError(Session userSession, Throwable ex) {
		System.out.println("error websocket: " + ex);
		if (this.messageHandler != null) {
			this.messageHandler.onError(this, userSession, ex);
		}
	}

	/**
	 * Callback hook for Connection close events.
	 *
	 * @param userSession
	 *            the userSession which is getting closed.
	 * @param reason
	 *            the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		if (this.messageHandler != null) {
			this.messageHandler.onClose(this, userSession, reason);
		}
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a client send a message.
	 *
	 * @param message
	 *            The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		if (this.messageHandler != null) {
			this.messageHandler.handle(this, message);
		}
	}

	/**
	 * register message handler
	 *
	 */
	private void setMessageHandler(JKMessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	/**
	 * Send a message async
	 * @throws IOException 
	 *
	 */
	public void sendMessageAsync(String message) throws IOException {
		if (this.userSession != null) {
			this.userSession.getAsyncRemote().sendText(message);
		} else {
			throw new IOException("Session not available");
		}
	}
	
	/**
	 * Send a message async
	 * @throws IOException 
	 *
	 */
	public void sendMessageSync(String message) throws IOException {
		if (this.userSession != null) {
			this.userSession.getBasicRemote().sendText(message);
		} else {
			throw new IOException("Session not available");
		}
	}
}
