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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.dryuf.core.CallerContext;
import net.dryuf.core.UiContext;
import net.dryuf.srvui.DefaultPageContext;
import net.dryuf.srvui.PageContext;
import net.dryuf.srvui.SimpleRefStringifier;
import net.dryuf.web.jee.JeeWebRequest;
import net.dryuf.srvui.MetaTag;
import net.dryuf.srvui.PageUrl;
import net.dryuf.srvui.PendingMessage;
import net.dryuf.srvui.RefStringifier;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Response;
import net.dryuf.srvui.Session;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;


public abstract class RootPresenter extends Presenter
{
	public				RootPresenter(CallerContext callerContext_, Request request_)
	{
		callerContext = callerContext_;
		this.pageContext = new DefaultPageContext(callerContext_, request_);
		uiContext = callerContext.getUiContext();
		request = request_;
		started = System.currentTimeMillis();

		this.refStringifier = new SimpleRefStringifier(getPageContext());
	}

	public RootPresenter		getRootPresenter()
	{
		return this;
	}

	@Override
	public Presenter		createDefaultPresenter()
	{
		return this.createNotFoundPresenter();
	}

	public PageContext		getPageContext()
	{
		return this.pageContext;
	}

	public Response			getResponse()
	{
		return getRequest().getResponse();
	}

	public int			getServerError()
	{
		return serverError;
	}

	public void			setServerError(int code, String message)
	{
		this.serverError = code;
		this.serverMessage = message;
	}

	public Session			getSession()
	{
		if (session == null)
			session = getRequest().getSession();
		return session;
	}

	public Session			forceSession()
	{
		return getRequest().forceSession();
	}

	public void			invalidateSession()
	{
		getRequest().invalidateSession();
		session = null;
	}

	public abstract void		output(String text);

	public abstract String		escapeText(String text);

	public String			getLanguage()
	{
		return uiContext.getLanguage();
	}

	public String			getContextPath()
	{
		return "";
	}

	/**
	 * Gets next path element, excluding the optional slash.
	 *
	 * @return next path element
	 */
	public String			getPathElement()
	{
		return pageContext.getPathElement();
	}

	/**
	 * Gets next path element, excluding the optional slash.
	 * Checks for safety of the element, i.e. it must not contain special filesystem characters, like /.
	 *
	 * @return next path element
	 */
	public String			getPathElementSafe()
	{
		String element;
		if ((element = getPathElement()) == null)
			return null;
		if (element.indexOf('/') >= 0)
			throw new IllegalArgumentException("/ not allowed as part of path");
		if (element.equals(".."))
			throw new IllegalArgumentException(".. not allowed as part of path");
		return element;
	}

	/**
	 * Puts back the last element so it can be processed again.
	 */
	public void			putBackLastElement()
	{
		pageContext.putBackLastElement();
	}

	/**
	 * Gets last element in the path, including the slash if directory.
	 */
	public String			getLastElement()
	{
		return pageContext.getLastElement();
	}

	/**
	 * Gets last element in the path, including the slash if directory.
	 */
	public String			getLastElementWithoutSlash()
	{
		return pageContext.getLastElementWithoutSlash();
	}

	/**
	 * Get reverse path to current path part.
	 *
	 * @return
	 * 	reverse path to current path part.
	 */
	public String			getReversePath()
	{
		return pageContext.getReversePath();
	}

