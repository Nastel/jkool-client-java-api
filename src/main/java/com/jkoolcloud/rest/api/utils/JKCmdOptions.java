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

import java.io.PrintStream;

import com.jkoolcloud.rest.api.service.JKQueryAsync;

public class JKCmdOptions {
	public String query;
	public String token;
	public String usage;
	public String uri = JKQueryAsync.JKOOL_WEBSOCK_URL;
	public long waitTimeMs = 20000;
	
	public JKCmdOptions(String [] args) {
		parseOptions(args);
	}

	private void parseOptions(String[] args) {
		for (int i=0; i < args.length; i++) {
			String arg = args[i];
			if ("-a".equals(arg)) { if (i == args.length) { usage = "Must specify access token with -a"; return; } token = args[++i]; }
			if ("-q".equals(arg)) { if (i == args.length) { usage = "Must specify query with -q"; return; } query = args[++i]; }
			if ("-u".equals(arg)) { if (i == args.length) { usage = "Must specify URI with -u"; return; } uri = args[++i]; }
			if ("-w".equals(arg)) { if (i == args.length) { usage = "Must specify wait time (ms) -w"; return; } waitTimeMs = Long.parseLong(args[++i]); }
		}
		if (query == null || token == null) {
			usage = "Usage: -a token -q query [-u url] [-w wait-ms]";
			return;
		}
    }
	
	public void print() {
		print(System.out);
	}
	
	public void print(PrintStream out) {
		out.println("Options: token=" + token + ", query=\"" + query + "\", wait.ms=" + waitTimeMs + ", uri=\"" + uri + "\"");
	}
}