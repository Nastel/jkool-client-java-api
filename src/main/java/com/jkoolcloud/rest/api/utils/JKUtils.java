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

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * This class implements common API utilities
 * 
 * @author albert
 */
public class JKUtils {

	/**
	 * JVM process ID
	 */
	public static final long VM_PID = initVMID();
	
	/**
	 * JVM user name
	 */
	public static final String VM_USER = System.getProperty("user.name");
	
	/**
	 * JVM host name
	 */
	public static String VM_HOST;
	
	/**
	 * JVM network address
	 */
	public static String VM_NETADDR;
	
	/**
	 * JSON object mapper instance
	 */
	public static final ObjectMapper MAPPER = newObjectMapper();

	/**
	 * JVM runtime name
	 */
	public static final String VM_NAME = ManagementFactory.getRuntimeMXBean().getName();
	
	
	static {
		try {
	        VM_HOST = InetAddress.getLocalHost().getHostName();
	        VM_NETADDR = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
           	VM_HOST = "uknown";
           	VM_NETADDR = "0.0.0.0";
        }
	}

	private static long initVMID() {
		String _vm_pid_del = System.getProperty("jk.vm.pid.dlm", "@");
		String vm_name = ManagementFactory.getRuntimeMXBean().getName();
		try {
			int index = vm_name.indexOf(_vm_pid_del);
			if (index > 0) {
				return Long.parseLong(vm_name.substring(0, index));
			}
		} catch (Throwable e) {
		}
		return 0;
	}

	/**
	 * Return local host name
	 *
	 * @return Return local host name
	 */
	public static String getHostName() {
		return VM_HOST;
	}

	/**
	 * Return local host address
	 *
	 * @return Return local host address
	 */
	public static String getHostAddress() {
		return VM_NETADDR;
	}

	/**
	 * Return process ID associated with the current VM.
	 *
	 * @return process id associated with the current VM
	 */
	public static long getVMPID() {
		return VM_PID;
	}

	/**
	 * Return user name associated with the current VM.
	 *
	 * @return user name associated with the current VM.
	 */
	public static String getVMUser() {
		return VM_USER;
	}

	/**
	 * Return a name associated with the current VM.
	 *
	 * @return name associated with the current VM
	 */
	public static String getVMName() {
		return VM_NAME;
	}

	public static ObjectMapper getJsonMapper() {
		return MAPPER;
	}
	
	public static ObjectMapper newObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.registerModule(new JodaModule());
		return mapper;
	}
}
