/*
 * Copyright 2014-2023 JKOOL, LLC.
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

import java.util.TimeZone;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jkoolcloud.client.api.utils.JKUtils;

/**
 * This class defines a RESTFul way to run JKQL queries. Supports standard queries only (does not support subscriptions)
 * 
 * @author albert
 */
public class JKQuery extends JKService {
	public static final String QAPI_CLIENT_VERSION = JKQuery.class.getPackage().getImplementationVersion();

	boolean trace = false;
	String repoId = null;
	String dateRange = "today";
	String tz = TimeZone.getDefault().getID();

	/**
	 * Create a jKool query service end-point with default end-point and access token
	 * 
	 */
	public JKQuery() {
		super(JKOOL_QUERY_URL, JKOOL_TOKEN);
	}

	/**
	 * Create a jKool query service end-point with default end-point and access token
	 * 
	 * @param token
	 *            security access token
	 */
	public JKQuery(String token) {
		super(JKOOL_QUERY_URL, token);
	}

	/**
	 * Create a jKool query service end-point with default end-point and access token
	 * 
	 * @param endPoint
	 *            URL end-point
	 * @param token
	 *            security access token
	 */
	public JKQuery(String endPoint, String token) {
		super(endPoint, token);
	}

	/**
	 * Obtain query timezone
	 * 
	 * @return query timezone
	 */
	public String getTimeZone() {
		return tz;
	}

	/**
	 * Obtain query default date range
	 * 
	 * @return query date range
	 */
	public String getDateRange() {
		return dateRange;
	}

	/**
	 * Obtain query repository id
	 * 
	 * @return query repository id (null if default)
	 */
	public String getRepoId() {
		return repoId;
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
	 * Set default query timezone
	 * 
	 * @param tzone
	 *            timezone name (e.g. UTC)
	 * @return self
	 */
	public JKQuery setTimeZone(String tzone) {
		tz = tzone;
		return this;
	}

	/**
	 * Set default query timezone
	 * 
	 * @param tzone
	 *            timezone
	 * @return self
	 */
	public JKQuery setTimeZone(TimeZone tzone) {
		tz = tzone.getID();
		return this;
	}

	/**
	 * Set default date range for queries
	 * 
	 * @param dfilter
	 *            date range (e.g. today)
	 * @return self
	 */
	public JKQuery setDateFilter(String dfilter) {
		dateRange = dfilter;
		return this;
	}

	/**
	 * Set repository id to use for queries (provided access token has permission to read specified repository)
	 * 
	 * @param repo
	 *            repository id or null for default.
	 * @return self
	 */
	public JKQuery setRepoId(String repo) {
		repoId = repo;
		return this;
	}

	/**
	 * Set trace mode
	 * 
	 * @param flag
	 *            enable or disable trace
	 * @return self
	 */
	public JKQuery setTrace(boolean flag) {
		this.trace = flag;
		return this;
	}

	/**
	 * Prepare a JQKL query statement with default max rows
	 * 
	 * @param query
	 *            JKQL query
	 * @return {@link JKStatement} instance
	 */
	public JKStatement prepare(String query) {
		return new JKStatementImpl(this, query, DEFAULT_MAX_ROWS);
	}

	/**
	 * Prepare a JQKL query statement
	 * 
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            maximum rows in the response result
	 * @return {@link JKStatement} instance
	 */
	public JKStatement prepare(String query, int maxRows) {
		return new JKStatementImpl(this, query, maxRows);
	}

	/**
	 * Prepare a JQKL query statement
	 * 
	 * @param id
	 *            JKQL query id
	 * @param query
	 *            JKQL query
	 * @param maxRows
	 *            maximum rows in the response result
	 * @return {@link JKStatement} instance
	 */
	public JKStatement prepare(String id, String query, int maxRows) {
		return new JKStatementImpl(this, id, query, maxRows);
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String query) throws JKStreamException {
		return call(prepare(UUID.randomUUID().toString(), query, DEFAULT_MAX_ROWS));
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @param maxRows
	 *            maximum rows in response
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String query, int maxRows) throws JKStreamException {
		return call(prepare(UUID.randomUUID().toString(), query, maxRows));
	}

	/**
	 * Execute a specific JKQL query.
	 * 
	 * @param qid
	 *            JKQL query request id
	 * @param query
	 *            JKQL query statement
	 * @param maxRows
	 *            maximum rows in response
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String qid, String query, int maxRows) throws JKStreamException {
		return call(prepare(qid, query, maxRows));
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(JKStatement query) throws JKStreamException {
		if (isEmpty(query.getQuery())) {
			throw new JKStreamException("Call has no query defined");
		}

		Form qParms = new Form();
		if (!isEmpty(getToken())) {
			qParms.param(JK_TOKEN_KEY, getToken());
		}
		qParms.param(JK_QUERY_KEY, query.getQuery());
		qParms.param(JK_REPO_KEY, !isEmpty(query.getRepoId()) ? query.getRepoId() : repoId);
		qParms.param(JK_TIME_ZONE_KEY, !isEmpty(query.getTimeZone()) ? query.getTimeZone() : tz);
		qParms.param(JK_DATE_KEY, !isEmpty(query.getDateRange()) ? query.getDateRange() : dateRange);
		qParms.param(JK_TRACE_KEY, Boolean.toString(query.isTrace()));
		qParms.param(JK_MAX_ROWS_KEY, Integer.toString(query.getMaxRows() > 0 ? query.getMaxRows() : DEFAULT_MAX_ROWS));
		qParms.param(JK_SUBID_KEY, !isEmpty(query.getId()) ? query.getId() : UUID.randomUUID().toString());

		return target.request(MediaType.APPLICATION_JSON_TYPE) //
				.header(X_REFERER, query.getReferrer()) //
				.header(X_API_KEY, getToken()) //
				.header(X_API_TOKEN, getToken()) //
				.header(X_API_HOSTNAME, JKUtils.VM_HOST) //
				.header(X_API_HOSTADDR, JKUtils.VM_NETADDR) //
				.header(X_API_RUNTIME, JKUtils.VM_NAME) //
				.header(X_API_VERSION, QAPI_CLIENT_VERSION) //
				.post(Entity.form(qParms));
	}

	/**
	 * Checks if string is empty.
	 * 
	 * @param str
	 *            string to check
	 * @return {@code true} if string has meaningful value, {@code false} - otherwise
	 */
	protected static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
}
