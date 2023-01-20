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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

class JKThreadFactory implements ThreadFactory {

	String threadPrefix;
	boolean daemon;
	AtomicLong counter = new AtomicLong(0);

	/**
	 * Thread counting thread factory
	 * 
	 * @param prefix
	 *            thread name prefix
	 * @param daemon
	 *            flag that defines weather threads are daemon
	 */
	public JKThreadFactory(String prefix, boolean daemon) {
		threadPrefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread th = new Thread(r, threadPrefix + "/" + counter.incrementAndGet());
		th.setDaemon(daemon);
		return th;
	}
}