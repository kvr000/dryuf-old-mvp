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

package net.dryuf.mvp.jsuse.yui;

import net.dryuf.core.Options;
import net.dryuf.mvp.Presenter;
import net.dryuf.srvui.PageUrl;
import net.dryuf.mvp.RootPresenter;
import net.dryuf.core.StringUtil;


public class YuiJsRegister extends net.dryuf.mvp.NoLeadChildPresenter
{
	private Options			options;

	public				YuiJsRegister(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
		this.options = options;
	}

	public void			prepare()
	{
		RootPresenter rootPresenter = getRootPresenter();
		rootPresenter.addLinkedFile("js", PageUrl.createRooted("/yui/"+"yui-base/yui-base-debug.js"));
		for (String name: options.getOptionDefault("jsFiles", StringUtil.STRING_EMPTY_ARRAY)) {
			rootPresenter.addLinkedFile("js", PageUrl.createRooted("/yui/"+name));
		}
		for (String name: options.getOptionDefault("cssFiles", StringUtil.STRING_EMPTY_ARRAY)) {
			rootPresenter.addLinkedFile("css", PageUrl.createRooted("/yui/"+name));
		}
		super.prepare();
	}
};
