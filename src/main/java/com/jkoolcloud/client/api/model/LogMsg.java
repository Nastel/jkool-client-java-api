/*
 * Copyright 2014-2025 JKOOL, LLC.
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
package com.jkoolcloud.client.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class defines an log message event.
 * 
 * @author albert
 */
@Schema(description = "")
public class LogMsg extends Event {

	/**
	 * Create an log message
	 * 
	 */
	public LogMsg() {
		super("LogMsg");
		this.setType(EvType.LOG);
	}

	/**
	 * Create an log message
	 * 
	 * @param msg
	 *            message associated with the event
	 */
	public LogMsg(String msg) {
		super("LogMsg");
		this.setMsgText(msg);
		this.setType(EvType.LOG);
	}

	/**
	 * Create an log message
	 * 
	 * @param sev
	 *            severity level
	 * @param msg
	 *            message associated with the event
	 */
	public LogMsg(Level sev, String msg) {
		super("LogMsg");
		this.setMsgText(msg);
		this.setType(EvType.LOG);
		this.setSeverity(sev);
	}

	/**
	 * Create an log message
	 * 
	 * @param tag
	 *            message tag
	 * @param msg
	 *            message associated with the event
	 */
	public LogMsg(String tag, String msg) {
		super("LogMsg");
		this.setMsgText(msg);
		this.setMsgTag(msg);
		this.setType(EvType.LOG);
	}

	/**
	 * Create an log message
	 * 
	 * @param name
	 *            event name
	 * @param tag
	 *            message tag
	 * @param msg
	 *            message associated with the event
	 */
	public LogMsg(String name, String tag, String msg) {
		super(name);
		this.setMsgText(msg);
		this.setMsgTag(msg);
		this.setType(EvType.LOG);
	}
}
