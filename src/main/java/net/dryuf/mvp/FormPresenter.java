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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.dryuf.core.Dryuf;
import net.dryuf.core.Options;
import net.dryuf.core.ReportException;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Session;
import net.dryuf.meta.ActionDef;


public abstract class FormPresenter extends net.dryuf.mvp.ChildPresenter
{
	public				FormPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	public boolean			performAction(ActionDef action)
	{
		String formActioner = action.formActioner();
		if (formActioner != null) {
			try {
				if (formActioner.startsWith(".")) {
					return (Boolean)Dryuf.invokeMethod(this, Dryuf.getObjectMethod(this, formActioner.substring(1), ActionDef.class), action);
				}
				else {
					Object performer = Dryuf.createObjectArgs(Dryuf.loadConstructor(formActioner, FormPresenter.class), this);
					return (Boolean)Dryuf.invokeMethod(performer, Dryuf.getObjectMethod(performer, "performAction", ActionDef.class), action);
				}
			}
			catch (Exception ex) {
				throw new RuntimeException("Failed to run "+formActioner+": "+ex.toString(), ex);
			}
		}
		throw new ReportException("invalid action: "+action.name());
	}

	public abstract void		initForm();
	public abstract boolean		retrieve(Map<String, String> errors, ActionDef action);

	public boolean			processCommon()
	{
		for (ActionDef action: getActionDefs()) {
			Map<String, String> errors = new LinkedHashMap<String, String>();
			if (getRequest().getParamDefault(formWebPrefix+action.name(), null) == null)
				continue;
			if (!retrieve(errors, action)) {
				for (String key: errors.keySet()) {
					addMessage(Presenter.MSG_Error, localize(formClassName, key)+": "+errors.get(key));
				}
				return true;
			}
			else {
				return performAction(action);
			}
		}
		initForm();
		return true;
	}

	public static String		getRequestCaptcha(Request request)
	{
		Session session;
		if ((session = request.getSession()) != null)
			return (String)session.getAttribute(FormPresenter.class.getName()+".captcha");
		return null;
	}

	public static void		setRequestCaptcha(Request request, String captcha)
	{
		request.forceSession().setAttribute(FormPresenter.class.getName()+".captcha", captcha);
	}

	protected abstract List<ActionDef> getActionDefs();

	protected String		formClassName;

	protected String		formWebPrefix;

	public String			getFormWebPrefix()
	{
		return this.formWebPrefix;
	}
};
