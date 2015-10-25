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

import java.util.LinkedHashMap;

import net.dryuf.app.FieldDef;
import net.dryuf.textual.TextualManager;
import net.dryuf.meta.FilterDef;
import net.dryuf.srvui.Request;


public class DataPresenter_ModeList <T> extends net.dryuf.datagrid.DataPresenter_SubMode<T>
{
	public				DataPresenter_ModeList(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
	}

	public boolean			process()
	{
		String el;
		if (this.getLeadChild() != null) {
			return this.getLeadChild().process();
		}
		else if ((el = this.getRootPresenter().getPathElement()) != null) {
			if (el.equals("-page-")) {
				return this.processPage();
			}
			return this.processMore(el);
		}
		else {
			return this.processFinal();
		}
	}

	public boolean			processPage()
	{
		String pageStr;
		rootPresenter = this.getRootPresenter();
		if ((pageStr = rootPresenter.getPathElement()) == null) {
			return this.createNotFoundPresenter().process();
		}
		this.getDataPresenter().setListPageNum((int)(long)TextualManager.convertTextual(net.dryuf.textual.PageNumberTextual.class, this.getCallerContext(), pageStr)-1);
		if (rootPresenter.needPathSlash(true) == null) {
			return false;
		}
		if (!rootPresenter.needPathFinalParent()) {
			return true;
		}
		return this.processCommon();
	}

	public boolean			processCommon()
	{
		Request request = this.getRequest();
		DataPresenter<T> dataPresenter = this.getDataPresenter();
		dataPresenter.setListFilter(new LinkedHashMap<String, Object>());
		for (FieldDef<?> fdef: this.classMeta.getFields()) {
			String value;
			if ((value = request.getParamDefault(fdef.getName(), null)) != null) {
				dataPresenter.getListFilter().put(fdef.getName(), classMeta.convertField(getCallerContext(), fdef, value));
			}
		}
		for (FilterDef filterDef: this.classMeta.getFilterDefsHash().values()) {
			String value;
			if ((value = request.getParamDefault(filterDef.name(), null)) != null) {
				dataPresenter.getListFilter().put(":"+filterDef.name(), value);
			}
		}
		dataPresenter.setListTotal(dataPresenter.loadList());
		dataPresenter.setRenderingList();
		return true;
	}
}
