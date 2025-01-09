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

import jakarta.ws.rs.core.Response;

/**
 * This interface defines a JKQL statement. All concrete statement implementations must implement this interface.
 * 
 * @author albert
 */
public interface JKStatement {
	public final static String DEFAULT_DATE_RANGE = "today";
	public final static String DEFAULT_REPO = "";

	/**
	 * Obtain statement create time
	 * 
	 * @return statement create time
	 */
	long getTimeCreated();

	/**
	 * Obtain {@link JKQuery} instance associated with the statement
	 * 
	 * @return {@link JKQuery} instance
	 */
	JKQuery getJKQuery();

	/**
	 * Obtain GUID associated with the statement
	 * 
	 * @return statement GUID
	 */
	String getId();

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
	 * Set max rows limit for returned responses
	 * 
	 * @param mrows
	 *            maximum row count
	 * @return this statement instance
	 */
	JKStatement setMaxRows(int mrows);

	/**
	 * Obtain query TimeZone
	 * 
	 * @return query TimeZone
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
	 * Set statement Referrer (original URL/IP of app issuing the statement)
	 * 
	 * @param ref
	 *            name of the referrer
	 * @return this statement instance
	 */
	JKStatement setReferrer(String ref);

	/**
	 * Obtain statement Referrer (original URL/IP of app issuing the statement)
	 * 
	 * @return URL or IP of the original referrer
	 */
	String getReferrer();

	/**
	 * Determine if current statement is associated with a valid connection
	 * 
	 * @return true if connected, false otherwise
	 */
	boolean isConnected();

	/**
	 * Determine if current statement id represents a subscription query
	 * 
	 * @return true if subscribe query, false otherwise
	 */
	boolean isSubscribeId();

	/**
	 * Determine if current statement query represents a subscription query
	 * 
	 * @return true if subscribe query, false otherwise
	 */
	public boolean isSubscribe();

	/**
	 * Get trace flag for this statement
	 * 
	 * @return true if trace enabled, false otherwise
	 */
	boolean isTrace();

	/**
	 * Set trace flag for this statement
	 * 
	 * @param flag
	 *            trace flag
	 * @return this statement instance
	 */
	JKStatement setTrace(boolean flag);

	/**
	 * Call current statement and wait for response
	 *
	 * @return query statement associate with this query
	 *
	 * @throws IOException
	 *             when IO errors occur
	 * @throws com.jkoolcloud.client.api.service.JKStreamException
	 *             if error occurs during a call
	 */
	Response call() throws IOException, JKStreamException;
}
