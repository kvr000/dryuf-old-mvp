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

import net.dryuf.srvui.Request;
import org.apache.commons.lang3.StringUtils;

import net.dryuf.core.Options;
import net.dryuf.core.RoleContext;
import net.dryuf.service.logger.LoggerService;
import net.dryuf.service.logger.MessageLogger;


@SuppressWarnings("unused")
public class LoggerInterceptPresenter extends net.dryuf.mvp.ChildPresenter
{
	@SuppressWarnings("unchecked")
	public				LoggerInterceptPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
		this.subPresenter = (Class<? extends Presenter>) options.getOptionMandatory("subPresenter");
		this.subOptions = options.getOptionDefault("subOptions", net.dryuf.core.Options.NONE);
		this.loggerIdentifier = (String) options.getOptionMandatory("loggerIdentifier");
	}

	@Override
	public boolean			process()
	{
		Request request = getRequest();
		MessageLogger logger = (getCallerContext().getBeanTyped("loggerService", LoggerService.class)).getLogger(loggerIdentifier);
		logger.logMessage(request.getMethod(), request.getRemoteHost()+" "+request.getMethod()+" \""+getRootPresenter().getFullUrl()+"\" \""+StringUtils.defaultString(request.getHeader("User-Agent"), "")+"\" \""+StringUtils.defaultString(request.getHeader("Referer"), "")+"\"");
		Presenter.createSubPresenter(this.subPresenter, this, this.subOptions);
		return super.process();
	}

	public Class<? extends Presenter> subPresenter;
	public Options			subOptions;
	public String			loggerIdentifier;
}

