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

import net.dryuf.core.Options;
import net.dryuf.core.CallerContext;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Response;
import net.dryuf.srvui.PageContext;


public class ChildPresenter extends Presenter
{
	public				ChildPresenter(Presenter parentPresenter_, Options options)
	{
		assert parentPresenter_ != null;

		this.parentPresenter = parentPresenter_;
		this.rootPresenter = this.parentPresenter.getRootPresenter();

		if (!options.getOptionDefault("nolead", Boolean.FALSE))
			parentPresenter.setLeadChild(this);
	}

	@Override
	public boolean			processMore(String element)
	{
		getRootPresenter().putBackLastElement();
		return this.createDefaultPresenter().process();
	}

	@Override
	public boolean			processFinal()
	{
		switch (this.getRequest().getMethod()) {
		case "HEAD":
			return this.processHead();
		case "GET":
			return this.processGet();
		case "POST":
			return this.processPost();
		case "PUT":
			return this.processPut();
		case "PATCH":
			return this.processPatch();
		case "DELETE":
			return this.processDelete();
		case "OPTIONS":
			return this.processOptions();
		case "TRACE":
			return this.processTrace();
		default:
			return this.processOther();
		}
	}

	public boolean			processCommon()
	{
		return true;
	}

	public boolean			processSpecial()
	{
		return createUnallowedMethodPresenter().process();
	}

	public boolean			processHead()
	{
		return this.processCommon();
	}

	public boolean			processGet()
	{
		return this.processCommon();
	}

	public boolean			processPost()
	{
		return this.processCommon();
	}

	public boolean			processPut()
	{
		return this.processSpecial();
	}

	public boolean			processPatch()
	{
		return this.processSpecial();
	}

	public boolean			processDelete()
	{
		return this.processSpecial();
	}

	public boolean			processTrace()
	{
		return this.processSpecial();
	}

	public boolean			processOptions()
	{
		return this.processSpecial();
	}

	public boolean			processOther()
	{
		return this.createUnallowedMethodPresenter().process();
	}

	@Override
	public net.dryuf.core.UiContext	getUiContext()
	{
		return this.rootPresenter.getUiContext();
	}

	@Override
	public CallerContext		getCallerContext()
	{
		if (this.callerContext == null)
			this.callerContext = this.parentPresenter.getCallerContext();
		return this.callerContext;
	}

	@Override
	public Request			getRequest()
	{
		return this.rootPresenter.getRequest();
	}

	@Override
	public Response			getResponse()
	{
		return this.rootPresenter.getResponse();
	}

	@Override
	public RootPresenter		getRootPresenter()
	{
		return this.rootPresenter;
	}

	public Presenter		getParentPresenter()
	{
		return this.parentPresenter;
	}

	@Override
	public PageContext		getPageContext()
	{
		return this.getRootPresenter().getPageContext();
	}

	public void			setParentWithLead(Presenter parentPresenter)
	{
		this.parentPresenter = parentPresenter;
		parentPresenter.setLeadChild(this);
	}

	@Override
	public void			setCallerContext(CallerContext callerContext)
	{
		this.callerContext = callerContext;
	}

	@Override
	public String			getLanguage()
	{
		return this.rootPresenter.getLanguage();
	}

	@Override
	public void			setTitle(String title)
	{
		this.parentPresenter.setTitle(title);
	}

	@Override
	public void			output(String text)
	{
		this.rootPresenter.output(text);
	}

	@Override
	public void			outputFormat(String format, Object... args)
	{
		rootPresenter.outputFormat(format, args);
	}

	@Override
	public String			localize(String classname, String text)
	{
		return this.rootPresenter.localize(classname, text);
	}

	@Override
	public String			localizeArgs(String classname, String text, Object[] args)
	{
		return this.rootPresenter.localizeArgs(classname, text, args);
	}

	@Override
	public void addMessage(int msgType, String message)
	{
		rootPresenter.addMessage(msgType, message);
	}

	@Override
	public void addMessageLocalized(int msgType, String classname, String message)
	{
		rootPresenter.addMessageLocalized(msgType, classname, message);
	}

	@Override
	public Presenter		createNotFoundPresenter()
	{
		return parentPresenter.createNotFoundPresenter();
	}

	@Override
	public Presenter		createDeniedPresenter()
	{
		return parentPresenter.createDeniedPresenter();
	}

	@Override
	public Presenter		createUnallowedMethodPresenter()
	{
		return parentPresenter.createUnallowedMethodPresenter();
	}

	@Override
	public Presenter		createDefaultPresenter()
	{
		return parentPresenter.createDefaultPresenter();
	}

	protected Presenter		parentPresenter;

	protected RootPresenter		rootPresenter;
}
