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

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.json.JsonObject;

/**
 * This class implements a query handle which encapsulates asynchronous subscription for query and callback pair.
 * 
 * @author albert
 */
public class JKQueryHandle implements JKQIConstants {
	public final static String DEFAULT_DATE_RANGE = "today";
	public final static String DEFAULT_REPO = "";

	final JKStatementAsync query;
	final boolean subscribe;
	final long timeCreated;

	int maxRows = 100;
	boolean done = false, trace = false;

	private final ReentrantLock aLock = new ReentrantLock();
	private final Condition calledBack = aLock.newCondition();
	private final Condition doneCall = aLock.newCondition();
	private final AtomicLong callCount = new AtomicLong(0);

	/**
	 * Create a jKool query handle
	 * 
	 * @param stmt
	 *            JKQL query statement
	 * @param callback
	 *            associated with the given query
	 */
	public JKQueryHandle(JKStatementAsync stmt) {
		this.timeCreated = System.currentTimeMillis();
		this.query = stmt;
		this.trace = query.isTrace();
		this.subscribe = isSubscribeQ(query.getQuery());
	}

	/**
	 * Create a unique identifier
	 * 
	 * @return a unique identifier
	 */
	public static String newId() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	/**
	 * Create a unique identifier for a given query
	 * 
	 * @param q
	 *            JKQL query
	 * @return a unique identifier
	 */
	public static String newId(String q) {
		String uuid = UUID.randomUUID().toString();
		uuid = isSubscribeQ(q) ? JK_SUB_UUID_PREFIX + uuid : uuid;
		return uuid;
	}

	/**
	 * Determine if a query represents a subscription query
	 * 
	 * @param query
	 *            JKQL query
	 * @return true if subscribe query, false otherwise
	 */
	public static boolean isSubscribeQ(String query) {
		return query.toLowerCase().startsWith(JK_SUB_QUERY_PREFIX) 
				|| query.toLowerCase().startsWith(JK_SUB_QUERY_PREFIX2);
	}

	/**
	 * Determine if a handle id represents a subscription query
	 * 
	 * @param id
	 *            JKQL query id
	 * @return true if subscribe query, false otherwise
	 */
	public static boolean isSubscribeId(String id) {
		return id.startsWith(JK_SUB_UUID_PREFIX);
	}

	/**
	 * Determine if current handle id represents a subscription query
	 * 
	 * @return true if subscribe query, false otherwise
	 */
	public boolean isSubscribeId() {
		return query.getId().startsWith(JK_SUB_UUID_PREFIX);
	}

	/**
	 * Determine if current handle query represents a subscription query
	 * 
	 * @return true if subscribe query, false otherwise
	 */
	public boolean isSubscribeQuery() {
		return subscribe;
	}

	/**
	 * Determine if current handle is DONE -- meaning all responses for this query have been received and consumed.
	 * 
	 * @return true if handle is done, false otherwise
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Obtain query associated with the current handle
	 * 
	 * @return JKQL query statement
	 */
	public String getQuery() {
		return query.getQuery();
	}

	/**
	 * Obtain query associated with the current handle
	 * 
	 * @return JKQL query {@link JKStatementAsync}
	 */
	public JKStatementAsync getQueryStatement() {
		return query;
	}

	/**
	 * Obtain query TimeZone
	 * 
	 * @return JKQL query TimeZone
	 */
	public String getTimeZone() {
		return query.getTimeZone();
	}

	/**
	 * Obtain query default date range
	 * 
	 * @return JKQL query date range
	 */
	public String getDateRange() {
		return query.getDateRange();
	}

	/**
	 * Obtain query repository id
	 * 
	 * @return JKQL query repository id (null if default)
	 */
	public String getRepoId() {
		return query.getRepoId();
	}

	/**
	 * Obtain referrer name
	 * 
	 * @return name of the query referrer
	 */
	public String getReferrer() {
		return query.getReferrer();
	}

	/**
	 * Obtain query callback associated with this handle
	 * 
	 * @return JKQL query callback handle
	 */
	public JKQueryCallback getCallback() {
		return query.getCallback();
	}

	/**
	 * Obtain handle identifier
	 * 
	 * @return handle identifier
	 */
	public String getId() {
		return query.getId();
	}

