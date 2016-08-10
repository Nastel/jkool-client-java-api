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

import java.util.UUID;

public class JKQueryHandle {
	public static final String SUB_UUID_PREFIX = "$sub/";
	public static final String SUB_QUERY_PREFIX = "subscribe to ";
	public static final String UNSUB_QUERY_PREFIX = "unsubsribe ";

	final String query, id;
	final boolean subscribe;
	final JKQueryCallback callback;
	
	public JKQueryHandle(String q, JKQueryCallback callback) {
		this(q, newId(q), callback);
	}
	
	public JKQueryHandle(String q, String id, JKQueryCallback callback) {
		this.query = q;
		this.id = id;
		this.callback = callback;
		this.subscribe = isSubscribeQ(q);
	}
	
	public static String newId(String q) {
		String uuid = UUID.randomUUID().toString();
		uuid = isSubscribeQ(q)? SUB_UUID_PREFIX + uuid: uuid;
		return uuid;
	}
	
	public static boolean isSubscribeQ(String query) {
		return query.toLowerCase().startsWith(SUB_QUERY_PREFIX);		
	}
	
	public static boolean isSubscribeId(String id) {
		return id.startsWith(SUB_UUID_PREFIX);		
	}
	
	public boolean isSubscribeQ() {
		return subscribe;
	}
	
	public boolean isSubscribeId() {
		return id.startsWith(SUB_UUID_PREFIX);
	}
	
	public String getQuery() {
		return query;
	}
	
	public JKQueryCallback getCallback() {
		return callback;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JKQueryHandle) {
			JKQueryHandle q2 = (JKQueryHandle) obj;
			return id.equals(q2.id);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+ "{"
				+ "id: " + id
				+ ", query:" + query
				+ ", callback:" + callback
				+ "}";
	}
}
