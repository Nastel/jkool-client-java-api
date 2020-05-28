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

import java.io.IOException;
import java.util.UUID;

/**
 * This is used to encapsulate Async JKQL query statements and implements {@link JKStatementAsync} interface.
 * 
 * @author albert
 */
public class JKStatementAsyncImpl extends JKStatementImpl implements JKStatementAsync {

	JKQueryCallback callback;

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
	public JKQueryHandle callAsync() throws IOException {
		return callAsync(maxRows);
	}

	@Override
	public JKQueryHandle callAsync(int maxrows) throws IOException {
		return ((JKQueryAsync)handle).callAsync(query, maxrows, callback);
	}
}
