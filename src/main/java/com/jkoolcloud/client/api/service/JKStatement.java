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
 * This interface defines a JKQL statement. All concrete statement implementations must implement this interface.
 * 
 * @author albert
 */
public interface JKStatement {
	/**
	 * Obtain query associated with this statement
	 * 
	 * @return query associated with this statement
	 */
	String getQuery();

	/**
	 * Obtain max rows limit for returned responses
	 * 
	 * @return max rows limit
	 */
	int getMaxRows();

	/**
	 * Obtain query timezone
	 * 
	 * @return query timezone
	 */
	String getTimeZone();

	/**
	 * Obtain query default date range
	 * 
	 * @return query date range
	 */
	String getDateRange();
	
	/**
	 * Obtain query repository id
	 * 
	 * @return query repository id (null if default)
	 */
	String getRepoId();

	/**
	 * Get trace flag for this handle
	 * 
	 * @return true if trace enabled, false otherwise
	 */
	boolean isTrace();

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
	JKQueryHandle call() throws IOException;

	/**
	 * Call current statement with responses routed to the associated callback.
	 * 
	 * @param maxRows
	 *            maximum rows limit in response
	 * @throws IOException
	 *             when IO errors occur
	 * @return query handle associate with this query
	 */
	JKQueryHandle call(int maxRows) throws IOException;
}
