package com.jkoolcloud.rest.api.service;

import javax.websocket.CloseReason;

public interface JKConnectionHandler {
	void open(JKQueryAsync async);
	void error(JKQueryAsync async, Throwable ex);
	void close(JKQueryAsync async, CloseReason reason);
}
