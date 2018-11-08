/*
 * Copyright 2014-2018 JKOOL, LLC.
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

/**
 * This is used to encapsulate JKQL query statements and implements {@link JKStatement} interface.
 * 
 * @author albert
 */
public class JKStatementImpl implements JKStatement {

	int maxRows;
	String query;
	JKQueryCallback callback;
	JKQueryAsync handle;

	protected JKStatementImpl(JKQueryAsync handle, String query, int maxRows, JKQueryCallback callb) {
		this.handle = handle;
		this.query = query;
		this.maxRows = maxRows;
		this.callback = callb;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public int getMaxRows() {
		return maxRows;
	}

	@Override
	public JKQueryCallback getCallback() {
		return callback;
	}

	@Override
	public JKQueryAsync getQueryAsync() {
		return handle;
	}

	@Override
	public JKQueryHandle call() throws IOException {
		return call(maxRows);
	}

	@Override
	public JKQueryHandle call(int maxrows) throws IOException {
		return handle.callAsync(query, maxrows, callback);
	}
}