	/**
	 * Obtain handle create time
	 * 
	 * @return handle create time
	 */
	public long getTimeCreated() {
		return timeCreated;
	}

	/**
	 * Set maximum rows for query response
	 * 
	 * @param rows
	 *            maximum rows in response
	 * @return self
	 */
	public JKQueryHandle setMaxRows(int rows) {
		this.maxRows = rows;
		return this;
	}

	/**
	 * Get maximum rows for query response
	 * 
	 * @return rows maximum rows in response
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * Set trace flag
	 * 
	 * @param flag
	 *            enable or disable trace
	 * @return self
	 */
	public JKQueryHandle setTrace(boolean flag) {
		this.trace = flag;
		return this;
	}

	/**
	 * Get trace flag for this handle
	 * 
	 * @return true if trace enabled, false otherwise
	 */
	public boolean isTrace() {
		return trace;
	}

	/**
	 * Await for response until a given date/time
	 * 
	 * @param until
	 *            date/time until to await for response
	 * @return false if the deadline has elapsed upon return, else true
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	public boolean awaitOnCallbackUntil(Date until) throws InterruptedException {
		aLock.lock();
		try {
			return calledBack.awaitUntil(until);
		} finally {
			aLock.unlock();
		}
	}

	/**
	 * Await for response until indefinitely or interrupted
	 * 
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	public void awaitOnCallback() throws InterruptedException {
		aLock.lock();
		try {
			calledBack.await();
		} finally {
			aLock.unlock();
		}
	}

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
	public boolean awaitOnCallback(long time, TimeUnit unit) throws InterruptedException {
		aLock.lock();
		try {
			return calledBack.await(time, unit);
		} finally {
			aLock.unlock();
		}
	}

	/**
	 * Await for completion until a given date/time
	 * 
	 * @param until
	 *            date/time until to await for completion
	 * @return false if the deadline has elapsed upon return, else true
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	public boolean awaitOnDeadUntil(Date until) throws InterruptedException {
		aLock.lock();
		try {
			return doneCall.awaitUntil(until);
		} finally {
			aLock.unlock();
		}
	}

	/**
	 * Await for completion until indefinitely or interrupted
	 * 
	 * @throws InterruptedException
	 *             if connection is interrupted
	 */
	public void awaitOnDone() throws InterruptedException {
		aLock.lock();
		try {
			doneCall.await();
		} finally {
			aLock.unlock();
		}
	}

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
	public boolean awaitOnDone(long time, TimeUnit unit) throws InterruptedException {
		aLock.lock();
		try {
			return doneCall.await(time, unit);
		} finally {
			aLock.unlock();
		}
	}

	/**
	 * Get total number of times the callback was called
	 * 
	 * @return number of times the callback was called
	 */
	public long getCallCount() {
		return callCount.get();
	}

	/**
	 * Reset total number of times the callback was called
	 * 
	 */
	public void resetCallCount() {
		callCount.set(0);
	}

	@Override
	public int hashCode() {
		return query.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JKQueryHandle) {
			JKQueryHandle q2 = (JKQueryHandle) obj;
			return query.getId().equals(q2.query.getId());
		} else if (obj instanceof JKQueryCallback) {
			return this.query.getCallback() == (JKQueryCallback) obj;
		} else if (obj instanceof String) {
			return query.getId().equals(String.valueOf(obj));
		}
		return false;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {" //
				+ "id: \"" + query.getId() //
				+ "\", query: \"" + query.getQuery() //
				+ "\", timezone: \"" + query.getTimeZone() //
				+ "\", daterange: \"" + query.getDateRange() //
				+ "\", repo: \"" + query.getRepoId() //
				+ "\", referrer: \"" + query.getReferrer() //
				+ "\", trace: \"" + query.isTrace() //
				+ "\", callback: \"" + query.getCallback() + "\"}";
	}

	protected void done() {
		aLock.lock();
		try {
			done = true;
			query.getCallback().done(this);
			doneCall.signalAll();
		} finally {
			aLock.unlock();
		}
	}

	protected void handle(JsonObject response, Throwable ex) {
		aLock.lock();
		try {
			callCount.incrementAndGet();
			query.getCallback().handle(this, response, ex);
			calledBack.signalAll();
		} finally {
			aLock.unlock();
		}
	}
}
