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

import java.net.URLEncoder;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

/**
 * This class defines a RESTFul way to run jKool queries. Supports standard queries only (does not support
 * subscriptions)
 * 
 * @author albert
 */
public class JKQuery extends JKService {
	HttpClient httpClient = HttpClients.createDefault();

	boolean trace = false;
	String repoId = "";
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
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public HttpResponse get(String query) throws JKStreamException {
		return get(query, DEFAULT_MAX_ROWS);
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
	public HttpResponse get(JKStatement query) throws JKStreamException {
		return get(query.getQuery(), query.getMaxRows());
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
	 * @param _dfilter
	 *            time filter or range (e.g. today)
	 * @param _trace
	 *            trace mode
	 * @param _maxRows
	 *            maximum rows in response
	 * @return object containing JSON response
	 * @throws JKStreamException
	 *             if error occurs during a call
	 */
	public Response call(String _query, String _token, String _repo, String _tz, String _dfilter, boolean _trace, int _maxRows) throws JKStreamException {
		target = target.queryParam(JK_QUERY_KEY, _query)
				.queryParam(JK_TOKEN_KEY, _token)
				.queryParam(JK_TIME_ZONE_KEY, _tz)
				.queryParam(JK_DATE_KEY, _dfilter)
				.queryParam(JK_REPO_KEY, _repo)
				.queryParam(JK_TRACE_KEY, _trace)
				.queryParam(JK_MAX_ROWS_KEY, _maxRows);

		return target.request(MediaType.APPLICATION_JSON).header(JK_TOKEN_KEY, _token).get();
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
	public HttpResponse get(String query, int maxRows) throws JKStreamException {
		try {
			String urlQuery = JK_QUERY_KEY + "=" + URLEncoder.encode(query, "UTF-8") //
					+ "&" + JK_TOKEN_KEY + "=" + getToken() //
					+ "&" + JK_TIME_ZONE_KEY + "=" + tz //
					+ "&" + JK_DATE_KEY + "=" + dateRange //
					+ "&" + JK_REPO_KEY + "=" + repoId //
					+ "&" + JK_TRACE_KEY + "=" + trace //
					+ "&" + JK_MAX_ROWS_KEY + "=" + maxRows;
			HttpGet request = new HttpGet(getServiceUrl() + "?" + urlQuery);
			// optionally, token can be in the header.
			request.addHeader(JK_TOKEN_KEY, getToken());
			request.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON);
			HttpResponse response = httpClient.execute(request);
			return response;
		} catch (Throwable e) {
			throw new JKStreamException(300, "Failed: path=" + getServiceUrl() + ", query=" + query, e);
		}
	}
}
