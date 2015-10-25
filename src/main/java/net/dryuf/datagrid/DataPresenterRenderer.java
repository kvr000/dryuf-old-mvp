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

package net.dryuf.datagrid;

import javax.validation.constraints.NotNull;

import net.dryuf.app.FieldDef;
import net.dryuf.core.EntityHolder;
import net.dryuf.mvp.AbstractStaticRenderer;
import net.dryuf.mvp.Presenter;


public abstract class DataPresenterRenderer<T extends Object, OBJ extends Object, CARRIER> extends AbstractStaticRenderer<OBJ, CARRIER>
{
	public			 	DataPresenterRenderer()
	{
	}

	public CARRIER			createModel()
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CARRIER			prepare(Presenter presenter, OBJ data)
	{
		CARRIER model = createModel();
		prepareData((DataPresenter<T>) presenter, data, model);
		return model;
	}

	public void			prepareData(@NotNull DataPresenter<T> presenter, OBJ data, CARRIER model)
	{
	}

	public String			getInfoCssClasses()
	{
		return "net-dryuf-datagrid-Renderer-info";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void			render(Presenter presenter, OBJ data, CARRIER model)
	{
		renderData((DataPresenter<T>) presenter, data, model);
	}

	public void			renderData(@NotNull DataPresenter<T> presenter, OBJ data, CARRIER model)
	{
	}

	public void			renderInfo(@NotNull DataPresenter<T> presenter, @NotNull EntityHolder<T> obj, CARRIER model)
	{
		presenter.outputFormat("<table width=\"100%%\" class=%A>\n", getInfoCssClasses());
		this.renderInfoActions(presenter, obj, model);
		this.renderInfoContent(presenter, obj, model);
		presenter.outputFormat("</table>\n");
	}

	public void			renderInfoActions(@NotNull DataPresenter<T> presenter, @NotNull EntityHolder<T> obj, CARRIER model)
	{
		presenter.outputFormat("<tr><td>");
		this.renderInfoActionsContent(presenter, obj, model);
		presenter.outputFormat("</td></tr>");
	}

	public void			renderInfoActionsContent(@NotNull DataPresenter<T> presenter, @NotNull net.dryuf.core.EntityHolder<T> obj, CARRIER model)
	{
		if (obj.checkAccess(presenter.classMeta.getEntityRoles().roleSet()))
			presenter.outputFormat("<a href=\"?mode=edit\" class=\"action\">%W</a> ", net.dryuf.datagrid.DataPresenter.class, "Edit");
		if (obj.checkAccess(presenter.classMeta.getEntityRoles().roleDel()))
			presenter.outputFormat("<a href=\"?mode=remove\" class=\"action\">%W</a> ", net.dryuf.datagrid.DataPresenter.class, "Remove");
	}

	public void			renderInfoContent(@NotNull DataPresenter<T> presenter, @NotNull net.dryuf.core.EntityHolder<T> obj, CARRIER model)
	{
		T entity = obj.getEntity();
		for (FieldDef<?> fdef: presenter.classMeta.getFields()) {
			presenter.outputFormat("<tr><td class=\"key\">%W:</td><td class=\"value\">%S</td></tr>\n", presenter.getDataClass(), fdef.getName(), presenter.formatField(entity, fdef));
		}
	}
};
