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
package com.jkoolcloud.client.api.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.Properties;
import java.util.TimeZone;

import com.jkoolcloud.client.api.service.JKQueryAsync;
import com.jkoolcloud.client.api.service.JKQueryConstants;

public class JKCmdOptions {
	public static final String PROP_URI = "uri";
	public static final String PROP_TOKEN = "token";
	public static final String PROP_TZ = "timezone";
	public static final String PROP_DATE_RANGE = "daterange";
	public static final String PROP_REPO_NAME = "repo";
	public static final String PROP_QUERY = "query";
	public static final String PROP_TRACE = "trace";
	public static final String PROP_SEARCH = "search";
	public static final String PROP_JPATH = "jpath";
	public static final String PROP_FILE = "file";
	public static final String PROP_WAIT = "wait";
	public static final String PROP_RETRY = "retry";
	public static final String PROP_MAX_ROWS = "maxrows";

	public static final String OPTION_URI = "-uri";
	public static final String OPTION_TOKEN = "-token";
	public static final String OPTION_QUERY = "-query";
	public static final String OPTION_TRACE = "-trace";
	public static final String OPTION_SEARCH = "-search";
	public static final String OPTION_JPATH = "-jpath";
	public static final String OPTION_FILE = "-file";
	public static final String OPTION_TZ = "-tz";
	public static final String OPTION_DRANGE = "-drange";
	public static final String OPTION_REPO = "-repo";
	public static final String OPTION_WAIT = "-wait";
	public static final String OPTION_RETRY = "-retry";
	public static final String OPTION_MAX_ROWS = "-maxrows";

	public static final String DEFAULT_CMD_NAME = "class";
	public static final String USAGE_TEXT = "%s options:\n\t"
			+ "-token access-token\n\t"
			+ "-query query-statement\n\t"
			+ "[-file args-file]\n\t"
			+ "[-uri query service uri]\n\t"
			+ "[-jpath json-path]\n\t"
			+ "[-search search-text]\n\t"
			+ "[-wait wait-ms]\n\t"
			+ "[-retry retry-ms]\n\t"
			+ "[-maxrows max-rows]\n\t"
			+ "[-repo repo-name]\n\t"
			+ "[-tz timezone]\n\t"
			+ "[-drange date-range]\n\t"
			+ "[-trace true|false]";

	public String query;
	public String search;
	public String token;
	public String timezone = TimeZone.getDefault().getID();
	public String daterange = "today";
	public String reponame = "";
	public String usage;
	public String json_path;
	public String appname = DEFAULT_CMD_NAME;
	public String uri = JKQueryAsync.JKOOL_WEBSOCK_URL;

	public boolean trace = false;
	public int maxRows = JKQueryConstants.DEFAULT_MAX_ROWS;
	public long waitTimeMs = JKQueryConstants.DEFAULT_WAIT_TIME;
	public long retryTimeMs = JKQueryConstants.DEFAULT_RETRY_TIME;

	public JKCmdOptions(String[] args) {
		parseOptions(args);
	}

	public JKCmdOptions(String[] args, Properties defProps) {
		assignDefaults(defProps);
		parseOptions(args);
	}

	public JKCmdOptions(String appName, String[] args) {
		appname = appName;
		parseOptions(args);
	}

	public JKCmdOptions(String appName, String[] args, Properties defProps) {
		appname = appName;
		assignDefaults(defProps);
		parseOptions(args);
	}

	public JKCmdOptions(Class<?> clazz, String[] args) {
		appname = clazz.getSimpleName();
		parseOptions(args);
	}

	public JKCmdOptions(Class<?> clazz, String[] args, Properties defProps) {
		appname = clazz.getSimpleName();
		assignDefaults(defProps);
		parseOptions(args);
	}

	public String getUsage() {
		return String.format(USAGE_TEXT, appname);
	}

