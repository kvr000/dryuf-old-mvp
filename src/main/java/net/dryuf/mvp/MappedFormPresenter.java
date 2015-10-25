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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.dryuf.app.ActionDefImpl;
import net.dryuf.app.ClassMeta;
import net.dryuf.app.FieldDef;
import net.dryuf.core.Options;
import net.dryuf.meta.ActionDef;
import net.dryuf.mvp.Presenter;


public abstract class MappedFormPresenter extends net.dryuf.mvp.DynamicFormPresenter
{
	public				MappedFormPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	public static String		formatFormPrefix(Class<?> formClazz)
	{
		return formClazz.getName().replace(".", "_")+"__";
	}

	@SuppressWarnings("unchecked")
	protected void			setBackingObject(Map<String, Object> backingObject)
	{
		this.backingObject = backingObject;
		this.formClassName = getClass().getName();
		this.formWebPrefix = formatFormPrefix(formClassName);
	}

	protected Map<String, Object>	createBackingObject()
	{
		return new LinkedHashMap<String, Object>();
	}

	public boolean			processCommon()
	{
		setBackingObject(createBackingObject());
		return super.processCommon();
	}

	public List<ActionDef>		getActionDefs()
	{
		return Collections.<ActionDef>singletonList(
			new ActionDefImpl()
				.setName("submit")
				.setRoleAction("")
				.setFormActioner(".performSubmit")
		);
	}

	public boolean			performSubmit(ActionDef actionDef)
	{
		throw new IllegalStateException("default performSubmit not implemented");
	}

	public Map<String, Object>	needBackingObject()
	{
		if (this.backingObject == null)
			throw new NullPointerException("backingObject was not set");
		return this.backingObject;
	}

	protected void			setBackingValue(FieldDef<?> fieldDef, Object value)
	{
		backingObject.put(fieldDef.getName(), value);
	}

	protected Object		getBackingValue(FieldDef<?> fieldDef)
	{
		return backingObject.get(fieldDef.getName());
	}

	protected Map<String, Object>	backingObject;

	public Map<String, Object>	getBackingObject()
	{
		return this.backingObject;
	}

	protected ActionDef[]		backingActions;

	public ActionDef[]		getBackingActions()
	{
		return this.backingActions;
	}
}
