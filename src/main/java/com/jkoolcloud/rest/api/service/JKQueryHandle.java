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

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.json.JsonObject;

/**
 * This class implements a query handle which encapsulates an async subscription for query->callback pair.
 * 
 * @author albert
 */
public class JKQueryHandle implements JKQueryCallback {
	public static final String SUB_UUID_PREFIX = "$sub/";
	public static final String SUB_QUERY_PREFIX = "subscribe to ";
	public static final String UNSUB_QUERY_PREFIX = "unsubscribe ";

	final String query, id;
	final boolean subscribe;
	final JKQueryCallback callback;

	private final ReentrantLock aLock = new ReentrantLock();
	private final Condition calledBack = aLock.newCondition();
	private AtomicLong callCount = new AtomicLong(0);

	public JKQueryHandle(String q, JKQueryCallback callback) {
		this(q, newId(q), callback);
	}

	public JKQueryHandle(String q, String id, JKQueryCallback callback) {
		this.query = q;
		this.id = id;
		this.callback = callback;
		this.subscribe = isSubscribeQ(q);
	}

	public static String newId(String q) {
		String uuid = UUID.randomUUID().toString();
		uuid = isSubscribeQ(q) ? SUB_UUID_PREFIX + uuid : uuid;
		return uuid;
	}

	public static boolean isSubscribeQ(String query) {
		return query.toLowerCase().startsWith(SUB_QUERY_PREFIX);
	}

	public static boolean isSubscribeId(String id) {
		return id.startsWith(SUB_UUID_PREFIX);
	}

	public boolean isSubscribeQuery() {
		return subscribe;
	}

	public boolean isSubscribeId() {
		return id.startsWith(SUB_UUID_PREFIX);
	}

	public String getQuery() {
		return query;
	}

	public JKQueryCallback getCallback() {
		return callback;
	}

	public String getId() {
		return id;
	}

	public boolean awaitOnCallbackUntil(Date until) throws InterruptedException {
		aLock.lock();
		try {
			return calledBack.awaitUntil(until);
		} finally {
			aLock.unlock();
		}
	}
	
	public void awaitOnCallback() throws InterruptedException {
		aLock.lock();
		try {
			calledBack.await();
		} finally {
			aLock.unlock();
		}
	}
	
	public boolean awaitOnCallback(long time, TimeUnit unit) throws InterruptedException {
		aLock.lock();
		try {
			return calledBack.await(time, unit);
		} finally {
			aLock.unlock();
		}
	}
	
	public long getCallCount() {
		return callCount.get();
	}

	public void resetCallCount() {
		callCount.set(0);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JKQueryHandle) {
			JKQueryHandle q2 = (JKQueryHandle) obj;
			return id.equals(q2.id);
		}
		return false;
	}

	@Override
	public String toString() {
		return "{" + "class: \"" + this.getClass().getName() + "\", id: \"" + id + "\", query: \"" + query
		        + "\", callback: \"" + callback + "\"}";
	}

	@Override
	public void handle(JKQueryHandle qhandle, JsonObject response, Throwable ex) {
		aLock.lock();
		try {
			callCount.incrementAndGet();
			callback.handle(qhandle, response, ex);
			calledBack.signalAll();
		} finally {
			aLock.unlock();
		}
	}
}