	private void assignDefaults(Properties props) {
		uri = props.getProperty(PROP_URI, uri);
		token = props.getProperty(PROP_TOKEN, token);
		query = props.getProperty(PROP_QUERY, query);
		timezone = props.getProperty(PROP_TZ, timezone);
		daterange = props.getProperty(PROP_DATE_RANGE, daterange);
		reponame = props.getProperty(PROP_REPO_NAME, reponame);
		json_path = props.getProperty(PROP_JPATH, json_path);
		search = props.getProperty(PROP_SEARCH, search);
		if (search != null) {
			query = String.format(JKQueryConstants.JK_SEARCH_QUERY_PREFIX, search);
		}
		waitTimeMs = Long.parseLong(props.getProperty(PROP_WAIT, String.valueOf(waitTimeMs)));
		retryTimeMs = Long.parseLong(props.getProperty(PROP_RETRY, String.valueOf(retryTimeMs)));
		maxRows = Integer.parseInt(props.getProperty(PROP_MAX_ROWS, String.valueOf(maxRows)));
		trace = Boolean.parseBoolean(props.getProperty(PROP_TRACE, String.valueOf(trace)));
	}

	private void processOptions(String[] args) throws FileNotFoundException, IOException {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (OPTION_TOKEN.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify access token with " + arg;
					return;
				}
				token = args[++i];
			} else if (OPTION_QUERY.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify valid query with " + arg;
					return;
				}
				query = args[++i];
			} else if (OPTION_JPATH.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify json path with " + arg;
					return;
				}
				json_path = args[++i];
			} else if (OPTION_SEARCH.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify search text with " + arg;
					return;
				}
				search = args[++i];
				query = String.format(JKQueryConstants.JK_SEARCH_QUERY_PREFIX, search);
			} else if (OPTION_URI.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify URI with " + arg;
					return;
				}
				uri = args[++i];
				URI.create(uri); // test URI for correctness
			} else if (OPTION_FILE.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify file name with " + arg;
					return;
				}
				loadProperties(args[++i]);
			} else if (OPTION_TZ.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify timezone with " + arg;
					return;
				}
				this.timezone = args[++i];
			} else if (OPTION_DRANGE.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify date range with " + arg;
					return;
				}
				this.daterange = args[++i];
			} else if (OPTION_REPO.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify repo name with " + arg;
					return;
				}
				this.reponame = args[++i];
			} else if (OPTION_WAIT.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify wait time (ms) with " + arg;
					return;
				}
				waitTimeMs = Long.parseLong(args[++i]);
				if (waitTimeMs <= 0) {
					usage = "Must be a positive number with " + arg;
					return;
				}
			} else if (OPTION_RETRY.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify retry time (ms) with " + arg;
					return;
				}
				retryTimeMs = Long.parseLong(args[++i]);
				if (retryTimeMs <= 0) {
					usage = "Must be a positive number with " + arg;
					return;
				}
			} else if (OPTION_MAX_ROWS.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify maximum rows with " + arg;
					return;
				}
				maxRows = Integer.parseInt(args[++i]);
				if (maxRows <= 0) {
					usage = "Must be a positive number with " + arg;
					return;
				}
			} else if (OPTION_TRACE.equals(arg)) {
				if ((i + 1) == args.length) {
					usage = "Must specify true|false with " + arg;
					return;
				}
				trace = Boolean.parseBoolean((args[++i]));
			} else {
				throw new IllegalArgumentException("Unknown option: " + arg);
			}
		}
		if (query == null || token == null || uri == null) {
			usage = String.format(USAGE_TEXT, appname);
			if (uri == null) {
				usage += "\nMissing option: " + OPTION_URI;
			}
			if (token == null) {
				usage += "\nMissing option: " + OPTION_TOKEN;
			}
			if (query == null) {
				usage += "\nMissing option: " + OPTION_QUERY;
			}
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
		assignDefaults(props);
	}

	public void print() {
		print(System.out);
	}

	public void print(PrintStream out) {
		out.println(this.toString());
	}

	@Override
	public String toString() {
		String formatted = String.format("%s: uri=\"%s\", "
				+ "query=\"%s\", tz=\"%s\", date-range=\"%s\", repo=\"%s\", wait.ms=%d, retry.ms=%d, max.rows=%d, trace=%b",
				appname, uri, query, timezone, daterange, reponame, waitTimeMs, retryTimeMs, maxRows, trace);
		return formatted;
	}
}