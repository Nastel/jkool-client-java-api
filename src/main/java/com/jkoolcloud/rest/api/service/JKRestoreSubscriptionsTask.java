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

import java.io.IOException;

/**
 * This class implements a task that automatically re-subscribes
 * all active subscriptions after reconnecting {@link JKQueryAsync}.
 * 
 * @author albert
 */
public class JKRestoreSubscriptionsTask implements JKGate<JKQueryHandle>, Runnable {
	JKQueryAsync agent;
	long timeStamp;

	protected JKRestoreSubscriptionsTask(JKQueryAsync async, long timeOpen) {
		timeStamp = timeOpen;
		agent = async;
	}

	@Override
	public void run() {
		try {
			if (agent.isConnected()) {
				agent.restoreSubscriptions(this);
			}
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}

	@Override
	public boolean check(JKQueryHandle handle) {
		return handle.isSubscribeQuery() && (handle.getTimeCreated() <= timeStamp); 
	}
}
