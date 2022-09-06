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

import java.io.IOException;
import java.util.UUID;

import com.jkoolcloud.client.api.utils.JKUtils;

import jakarta.ws.rs.core.Response;

/**
 * This is used to encapsulate JKQL query statements and implements {@link JKStatement} interface.
 * 
 * @author albert
 */
public class JKStatementImpl implements JKStatement {

	int maxRows = 100;
	boolean trace = false;
	String referrer = JKUtils.VM_NETADDR;
	String query, id;
	JKQuery handle;
	boolean subscribe;
	final long timeCreated;

	protected JKStatementImpl() {
		timeCreated = System.currentTimeMillis();
	}

	protected JKStatementImpl(JKQuery handle, String query, int maxRows) {
		this(handle, UUID.randomUUID().toString(), query, maxRows);
	}

	protected JKStatementImpl(JKQuery handle, String id, String query, int maxRows) {
		timeCreated = System.currentTimeMillis();
		this.handle = handle;
		this.id = id;
		this.query = query;
		this.maxRows = maxRows;
		this.subscribe = JKUtils.isSubscribeQ(query);
		this.trace = handle.isTrace();
	}

	@Override
	public long getTimeCreated() {
		return timeCreated;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()
				+ " {id: \"" + getId()
				+ "\", handle: \"" + getJKQuery()
				+ "\", query: \"" + getQuery()
				+ "\", time.zone: \"" + getTimeZone()
				+ "\", date.range: \"" + getDateRange()
				+ "\", referrer: \"" + getReferrer()
				+ "\", repo.id: \"" + getRepoId()
				+ "\", trace: \"" + isTrace()
				+ "\", max.rows: \"" + getMaxRows()
				+ "\"}";
	}

	@Override
	public boolean isSubscribeId() {
		return getId().startsWith(JKQIConstants.JK_SUB_UUID_PREFIX);
	}

	@Override
	public boolean isSubscribe() {
		return subscribe;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getMaxRows() {
		return maxRows;
	}

	@Override
	public String getTimeZone() {
		return handle.getTimeZone();
	}

	@Override
	public String getDateRange() {
		return handle.getDateRange();
	}

	@Override
	public String getRepoId() {
		return handle.getRepoId();
	}

	@Override
	public boolean isTrace() {
		return trace;
	}

	@Override
	public JKQuery getJKQuery() {
		return handle;
	}

	@Override
	public String getReferrer() {
		return referrer;
	}

	@Override
	public JKStatement setReferrer(String ref) {
		this.referrer = ref;
		return this;
	}

	@Override
	public JKStatement setTrace(boolean flag) {
		this.trace = flag;
		return this;
	}

	@Override
	public Response call() throws IOException, JKStreamException {
		return handle.call(this);
	}

	@Override
	public JKStatement setMaxRows(int mrows) {
		this.maxRows = mrows;
		return this;
	}

	@Override
	public boolean isConnected() {
		return handle.isConnected();
	}
}
