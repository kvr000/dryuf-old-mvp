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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.ClassMetaManager;
import net.dryuf.app.FieldDef;
import net.dryuf.core.Options;
import net.dryuf.meta.ActionDef;
import net.dryuf.mvp.Presenter;


public abstract class BeanFormPresenter <BACK> extends net.dryuf.mvp.DynamicFormPresenter
{
	public				BeanFormPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	@SuppressWarnings("unchecked")
	protected void			setBackingObject(BACK backingObject)
	{
		this.backingObject = backingObject;
		this.formClassName = backingObject.getClass().getName();
		this.formWebPrefix = formatFormPrefix(formClassName);
		backingMeta = ClassMetaManager.openCached(this.getCallerContext().getAppContainer(), (Class<BACK>) backingObject.getClass(), null);
	}

	protected abstract BACK		createBackingObject();

	public boolean			processCommon()
	{
		setBackingObject(createBackingObject());
		return super.processCommon();
	}

	public List<FieldDef<?>>	buildDisplayableFields()
	{
		LinkedList<FieldDef<?>> fields = new LinkedList<FieldDef<?>>();
		for (FieldDef<?> fieldDef: backingMeta.getFields()) {
			fields.add(fieldDef);
		}
		return fields;
	}

	public List<ActionDef>		getActionDefs()
	{
		if (backingActions == null)
			backingActions = Arrays.asList(backingMeta.getActions());
		return backingActions;
	}

	public BACK			needBackingObject()
	{
		if (this.backingObject == null)
			throw new NullPointerException("backingObject was not set");
		return this.backingObject;
	}

	protected void			setBackingValue(FieldDef<?> fieldDef, Object value)
	{
		backingMeta.setEntityFieldValue(backingObject, fieldDef.getName(), value);
	}

	protected Object		getBackingValue(FieldDef<?> fieldDef)
	{
		return backingMeta.getEntityFieldValue(backingObject, fieldDef.getName());
	}

	protected BACK			backingObject;

	public BACK			getBackingObject()
	{
		return this.backingObject;
	}

	protected List<ActionDef>	backingActions;

	public List<ActionDef>		getBackingActions()
	{
		return this.backingActions;
	}

	protected ClassMeta<BACK>	backingMeta;

	public ClassMeta<BACK>		getBackingMeta()
	{
		return this.backingMeta;
	}
};
