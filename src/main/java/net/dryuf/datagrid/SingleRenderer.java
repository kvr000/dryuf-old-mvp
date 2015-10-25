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


public abstract class SingleRenderer <T, CARRIER> extends net.dryuf.datagrid.DataPresenterRenderer<T, EntityHolder<T>, CARRIER>
{
	public				SingleRenderer()
	{
	}

	public void			prepareObject(@NotNull DataPresenter<T> presenter, @NotNull EntityHolder<T> object, CARRIER carrier)
	{
	}

	public void			renderInfo(@NotNull DataPresenter<T> presenter, @NotNull net.dryuf.core.EntityHolder<T> object, CARRIER carrier)
	{
		presenter.outputFormat("<table width=\"100%%\">\n");
		this.renderInfoActions(presenter, object, carrier);
		this.renderInfoContent(presenter, object, carrier);
		presenter.outputFormat("</table>\n");
	}

	public void			renderInfoActions(@NotNull DataPresenter<T> presenter, @NotNull net.dryuf.core.EntityHolder<T> object, CARRIER carrier)
	{
		presenter.outputFormat("<tr><td>");
		this.renderInfoActionsContent(presenter, object, carrier);
		presenter.outputFormat("</td></tr>");
	}

	public void			renderInfoActionsContent(@NotNull DataPresenter<T> presenter, @NotNull net.dryuf.core.EntityHolder<T> object, CARRIER carrier)
	{
		if (object.checkAccess(presenter.getClassMeta().getEntityRoles().roleSet()))
			presenter.outputFormat("<a href=\"?mode=edit\">%W</a> ", "net.dryuf.datagrid.DataPresenter", "Edit");
		if (object.checkAccess(presenter.getClassMeta().getEntityRoles().roleDel()))
			presenter.outputFormat("<a href=\"?mode=remove\">%W</a> ", "net.dryuf.datagrid.DataPresenter", "Remove");
	}

	public void			renderInfoContent(@NotNull DataPresenter<T> presenter, @NotNull net.dryuf.core.EntityHolder<T> object, CARRIER carrier)
	{
		T entity = object.getEntity();
		for (FieldDef<?> fdef: presenter.getClassMeta().getFields()) {
			presenter.outputFormat("<tr><td class='key'>%W:</td><td>%S</td></tr>\n", presenter.getDataClass(), fdef.getName(), presenter.formatField(entity, fdef));
		}
	}
}
