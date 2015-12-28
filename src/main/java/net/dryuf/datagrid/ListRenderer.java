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

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

import net.dryuf.core.Dryuf;
import org.apache.commons.lang3.StringUtils;

import net.dryuf.app.FieldDef;
import net.dryuf.core.EntityHolder;
import net.dryuf.meta.ActionDef;
import net.dryuf.net.util.UrlUtil;


public class ListRenderer <T, CARRIER> extends net.dryuf.datagrid.DataPresenterRenderer<T, List<EntityHolder<T>>, CARRIER>
{
	public				ListRenderer()
	{
		super();
	}

	@Override
	public void			prepareData(@NotNull net.dryuf.datagrid.DataPresenter<T> presenter, List<EntityHolder<T>> data, CARRIER carrier)
	{
		int i = 0;
		for (EntityHolder<T> object: data) {
			this.prepareObject(presenter, object, i, carrier);
			i++;
		}
		super.prepareLeadChild(presenter);
	}

	public void			prepareObject(DataPresenter<T> presenter, EntityHolder<T> object, int i, CARRIER carrier)
	{
	}

	@Override
	public void			renderData(@NotNull net.dryuf.datagrid.DataPresenter<T> presenter, List<EntityHolder<T>> data, CARRIER carrier)
	{
		this.renderList(presenter, data, carrier);
	}

	public String			getListCss()
	{
		return Dryuf.dashClassname(DataPresenter.class)+"-list-behaviour";
	}

	public void			renderList(DataPresenter<T> presenter, List<EntityHolder<T>> data, CARRIER carrier)
	{
		presenter.outputFormat("<div class=%A>\n", getListCss());
		this.renderListInner(presenter, data, carrier);
		presenter.output("</div>\n");
	}

	public void			renderListInner(DataPresenter<T> presenter, List<EntityHolder<T>> data, CARRIER carrier)
	{
		presenter.outputFormat("<table class='controls'>\n");
		this.renderListMainHead(presenter, carrier);
		presenter.outputFormat("</table>\n");

		this.renderListTable(presenter, data, carrier);
	}

	public void			renderListMainHead(DataPresenter<T> presenter, CARRIER carrier)
	{
		presenter.outputFormat("<tr>");
		presenter.outputFormat("<td class='globalactions'>\n");
		this.renderListGlobalActions(presenter, presenter.getGlobalActionList(), carrier);
		presenter.outputFormat("</td>\n");
		presenter.outputFormat("<td class='pager'>\n");
		this.renderListPager(presenter, carrier);
		presenter.outputFormat("</td>\n");
		presenter.outputFormat("<td class='command'>\n");
		this.renderListCommand(presenter, carrier);
		presenter.outputFormat("</td>\n");
		presenter.outputFormat("</tr>");
	}

	public void			renderListGlobalActions(DataPresenter<T> presenter, List<ActionDef> actions, CARRIER carrier)
	{
		for (ActionDef action: actions) {
			switch (action.name()) {
			case "new":
				presenter.outputFormat("<a class='action' href=\"%S-new-/\">%W</a> ", presenter.getRelativeUrl(), presenter.getDataClass(), action.name());
			}
		}
	}

	public void			renderListPager(DataPresenter<T> presenter, CARRIER carrier)
	{
		if (presenter.getListPageSize() != 0) {
			presenter.outputFormat("<form action=\".\">");
			List<Long> pages = new LinkedList<Long>();
			pages.add(presenter.getListPageNum());
			boolean cont = true;
			for (long diff = 1, step = 1; cont; diff += (step = step*2 > presenter.getListTotal()/presenter.getListPageSize()/7 ? Math.max((presenter.getListTotal()/presenter.getListPageSize()/7), 1) : (step*2))) {
				cont = false;
				if (diff < presenter.getListPageNum()) {
					cont = true;
					pages.add(0, presenter.getListPageNum()-diff);
				}
				if (diff < ((presenter.getListTotal()-1)/presenter.getListPageSize()-presenter.getListPageNum())) {
					cont = true;
					pages.add(presenter.getListPageNum()+diff);
				}
			}
			if (presenter.getListPageNum() > 0) {
				presenter.outputFormat("<a class='larr' href=\"%S-page-/%S\">◀</a> ", presenter.getRelativeUrl(), UrlUtil.appendOptionalQuery(presenter.getListPageNum()+"/", presenter.getRequest().getQueryString()));
				pages.add(0, 0L);
			}
			else {
				presenter.output("<span class='larr'>◀</span> ");
			}
			if (presenter.getListPageNum() < ((presenter.getListTotal()-1)/presenter.getListPageSize()))
				pages.add(((presenter.getListTotal()-1)/presenter.getListPageSize()));
			presenter.outputFormat("<select onchange='window.location=\"%S-page-/\"+(Number(this.value)+1)+\"/%S\";'>", presenter.getRelativeUrl(), UrlUtil.appendOptionalQuery("", presenter.getRequest().getQueryString()));
			for (Long page: pages) {
				if (page == presenter.getListPageNum())
					presenter.outputFormat("<option value=%A selected='selected'>%S</option>", String.valueOf(page), String.valueOf(page+1));
				else
					presenter.outputFormat("<option value=%A>%S</option>", String.valueOf(page), String.valueOf(page+1));
			}
			presenter.output("</select>");
			if (presenter.getListPageNum() < (presenter.getListTotal()-1)/presenter.getListPageSize()) {
				presenter.outputFormat(" <a class='rarr' href=\"%S-page-/%S\">▶</a>", presenter.getRelativeUrl(), UrlUtil.appendOptionalQuery((presenter.getListPageNum()+2)+"/", presenter.getRequest().getQueryString()));
			}
			else {
				presenter.output(" <span class='rarr'>▶</span>");
			}
			presenter.output("</form>");
		}
	}

