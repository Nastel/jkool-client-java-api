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

import java.util.UUID;

/**
 * This is used to encapsulate JKQL query statements and implements {@link JKStatement} interface.
 * 
 * @author albert
 */
public class JKStatementImpl implements JKStatement {

	int maxRows;
	String query, id;
	JKQuery handle;

	protected JKStatementImpl() {
	}

	protected JKStatementImpl(JKQuery handle, String query, int maxRows) {
		this(handle, UUID.randomUUID().toString(), query, maxRows);
	}

	protected JKStatementImpl(JKQuery handle, String id, String query, int maxRows) {
		this.handle = handle;
		this.id = id;
		this.query = query;
		this.maxRows = maxRows;
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
		return handle.isTrace();
	}

	@Override
	public JKQuery getJKQuery() {
		return handle;
	}
}
