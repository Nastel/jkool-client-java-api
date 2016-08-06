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
package com.jkoolcloud.rest.api.service;

import java.util.Map;
import java.util.List;

public class JKApiException extends Exception {
	/**
	 * 
	 */
    private static final long serialVersionUID = 293667154457636080L;
    
	private int code = 0;
	private Map<String, List<String>> responseHeaders = null;
	private String responseBody = null;

	public JKApiException(String message) {
		super(message);
	}

	public JKApiException(int code, String message) {
		super(message);
		this.code = code;
	}

	public JKApiException(int code, String message, Throwable e) {
		super(message, e);
		this.code = code;
	}

	public JKApiException(int code, String message, Map<String, List<String>> responseHeaders, String responseBody, Throwable e) {
		super(message, e);
		this.code = code;
		this.responseHeaders = responseHeaders;
		this.responseBody = responseBody;
	}

	public int getCode() {
		return code;
	}

	/**
	 * Get the HTTP response headers.
	 */
	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	/**
	 * Get the HTTP response body.
	 */
	public String getResponseBody() {
		return responseBody;
	}
}
