/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.mvp;

import javax.servlet.ServletContext;

import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import net.dryuf.core.Options;
import net.dryuf.web.jee.JeeWebRequest;


public class DefaultServletPresenter extends net.dryuf.mvp.ChildPresenter
{
	public				DefaultServletPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	public static boolean		createSafeAndProcess(Presenter parentPresenter)
	{
		Presenter this_ = new net.dryuf.mvp.DefaultServletPresenter(parentPresenter, net.dryuf.core.Options.NONE);
		return this_.process();
	}

	public boolean			process()
	{
		return this.processFinal();
	}

	public boolean			processCommon()
	{
		getRootPresenter().setLeadChild(this);
		return true;
	}

	public void			render()
	{
		try {
			JeeWebRequest webRequest = (JeeWebRequest)rootPresenter.getRequest();
			DefaultServletHttpRequestHandler defaultHandler = new DefaultServletHttpRequestHandler();
			defaultHandler.setServletContext((ServletContext)webRequest.getServletContext());
			defaultHandler.handleRequest(webRequest.getServletRequest(), webRequest.getServletResponse());
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
