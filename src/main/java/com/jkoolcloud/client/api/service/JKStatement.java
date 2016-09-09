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
package com.jkoolcloud.client.api.service;

import java.io.IOException;

/**
 * This interface defines a JKQL statement. All concrete statement implementations must implement
 * this interface.
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
	 * Obtain {@link JKQueryCallback} instance associated with the
	 * statement
	 * 
	 * @return {@link JKQueryCallback} instance
	 */
	JKQueryCallback getCallback();
	
	/**
	 * Call current statement with responses 
	 * routed to the associated callback.
	 * 
	 * @throws IOException
	 * @return query handle associate with this query
	 */
	JKQueryHandle call() throws IOException;
	
	/**
	 * Call current statement with responses 
	 * routed to the associated callback.
	 * 
	 * @param max rows limit
	 * @throws IOException
	 * @return query handle associate with this query
	 */
	JKQueryHandle call(int maxRows) throws IOException;
}
