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

import net.dryuf.core.CallerContext;
import net.dryuf.core.Options;
import net.dryuf.core.UiContext;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Response;
import net.dryuf.srvui.PageContext;
import net.dryuf.trans.meta.NoDynamic;


public abstract class Presenter extends java.lang.Object
{
	public static final int		MSG_Fatal		= 100;
	public static final int		MSG_Error		= 200;
	public static final int		MSG_Warning		= 300;
	public static final int		MSG_Info		= 400;
	public static final int		MSG_Debug		= 500;
	public static final int		MSG_Trace		= 600;

	public				Presenter()
	{
	}

	public static <T extends Presenter> T createSubPresenter(Class<T> clazz, Presenter parentPresenter, Options options)
	{
		try {
			T presenter = clazz.getConstructor(Presenter.class, Options.class).newInstance(parentPresenter, options);
			presenter.init();
			return presenter;
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Presenter> T callInit(T presenter)
	{
		return (T)presenter.init();
	}

	public Presenter		init()
	{
		return this;
	}

	public abstract RootPresenter	getRootPresenter();

	public abstract PageContext	getPageContext();

	public abstract void		output(String text);

	public abstract void		outputFormat(String fmt, Object ... args);

	public abstract String		getLanguage();

	public abstract CallerContext	getCallerContext();

	public abstract void		setCallerContext(CallerContext callerContext);

	public abstract Request		getRequest();

	public abstract Response	getResponse();

	public abstract String		localize(String class_name, String text);

	public abstract String		localizeArgs(String class_name, String text, Object[] args);

	public abstract void addMessage(int messageLevel, String message);

	public abstract void addMessageLocalized(int messageLevel, String classname, String message);

	public abstract void		setTitle(String title);

	public abstract UiContext	getUiContext();

	@NoDynamic
	public String			localize(Class<?> clazz, String text)
	{
		return localize(clazz.getName(), text);
	}

	@NoDynamic
	public String			localizeArgs(Class<?> clazz, String text, Object[] args)
	{
		return localizeArgs(clazz.getName(), text, args);
	}

	@NoDynamic
	public void addMessageLocalized(int messageLevel, Class<?> clazz, String text)
	{
		addMessageLocalized(messageLevel, clazz.getName(), text);
	}

	public abstract Presenter	createNotFoundPresenter();
	public abstract Presenter	createDeniedPresenter();
	public abstract Presenter	createUnallowedMethodPresenter();
	public abstract Presenter	createDefaultPresenter();

	public boolean			process()
	{
		String element;
		if (this.leadChild != null) {
			return this.leadChild.process();
		}
		else if ((element = this.getRootPresenter().getPathElement()) != null) {
			return this.processMore(element);
		}
		else {
			return this.processFinal();
		}
	}

	public abstract boolean		processMore(String element);

	public abstract boolean		processFinal();


	public void			close()
	{
		if (leadChild != null)
			leadChild.close();
	}

	public void			prepare()
	{
		if (this.leadChild != null)
			this.leadChild.prepare();
	}

	public void			render()
	{
		if (this.leadChild != null)
			this.leadChild.render();
	}

	public boolean			renderLeadChild()
	{
		if (this.leadChild == null)
			return false;
		this.leadChild.render();
		return true;
	}

	public Presenter		getLeadChild()
	{
		return this.leadChild;
	}

	public void			setLeadChild(Presenter leadChild_)
	{
		this.leadChild = leadChild_;
	}

	protected Presenter		leadChild = null;

	protected CallerContext		callerContext;
}
