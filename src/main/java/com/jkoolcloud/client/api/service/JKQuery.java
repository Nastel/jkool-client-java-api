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

import java.util.TimeZone;

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
	public static final String QAPI_CLIENT_VERSION = JKQuery.class.getClass().getPackage().getImplementationVersion();
	
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
	 * Execute a specific JKQL query
	 * 
	 * @param query
	 *            JKQL query statement
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String query) throws JKStreamException {
		return call(query, DEFAULT_MAX_ROWS);
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
		return call(query.getQuery(), query.getMaxRows());
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
		return call(query, getToken(), repoId, tz, dateRange, trace, maxRows);
	}

	/**
	 * Execute a specific JKQL query
	 * 
	 * @param _query
	 *            JKQL query statement
	 * @param _token
	 *            JKQL access token
	 * @param _repo
	 *            repo name (or null if default)
	 * @param _tz
	 *            timezone scope for the query
	 * @param _drange
	 *            time filter or range (e.g. today)
	 * @param _trace
	 *            trace mode
	 * @param _mrows
	 *            maximum rows in response
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String _query, String _token, String _repo, String _tz, String _drange, boolean _trace,
			int _mrows) throws JKStreamException {
		Form qParms = new Form();
		if (_token != null) qParms.param(JK_TOKEN_KEY, _token);
		if (_query != null) qParms.param(JK_QUERY_KEY, _query);
		if (_repo != null) qParms.param(JK_REPO_KEY, _repo);
		if (_tz != null) qParms.param(JK_TIME_ZONE_KEY, _tz);
		if (_drange != null) qParms.param(JK_DATE_KEY, _drange);
		if (_trace) qParms.param(JK_TRACE_KEY, Boolean.toString(_trace));
		if (_mrows > 0) qParms.param(JK_MAX_ROWS_KEY, Integer.toString(_mrows));

		return target.request(MediaType.APPLICATION_JSON_TYPE) //
				.header(X_API_KEY, _token) //
				.header(X_API_TOKEN, _token) //
				.header(X_API_HOSTNAME, JKUtils.VM_HOST) //
				.header(X_API_HOSTADDR, JKUtils.VM_NETADDR) //
				.header(X_API_RUNTIME, JKUtils.VM_NAME) //
				.header(X_API_VERSION, QAPI_CLIENT_VERSION) //
				.post(Entity.form(qParms));
	}
}
