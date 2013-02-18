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
package org.dihedron.ehttpd.renderers;

import java.io.UnsupportedEncodingException;

import org.dihedron.ehttpd.exceptions.ApplicationException;
import org.dihedron.ehttpd.exceptions.ServerException;
import org.dihedron.ehttpd.resources.FileStaticResource;
import org.dihedron.ehttpd.resources.JarStaticResource;
import org.dihedron.ehttpd.resources.StaticResource;
import org.dihedron.ehttpd.server.Response;
import org.dihedron.ehttpd.server.Status;
import org.dihedron.ehttpd.utils.VariableReplaceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a basic HTML result renderer; the result is sent back
 * to the client after having replaces any variables with the values
 * in the input map.
 * 
 * @author Andrea Funto'
 */
public class WebTemplateRenderer extends AbstractRenderer {

	/**
	 * The logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(WebTemplateRenderer.class);
	
	/**
	 * The web page template, as a static resource.
	 */
	private StaticResource template;
	
	/**
	 * Constructor.
	 */
	public WebTemplateRenderer() {
	}
		
	/**
	 * Renders the output to the client, using te given HTML template
	 * and the map of input parameters as output.
	 * 
	 * @param response
	 *   the client response.
	 * @see org.dihedron.ehttpd.renderers.Renderer#render(org.dihedron.ehttpd.server.Response)
	 */
	//@Override
	public void render(Response response) throws ApplicationException, ServerException {
		
		if(template == null) {
			initialise();
		}
		
		logger.debug("rendering output to client");
		
		String buffer;
		try {
			buffer = new String(template.getData(), "UTF-8");
			buffer = VariableReplaceUtils.replaceScalarVariables(buffer, variables);
			buffer = VariableReplaceUtils.replaceVectorVariables(buffer, variables);
			if(messageProvider != null) {
				buffer = VariableReplaceUtils.replaceMessages(buffer, messageProvider);
			}
			
			byte [] result = buffer.getBytes("UTF-8");
		
			logger.debug("rendering performed successfully");
			
			response.setStatus(Status.STATUS_200);
			response.setHeader("Content-Type", "text/html");
			response.addContent(result);
			response.flush();			
			
		} catch (UnsupportedEncodingException e) {
			logger.error("unsupported encoding translating template to string", e);
			throw new ApplicationException(e.getLocalizedMessage());
		}
	}
	
	private synchronized void initialise() throws ApplicationException {
		logger.debug("initialising template");
		
		// check if there are any parameters in place
		if(parameters == null) {
			logger.error("web resource renderer has no configuration");
			throw new ApplicationException("web resource renderer has no configuration");
		}
		
		// get the name of the template
		String resourceName = parameters.get("template");
		if(resourceName == null || resourceName.length() == 0) {
			logger.error("no valid template specified for the web resource renderer");
			throw new ApplicationException("no valid template specified for the web resource renderer");			
		}

		if(resourceName == null || resourceName.length() == 0) {
			logger.error("no valid template specified for the web resource renderer");
			throw new ApplicationException("no valid template specified for the web resource renderer");			
		}
		
		String resourceType = parameters.get("type");
		if(resourceType != null && resourceType.equalsIgnoreCase("file")) {
			this.template = new FileStaticResource("text/html", resourceName); 
		} else {
			this.template = new JarStaticResource(resourceName, "text/html");
		}
	}
}