	/**
	 * Checks that there is no more item in the path. If there is, createNotFoundPresenter() is called to create error presenter in parent presenter.
	 *
	 * @return true
	 * 	if there is no more item
	 * @return false
	 * 	if there is an item remaining
	 */
	public boolean			needPathFinalParent()
	{
		if (getPathElement() != null) {
			for (Presenter p = this; ; p = p.getLeadChild()) {
				if (p.getLeadChild() == null) {
					p = ((ChildPresenter)p).getParentPresenter();
					p.setLeadChild(null);
					p.createNotFoundPresenter();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks that there is no more item in the path. If there is, createNotFoundPresenter() is called to create
	 * error presenter in current presenter.
	 *
	 * @return true if there is no more item, false if there is item remaining
	 */
	public boolean			needPathFinalCurrent()
	{
		if (getPathElement() != null) {
			for (Presenter p = this; ; p = p.getLeadChild()) {
				if (p.getLeadChild() == null) {
					p.createNotFoundPresenter();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the current path ends with slash, depending on needSlash parameter. Either redirects to slashed
	 * path or creates not found presenter if the condition is not satisfied.
	 *
	 * @return current element
	 * 	if the condition was satisfied
	 * @return null
	 * 	otherwise, in that case process() function should return !needSlash
	 */
	public String			needPathSlash(boolean needSlash)
	{
		if (!pageContext.needPathSlash(needSlash)) {
			if (needSlash) {
				if (!getRequest().getMethod().equals("GET")) {
					Presenter presenter = createUnallowedMethodPresenter();
					presenter.prepare();
					presenter.render();
				}
				else {
					getResponse().redirect("/"+pageContext.getCurrentPath()+"/");
				}
				return null;
			}
			else {
				for (Presenter p = this; ; p = p.getLeadChild()) {
					if (p.getLeadChild() == null) {
						p.createNotFoundPresenter();
						break;
					}
				}
				return null;
			}
		}
		return pageContext.getProcessingPath();
	}

	public String			getCurrentPath()
	{
		return pageContext.getCurrentPath();
	}

	public String			getRemainPath()
	{
		return pageContext.getRemainPath();
	}

	public String			getRealPath()
	{
		return pageContext.getRealPath();
	}

	public void			setRealPath(String realPath)
	{
		pageContext.setRealPath(realPath);
	}

	public String			getFullUrl()
	{
		HttpServletRequest servletRequest = ((JeeWebRequest)getRequest()).getServletRequest();
		StringBuffer url = servletRequest.getRequestURL();
		String query;
		if ((query = servletRequest.getQueryString()) != null)
			url.append(query);
		return url.toString();
	}

	public String			stringifyRef(PageUrl ref)
	{
		return this.refStringifier.stringifyRef(ref);
	}

	public boolean			redirect(@NotNull PageUrl ref)
	{
		if (redirected != null) {
			throw new RuntimeException("already redirected to "+this.redirected);
		}
		String url = this.stringifyRef(ref);
		// process pendingMessages ??
		this.redirected = url;
		return false;
	}

	public void			redirectImm(PageUrl ref)
	{
		throw new UnsupportedOperationException("unimplemented");
	}

	public boolean			processMore(String element)
	{
		throw new UnsupportedOperationException("called processMore on RootPresenter");
	}

	public boolean			processFinal()
	{
		throw new UnsupportedOperationException("called processFinal on RootPresenter");
	}

	public String			localize(String className, String text)
	{
		return uiContext.localize(className, text);
	}

	public String			localizeArgs(String className, String text, Object[] args)
	{
		return uiContext.localizeArgs(className, text, args);
	}

	public void			addLinkedFile(String type, PageUrl url)
	{
		pageContext.addLinkedFile(type, url);
	}

	public void			addLinkedContent(String type, String identity, String content)
	{
		pageContext.addLinkedContent(type, identity, content);
	}

	public Collection<PageUrl>	getLinkedFiles(String type)
	{
		return pageContext.getLinkedFiles(type);
	}

	public void			addMeta(MetaTag metaTag)
	{
		pageContext.addMeta(metaTag);
	}

	public void			addMetaName(String name, String content)
	{
		this.addMeta(new MetaTag("name", name, content));
	}

	public void			addMetaHttp(String name, String content)
	{
		this.addMeta(new MetaTag("http-equiv", name, content));
	}

	public Map<String, Map<String, MetaTag>> getMetas()
	{
		return pageContext.getMetas();
	}

	@Override
	public void			addMessage(int msgType, String msg)
	{
		pageContext.addMessage(msgType, msg);
	}

	@Override
	public void			addMessageLocalized(int msgType, String classname, String msg)
	{
		pageContext.addMessage(msgType, localize(classname, msg));
	}


	public boolean			run()
	{
		boolean ret;
		try {
			if (process()) {
				if (this.serverError != 0) {
					getResponse().sendStatus(serverError, serverMessage);
				}
				prepare();
				render();
				ret = true;
			}
			else {
				if (this.serverError != 0) {
					getResponse().sendStatus(serverError, serverMessage);
				}
				else if (redirected != null) {
					getResponse().redirect(redirected);
				}
				ret = false;
			}
			return ret;
		}
		finally {
			close();
		}
	}

	@Override
	public void			close()
	{
		try {
			super.close();
		}
		finally {
			callerContext.close();
		}
	}

	public void			setActiveField(int priority, String activeField_)
	{
		this.activeField = activeField_;
	}

	public List<PendingMessage>	getPendingMessages()
	{
		return pageContext.getPendingMessages();
	}

	protected String		redirected;

	protected int			serverError;

	protected String		serverMessage;

	protected UiContext		uiContext;

	public UiContext		getUiContext()
	{
		return this.uiContext;
	}

	protected CallerContext		callerContext;

	public CallerContext		getCallerContext()
	{
		return this.callerContext;
	}

	public void			setCallerContext(CallerContext callerContext_)
	{
		this.callerContext = callerContext_;
	}

	protected Request		request;

	public Request			getRequest()
	{
		return this.request;
	}

	protected RefStringifier	refStringifier;

	public RefStringifier		getRefStringifier()
	{
		return this.refStringifier;
	}

	public void			setRefStringifier(RefStringifier refStringifier_)
	{
		this.refStringifier = refStringifier_;
	}

	protected String		title = "";

	public String			getTitle()
	{
		return this.title;
	}

	public void			setTitle(String title_)
	{
		this.title = title_;
	}

	protected String		activeField;

	public String			getActiveField()
	{
		return this.activeField;
	}

	protected long			started;

	public long			getStarted()
	{
		return this.started;
	}

	public void			setStarted(long started_)
	{
		this.started = started_;
	}

	protected Session		session;

	protected PageContext		pageContext;

	protected static Pattern	pathMatcher = Pattern.compile("^([^/]+)(/+(.*))?$");
	protected static Pattern	lastMatcher = Pattern.compile("^(|.*/)([^/]+/*)$");
	protected static Pattern	lastMatcherWithoutSlash = Pattern.compile("^(|.*/)([^/]+)/*$");
};
