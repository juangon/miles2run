package org.milestogo.framework;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

public interface Viewable
{
	/**
	 * Called to do the actual work of rendering a view.  Note that while ServletException
	 * can be thrown, WebApplicationException is preferred.
	 */
	public String render(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, WebApplicationException;
}
