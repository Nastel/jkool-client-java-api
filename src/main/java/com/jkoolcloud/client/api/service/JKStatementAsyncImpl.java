/*
 * Copyright 2014-2025 JKOOL, LLC.
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

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import jakarta.json.JsonObject;

/**
 * This is used to encapsulate Async JKQL query statements and implements {@link JKStatementAsync} interface.
 * 
 * @author albert
 */
public class JKStatementAsyncImpl extends JKStatementImpl implements JKStatementAsync {

	String _msg_id; // server side subscription id
	JKQueryCallback callback;

	private final ReentrantLock aLock = new ReentrantLock();
	private final Condition calledBack = aLock.newCondition();
	private final Condition doneCall = aLock.newCondition();
	private final AtomicLong callCount = new AtomicLong(0);

	protected JKStatementAsyncImpl(JKQueryAsync handle, String query, int maxRows, JKQueryCallback callb) {
		this(handle, UUID.randomUUID().toString(), query, maxRows, callb);
	}

	protected JKStatementAsyncImpl(JKQueryAsync handle, String id, String query, int maxRows, JKQueryCallback callb) {
		super(handle, id, query, maxRows);
		this.callback = callb;
	}

	@Override
	public JKQueryCallback getCallback() {
		return callback;
	}

	@Override
	public JKQueryAsync getQueryAsync() {
		return (JKQueryAsync) handle;
	}

	@Override
	public JKStatementAsync callAsync() throws IOException {
		return callAsync(maxRows);
	}

	@Override
	public JKStatementAsync callAsync(int maxrows) throws IOException {
		return ((JKQueryAsync) handle).callAsync(query, maxrows, callback);
	}

	@Override
	public JKStatementAsync cancelAsync() throws IOException {
		return ((JKQueryAsync) handle).cancelAsync(this);
	}

	@Override
	public boolean awaitOnCallbackUntil(Date until) throws InterruptedException {
		aLock.lock();
		try {
			return calledBack.awaitUntil(until);
		} finally {
			aLock.unlock();
		}
	}

	@Override
	public void awaitOnCallback() throws InterruptedException {
		aLock.lock();
		try {
			calledBack.await();
		} finally {
			aLock.unlock();
		}
	}

	@Override
	public boolean awaitOnCallback(long time, TimeUnit unit) throws InterruptedException {
		aLock.lock();
		try {
			return calledBack.await(time, unit);
		} finally {
			aLock.unlock();
		}
	}

	@Override
	public boolean awaitOnDoneUntil(Date until) throws InterruptedException {
		aLock.lock();
		try {
			return doneCall.awaitUntil(until);
		} finally {
			aLock.unlock();
		}
	}

	@Override
	public void awaitOnDone() throws InterruptedException {
		aLock.lock();
		try {
			doneCall.await();
		} finally {
			aLock.unlock();
		}
	}

	@Override
	public boolean awaitOnDone(long time, TimeUnit unit) throws InterruptedException {
		aLock.lock();
		try {
			return doneCall.await(time, unit);
		} finally {
			aLock.unlock();
		}
	}

	@Override
	public long getCallCount() {
		return callCount.get();
	}

	@Override
	public void resetStats() {
		callCount.set(0);
	}

	@Override
	public String getLastMsgId() {
		return this._msg_id != null ? this._msg_id : this.getId();
	}

	@Override
	public void close() throws IOException {
		this.getQueryAsync().close(this);
		this.onClose();
	}

	protected void onDone() {
		aLock.lock();
		try {
			callback.onDone(this);
			doneCall.signalAll();
		} finally {
			aLock.unlock();
		}
	}

	protected void onClose() {
		aLock.lock();
		try {
			callback.onClose(this);
			doneCall.signalAll();
		} finally {
			aLock.unlock();
		}
	}

	protected void onResponse(JsonObject response, Throwable ex) {
		aLock.lock();
		try {
			callCount.incrementAndGet();
			String msgId = response.getString(JKQueryAsync.JK_MSGID_KEY, null);
			setLastMsgId(msgId != null ? msgId : getId());
			callback.onResponse(this, response, ex);
			calledBack.signalAll();
		} finally {
			aLock.unlock();
		}
	}

	private JKStatementAsync setLastMsgId(String id) {
		this._msg_id = id;
		return this;
	}
}
