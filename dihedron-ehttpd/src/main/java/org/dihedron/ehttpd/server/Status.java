/**
 * Copyright (c) 2011, 2013, Andrea Funto'. All rights reserved.
 * 
 * This file is part of the Dihedron embeddable web container ("e-httpd").
 *
 * "e-httpd" is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * "e-httpd" is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with "e-httpd". If not, see <http://www.gnu.org/licenses/>.
 */

package org.dihedron.ehttpd.server;

/**
 * @author Andrea Funto'
 */
public enum Status {
	// success
	STATUS_200(200, "OK"),
	STATUS_201(201, "Created"),
	STATUS_202(202, "Accepted"),
	STATUS_203(203, "Non-Authoritative Information"),
	STATUS_204(204, "No Content"),
	STATUS_205(205, "Reset Content"),
	STATUS_206(206, "Partial Content"),
	// redirects
	STATUS_300(300, "Multiple Choices"),
	STATUS_301(301, "Moved Permanently"),
	STATUS_302(302, "Found"),
	STATUS_303(303, "See Other"),
	STATUS_304(304, "Not Modified"),
	STATUS_305(305, "Use Proxy"),
	STATUS_307(307, "Temporary Redirect"),
	// client error
	STATUS_400(400, "Bad Request"),
	STATUS_401(401, "Unauthorized"),
	STATUS_402(402, "Payment Required"),
	STATUS_403(403, "Forbidden"),
	STATUS_404(404, "Not Found"),
	STATUS_405(405, "Method Not Allowed"),
	STATUS_406(406, "Not Acceptable"),
	STATUS_407(407, "Proxy Authentication Required"),
	STATUS_408(408, "Request Timeout"),
	STATUS_409(409, "Conflict"),
	STATUS_410(410, "Gone"),
	STATUS_411(411, "Length Required"),
	STATUS_412(412, "Precondition Failed"),
	STATUS_413(413, "Request Entity Too Large"),
	STATUS_414(414, "Request-URI Too Long"),
	STATUS_415(415, "Unsupported Media Type"),
	STATUS_416(416, "Requested Range Not Satisfiable"),
	STATUS_417(417, "Expectation Failed"),
	// server errors
	STATUS_500(500, "Internal Server Error"),
	STATUS_501(501, "Not Implemented"),
	STATUS_502(502, "Bad Gateway"),
	STATUS_503(503, "Service Unavailable"),
	STATUS_504(504, "Gateway Timeout"),
	STATUS_505(505, "HTTP Version Not Supported");
	
	private Status(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String toString() {
		return "" + code + " " + description;
	}
	
	private int code = 0;
	
	private String description = null;	
}
