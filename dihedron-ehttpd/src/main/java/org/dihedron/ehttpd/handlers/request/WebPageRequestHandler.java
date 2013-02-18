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
package org.dihedron.ehttpd.handlers.request;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.i18n.MessageProvider;
import org.dihedron.ehttpd.resources.StaticResource;
import org.dihedron.ehttpd.server.Request;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.handlers.BaseHandler;
import org.dihedron.ehttpd.utils.VariableReplaceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andrea Funto'
 */
public class WebPageRequestHandler extends BaseHandler {
	
	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(WebPageRequestHandler.class);
		
	public final static String DEFAULT_CONTENT_TYPE = "text/html";
	
	
	private String contentType;
	private String template;
	private Map<String, Object> variables;
	private MessageProvider messageProvider;
	
	public WebPageRequestHandler(StaticResource resource, Map<String, Object> variables, MessageProvider messageProvider) {
		contentType = resource.getContentType();
		if(!contentType.equals(DEFAULT_CONTENT_TYPE)) {
			logger.warn("invalid content type: \"" + contentType +"\"?");
		}
		try {
			this.template = new String(resource.getData(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("UTF-8 is not supported on this platform: ", e);
		}
		this.variables = variables;
		this.messageProvider = messageProvider;
	}
	
	public WebPageRequestHandler(String template, String contentType, Map<String, Object> variables) {
		this.template = template;
		this.contentType = contentType;
		this.variables = variables;
	}	

	protected void service(Request request, Response response) throws ServerException {
		String result = VariableReplaceUtils.replaceScalarVariables(template, variables);
		result = VariableReplaceUtils.replaceVectorVariables(result, variables);
		result = VariableReplaceUtils.replaceMessages(result, messageProvider);
		response.setHeader("Content-Type", contentType);
		response.addContent(result);
	}
	
}
