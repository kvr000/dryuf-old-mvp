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

import net.dryuf.core.Options;
import net.dryuf.mvp.Presenter;


public class MainHtmlPresenter extends MainXhtmlPresenter
{
	public				MainHtmlPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	public void			headDocumentUtf8()
	{
		this.getRootPresenter().getResponse().setContentType("text/html; charset=UTF-8");
		this.getRootPresenter().addMetaHttp("Content-Type", "text/html; charset=UTF-8");
	}

	public void			renderDocumentStart()
	{
		this.output(
				"<!DOCTYPE html>\n"+
				"<html>\n"
		);
	}

	public void			addW3CStat()
	{
		this.addStat(new net.dryuf.mvp.stat.W3cHtmlPresenter(this, Options.NONE));
	}
}
