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
package com.jkoolcloud.rest.api.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import com.jkoolcloud.rest.api.service.JKQueryAsync;
import com.jkoolcloud.rest.api.service.JKQueryConstants;

public class JKCmdOptions {
	public static final String OPTION_TOKEN = "-token";
	public static final String OPTION_URI = "-uri";
	public static final String OPTION_QUERY = "-query";
	public static final String OPTION_TRACE = "-trace";
	public static final String OPTION_SEARCH = "-search";
	public static final String OPTION_JPATH = "-jpath";
	public static final String OPTION_FILE = "-file";
	public static final String OPTION_WAIT = "-wait";
	public static final String OPTION_RETRY = "-retry";
	public static final String OPTION_ROWS = "-rows";
	
	public static final String DEFAULT_CMD_NAME = "class";
	public static final String USAGE_TEXT = "%s options:\n\t"
			+ "-token access-token\n\t"
			+ "-query jkql-query\n\t"
			+ "[-file args-file]\n\t"
			+ "[-uri jkool service uri]\n\t"
			+ "[-jpath json-path]\n\t"
			+ "[-search search-text]\n\t"
			+ "[-wait wait-ms]\n\t"
			+ "[-retry retry-ms]\n\t"
			+ "[-rows max-rows]\n\t"
			+ "[-trace true|false]";
	
	public String query;
	public String search;
	public String token;
	public String usage;
	public String json_path;
	public String appname = DEFAULT_CMD_NAME;
	public String uri = JKQueryAsync.JKOOL_WEBSOCK_URL;
	public boolean trace = false;
	public long waitTimeMs = 20000;
	public long retryTimeMs = 0;
	public int maxRows = 50;

	public JKCmdOptions(String[] args) {
		parseOptions(args);
	}

	public JKCmdOptions(String appName, String[] args) {
		appname = appName;
		parseOptions(args);
	}

	public JKCmdOptions(Class<?> clazz, String[] args) {
		appname = clazz.getSimpleName();
		parseOptions(args);
	}

	public String getUsage() {
		return String.format(USAGE_TEXT, appname);
	}
	
	private void processOptions(String[] args) throws FileNotFoundException, IOException {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (OPTION_TOKEN.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify access token with -token";
					return;
				}
				token = args[++i];
			} else if (OPTION_QUERY.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify query with -query";
					return;
				}
				query = args[++i];
			} else if (OPTION_JPATH.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify json path with -jpath";
					return;
				}
				json_path = args[++i];
			} else if (OPTION_SEARCH.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify search text with -search";
					return;
				}
				search = args[++i];
				query = String.format(JKQueryConstants.JK_SEARCH_QUERY_PREFIX, search);
			} else if (OPTION_URI.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify URI with -uri";
					return;
				}
				uri = args[++i];
			} else if (OPTION_FILE.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify file name with -file";
					return;
				}
				loadProperties(args[++i]);
			} else if (OPTION_WAIT.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify wait time (ms) -wait";
					return;
				}
				waitTimeMs = Long.parseLong(args[++i]);
			} else if (OPTION_RETRY.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify retry time (ms) -retry";
					return;
				}
				retryTimeMs = Long.parseLong(args[++i]);
			} else if (OPTION_ROWS.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify maximum rows with -rows";
					return;
				}
				maxRows = Integer.parseInt(args[++i]);
			} else if (OPTION_TRACE.equals(arg)) {
				if ((i+1) == args.length) {
					usage = "Must specify true|false with -trace";
					return;
				}
				trace = Boolean.parseBoolean((args[++i]));
			} else {
				throw new IllegalArgumentException("Unknown option: " + arg);
			}
		}		
		if (query == null || token == null || uri == null) {
			usage = String.format(USAGE_TEXT, appname);
			if (uri == null) usage += "\nMissing -uri option";
			if (token == null) usage += "\nMissing -token option";
			if (query == null) usage += "\nMissing -query option";
		}
	}
	
	private void parseOptions(String[] args) {
		try {
			processOptions(args);
		} catch (Throwable e) {
			usage = "Must specify valid arguments";
			e.printStackTrace();
		}
	}

	private void loadProperties(String file) throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(file));
		uri = props.getProperty("uri", uri);
		token = props.getProperty("token", token);
		query = props.getProperty("query", query);
		json_path = props.getProperty("get", json_path);
		search = props.getProperty("search", search);
		if (search != null) {
			query = String.format(JKQueryConstants.JK_SEARCH_QUERY_PREFIX, search);
		}
		waitTimeMs = Long.parseLong(props.getProperty("wait", String.valueOf(waitTimeMs)));
		retryTimeMs = Long.parseLong(props.getProperty("retry", String.valueOf(retryTimeMs)));
		maxRows = Integer.parseInt(props.getProperty("maxrows", String.valueOf(maxRows)));
		trace = Boolean.parseBoolean(props.getProperty("trace", String.valueOf(trace)));
	}

	public void print() {
		print(System.out);
	}

	public void print(PrintStream out) {
		out.println(this.toString());
	}

	@Override
	public String toString() {
		String formatted = String.format("%s: uri=\"%s\", " +
				"query=\"%s\", wait.ms=%d, max.rows=%d, trace=%b",
		        appname, uri, query, waitTimeMs, maxRows, trace);
		return formatted;
	}
}