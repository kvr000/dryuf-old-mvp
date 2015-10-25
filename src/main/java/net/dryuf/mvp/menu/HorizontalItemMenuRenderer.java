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

package net.dryuf.mvp.menu;

import javax.inject.Inject;

import net.dryuf.menu.WebAccessiblePage;
import net.dryuf.menu.dao.WebAccessiblePageDao;
import net.dryuf.menu.dao.WebMenuItemDao;
import net.dryuf.srvui.PageUrl;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.AbstractStaticRenderer;


public class HorizontalItemMenuRenderer extends AbstractStaticRenderer<Object, Object>
{
	@Inject
	WebMenuItemDao			webMenuItemDao;

	@Inject
	WebAccessiblePageDao		webAccessiblePageDao;

	String				webProvider;

	public String			getWebProvider()
	{
		return this.webProvider;
	}

	public void			setWebProvider(String webProvider_)
	{
		this.webProvider = webProvider_;
	}

	public				HorizontalItemMenuRenderer()
	{
	}

	@Override
	public void			render(Presenter presenter, Object obj, Object carrier)
	{
		presenter.output("<div class=\"menu\"><ul>\n");
		for (WebAccessiblePage webPage: webMenuItemDao.listPagesRooted(webProvider)) {
			//WebAccessiblePage webPage = webAccessiblePageDao.loadByPageCode(menuItem.getPageCode());
			//if (webPage == null)
			//	throw new RuntimeException("WebAccessiblePage not defined for menu "+menuItem.getPageCode());
			if (!presenter.getCallerContext().checkRole(webPage.getReqRole()))
				continue;
			presenter.outputFormat("\t<li>\n\t\t");
			if (webPage.getPageCode().equals("login")) {
				renderLogged(presenter);
			}
			else {
				presenter.outputFormat("<a class=\"hide\" href=\"%U\">%S</a>\n", PageUrl.createPaged(webPage.getPageCode()), webPage.getPageCode());
			}
			renderSubMenu(presenter, webPage);
			presenter.outputFormat("\t</li>\n");
		}
		presenter.output("</ul></div>\n");
	}

	public void			renderSubMenu(Presenter presenter, WebAccessiblePage webPage)
	{
		presenter.output("\t\t<ul>\n");
		for (WebAccessiblePage subPage: webMenuItemDao.listPagesByParent(webProvider, webPage.getPageCode())) {
			//WebAccessiblePage subPage = webAccessiblePageDao.loadByPageCode(subItem.getPageCode());
			//if (subPage == null)
			//	throw new RuntimeException("WebAccessiblePage not defined for menu "+subItem.getPageCode());
			if (!presenter.getCallerContext().checkRole(subPage.getReqRole()))
				continue;
			presenter.outputFormat("\t\t\t<li>\n\t\t\t\t<a class=\"hide\" href=\"%U\">%S</a>\n", PageUrl.createPaged(subPage.getPageCode()), subPage.getPageCode());
			presenter.outputFormat("\t\t\t</li>\n");
		}
		presenter.output("\t\t</ul>\n");
	}

	public void			renderMenuItems(Presenter presenter)
	{
		presenter.outputFormat(
				"			<tr><td><a class='lmenu' href=\"%U\">%W</a>\n",
				PageUrl.createPaged(""), net.dryuf.mvp.MainXhtmlPresenter.class, "Main page"
				);
		presenter.output("			<tr><td>");
		renderLogged(presenter);
		presenter.output("			</td></tr>");
	}

	public void			renderLogged(Presenter presenter)
	{
		presenter.outputFormat(
				"<a class='lmenu' href=\"%U\">%W</a>\n",
				PageUrl.createPaged(presenter.getCallerContext().getUserId() == null ? "login" : "logout"),
				net.dryuf.mvp.MainXhtmlPresenter.class,
				presenter.getCallerContext().getUserId() == null ? "Log in" : "Log out"
				);
	}
}
