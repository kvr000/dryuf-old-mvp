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

import net.dryuf.srvui.PageUrl;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.AbstractStaticRenderer;


public class VerticalMenuRenderer extends AbstractStaticRenderer<Object, Object>
{
	public				VerticalMenuRenderer()
	{
	}

	@Override
	public void			render(Presenter presenter, Object obj, Object carrier)
	{
		presenter.outputFormat(
				"<table class='lmenu' width='140' border='0' cellspacing='5' cellpadding='0'>\n"
				);
		renderMenuItems(presenter);
		presenter.output(
				"</table>\n"
				);
	}

	public void			renderMenuItems(Presenter presenter)
	{
		presenter.outputFormat(
				"	<tr><td><a href=\"%U\">%W</a>\n",
				PageUrl.createPaged(""), net.dryuf.mvp.MainXhtmlPresenter.class, "Main page"
				);
		presenter.output("	<tr><td>");
		renderLogged(presenter);
		presenter.output("	</td></tr>");
	}

	public void			renderLogged(Presenter presenter)
	{
		presenter.outputFormat(
				"<a href=\"%U\">%W</a>",
				PageUrl.createPaged(presenter.getCallerContext().getUserId() == null ? "login" : "logout"),
				net.dryuf.mvp.MainXhtmlPresenter.class,
				presenter.getCallerContext().getUserId() == null ? "Log in" : "Log out"
				);
	}
};
