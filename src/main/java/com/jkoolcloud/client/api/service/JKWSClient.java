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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * This class implements WebSockets communication, message and error handling.
 * 
 * @author albert
 * @see JKWSHandler
 */
@ClientEndpoint
public class JKWSClient {

	URI wsUri;
	Session userSession;
	WebSocketContainer container;
	JKWSHandler messageHandler;

	public JKWSClient(String uri, JKWSHandler jkh) throws URISyntaxException {
		this(new URI(uri), jkh);
	}

	public JKWSClient(URI uri, JKWSHandler jkh) {
		this.wsUri = uri;
		setMessageHandler(jkh);
		container = ContainerProvider.getWebSocketContainer();
	}

	public synchronized JKWSClient connect() throws IOException {
		try {
			if (userSession == null) {
				container.connectToServer(this, wsUri);		
			}
		} catch (Throwable ex) {
			throw new IOException("Failed to connect: " + wsUri, ex);
		}
		return this;
	}
	
	/**
	 * Disconnect current connection
	 * 
	 */
	public synchronized JKWSClient disconnect() throws IOException {
		return disconnect(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "normal disconnect"));
	}
	
	/**
	 * Disconnect current connection
	 * 
	 * @param reason close reason
	 */
	public synchronized JKWSClient disconnect(CloseReason reason) throws IOException {
		if (userSession != null) {
			userSession.close(reason);	
		}
		return this;
	}
	
	/**
	 * Obtain underlying web socket session
	 *
	 * @return connection session
	 */
	public Session getSession() {
		return userSession;
	}
	
	/**
	 * Determine if client connection is open
	 *
	 * @return true if connection, false otherwise
	 */
	public boolean isConnected() {
		return userSession != null? userSession.isOpen(): false;
	}
	
	/**
	 * Callback hook for Connection open events.
	 *
	 * @param userSession
	 *            the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(Session userSession) {
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
			this.messageHandler.onMessage(this, message);
		}
	}

	/**
	 * register message handler
	 * 
	 * @param msgHandler message handler called on message
	 * @return itself
	 */
	private JKWSClient setMessageHandler(JKWSHandler msgHandler) {
		this.messageHandler = msgHandler;
		return this;
	}

	/**
	 * Send a message async
	 * @throws IOException 
	 * 
	 * @param message text message
	 * @return itself
	 */
	public JKWSClient sendMessageAsync(String message) throws IOException {
		if (this.userSession != null) {
			this.userSession.getAsyncRemote().sendText(message);
		} else {
			throw new IOException("Session not available");
		}
		return this;
	}
	
	/**
	 * Send message, synchronous
	 * 
	 * @param message text message
	 * @return itself
	 * @throws IOException 
	 *
	 */
	public JKWSClient sendMessageSync(String message) throws IOException {
		if (this.userSession != null) {
			this.userSession.getBasicRemote().sendText(message);
		} else {
			throw new IOException("Session not available");
		}
		return this;
	}
}
