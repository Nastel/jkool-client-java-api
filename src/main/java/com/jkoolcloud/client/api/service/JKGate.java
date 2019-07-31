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

/**
 * This interface defines a generic way to implement object checkers, gates.
 * 
 * @author albert
 */
public interface JKGate<T> {
	/**
	 * Run an implementation specific check on a given object
	 * 
	 * @param obj
	 *            object to be checked
	 * @return true of check was true, false otherwise
	 */
	boolean check(T obj);
}