	public void			renderListCommand(DataPresenter<T> presenter, CARRIER carrier)
	{
	}

	public void			renderListTable(DataPresenter<T> presenter, List<EntityHolder<T>> data, CARRIER carrier)
	{
		FieldDef<?>[] fields = presenter.getClassMeta().getFields();
		presenter.outputFormat("<table class='table' width=\"100%%\" border='1'>\n");
		this.renderListHead(presenter, fields, carrier);
		this.renderListContent(presenter, data, fields, carrier);
		presenter.output("</table>\n");
	}

	public void			renderListHead(DataPresenter<T> presenter, FieldDef<?>[] fields, CARRIER carrier)
	{
		presenter.outputFormat("<tr class='header'>\n");
		presenter.outputFormat("<th class='actions'>");
		//presenter.outputFormat("%W", "net.dryuf.datagrid.DataPresenter_ModeList", "Action");
		presenter.outputFormat("</th>");
		for (FieldDef<?> fdef: fields) {
			if (fdef.getAssocType() == FieldDef.AST_Children) {
			}
			else if (StringUtils.isEmpty(fdef.getDisplay())) {
				throw new RuntimeException("Field "+fdef+"[name] does not have display set");
			}
			else if (fdef.getDisplay().equals("hidden()")) {
				continue;
			}
			presenter.outputFormat("<th class='field'>%W</th>", presenter.getClassMeta().getDataClass(), fdef.getName());
		}
		presenter.output("</tr>\n");
	}

	public void			renderListContent(DataPresenter<T> presenter, List<EntityHolder<T>> data, FieldDef<?>[] fields, CARRIER carrier)
	{
		for (EntityHolder<T> dataObject: data) {
			this.renderListObject(presenter, fields, dataObject, carrier);
		}
	}

	public void			renderListObject(DataPresenter<T> presenter, FieldDef<?>[] fields, net.dryuf.core.EntityHolder<T> dataObject, CARRIER carrier)
	{
		presenter.outputFormat("<tr class='row'><td class='objectactions'>");
		this.renderListObjectActions(presenter, dataObject, carrier);
		presenter.outputFormat("</td>");
		this.renderListObjectData(presenter, fields, dataObject, carrier);
		presenter.outputFormat("</tr>\n");
	}

	public void			renderListObjectActions(DataPresenter<T> presenter, net.dryuf.core.EntityHolder<T> dataObject, CARRIER carrier)
	{
		T entity = dataObject.getEntity();
		presenter.outputFormat("<a class='action' href=\"%S%S\">%W</a> ", presenter.getRelativeUrl(), presenter.urlDisplayKey(entity), DataPresenter.class, "Detail");
		if (dataObject.checkAccess(presenter.getClassMeta().getEntityRoles().roleSet()))
			presenter.outputFormat("<a class='action' href=\"%S%S?mode=edit\">%W</a> ", presenter.getRelativeUrl(), presenter.urlDisplayKey(entity), DataPresenter.class, "Edit");
	}

	public void			renderListObjectData(DataPresenter<T> presenter, FieldDef<?>[] fields, net.dryuf.core.EntityHolder<T> dataObject, CARRIER carrier)
	{
		T entity = dataObject.getEntity();
		for (FieldDef<?> fdef: fields) {
			presenter.outputFormat("<td class='field'>%S</td>", presenter.formatField(entity, fdef));
		}
	}
}
