/*
 * Copyright 2014-2021 JKOOL, LLC.
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

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This interface defines a JKQL statement supporting async calls. 
 * All concrete statement implementations must implement this interface.
 * 
 * @author albert
 */
public interface JKStatementAsync extends JKStatement, Closeable, AutoCloseable {
	/**
	 * Obtain {@link JKQueryCallback} instance associated with the statement
	 * 
	 * @return {@link JKQueryCallback} instance
	 */
	JKQueryCallback getCallback();

	/**
	 * Obtain {@link JKQueryAsync} instance associated with the statement
	 * 
	 * @return {@link JKQueryAsync} instance
	 */
	JKQueryAsync getQueryAsync();

	/**
	 * Call current statement with responses routed to the associated callback.
	 * 
	 * @throws IOException
	 *             when IO errors occur
	 * @return query handle associate with this query
	 */
	JKStatementAsync callAsync() throws IOException;
	
	/**
	 * Call current statement with responses routed to the associated callback.
	 * 
	 * @param maxRows maximum number 
	 * @throws IOException
	 *             when IO errors occur
	 * @return query handle associate with this query
	 */
	JKStatementAsync callAsync(int maxRows) throws IOException;
	
	/**
	 * Cancel a live subscription
	 * 
	 * @return query handle associated with subscription
	 * @throws IOException
	 *             on error during IO
	 */
	JKStatementAsync cancelAsync() throws IOException;

	/**
	 * Obtain last msg id associated with the handle. Only
	 * available after first response on this handle is received.
	 * 
	 * @return msg id of last response
	 */
	String getLastMsgId();
	
	/**
	 * Get total number of times the callback was called
	 * 
	 * @return number of times the callback was called
	 */
	long getCallCount();
	/**
	 * Reset total number of times the callback was called
	 * 
	 */
	void resetStats();	

	/**
	 * Await for response until a given date/time
	 * 
	 * @param until
	 *            date/time until to await for response
	 * @return false if the deadline has elapsed upon return, else true
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	boolean awaitOnCallbackUntil(Date until) throws InterruptedException;

	/**
	 * Await for response until indefinitely or interrupted
	 * 
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	void awaitOnCallback() throws InterruptedException;

	/**
	 * Await for response for a given period of time
	 * 
	 * @param time
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the time argument
	 * @return false if the deadline has elapsed upon return, else true
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	boolean awaitOnCallback(long time, TimeUnit unit) throws InterruptedException;

	/**
	 * Await for completion until a given date/time
	 * 
	 * @param until
	 *            date/time until to await for completion
	 * @return false if the deadline has elapsed upon return, else true
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	boolean awaitOnDoneUntil(Date until) throws InterruptedException;

	/**
	 * Await for completion until indefinitely or interrupted
	 * 
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	void awaitOnDone() throws InterruptedException;

	/**
	 * Await for completion for a given period of time
	 * 
	 * @param time
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the time argument
	 * @return false if the deadline has elapsed upon return, else true
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	boolean awaitOnDone(long time, TimeUnit unit) throws InterruptedException;	
}
