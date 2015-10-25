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

package net.dryuf.mvp.jsuse.dryuf;

import net.dryuf.core.Options;
import net.dryuf.srvui.PageUrl;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.RootPresenter;
import net.dryuf.mvp.MainXhtmlPresenter;
import net.dryuf.xml.util.XmlFormat;


public class DryufJsRegister extends net.dryuf.mvp.NoLeadChildPresenter
{
	private Options			options;

	public				DryufJsRegister(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
		this.options = options;
	}

	public void			prepare()
	{
		RootPresenter rootPresenter = getRootPresenter();
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/dryuf.js"));
		this.getRootPresenter().addLinkedContent("js", "drufinit", "net.dryuf.serverPath=\""+rootPresenter.getContextPath()+"/\";");
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/core/ParallelSync.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/gui/GuiDom.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/xml/DomUtil.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/core/Base64.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/core/Eval.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/core/Ajax.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/core/RoleContext.js"));
		this.getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/net/dryuf/core/RoleContextHolder.js"));
		StringBuilder sb = new StringBuilder();
		sb.append("net.dryuf.core.RoleContextHolder.setSysRole(new net.dryuf.core.RoleContext(null, [ ");
		for (String rname: this.getCallerContext().getRoles())
			sb.append("\"").append(XmlFormat.escapeXml(rname)).append("\", ");
		sb.append("]));");
		this.getRootPresenter().addLinkedContent("js", "drufrole", sb.toString());
		for (String name: options.getOptionDefault("jsFiles", new String[0])) {
			rootPresenter.addLinkedFile("js", PageUrl.createRooted("/resources/"+name));
		}
		if (getCallerContext().getConfigValue("net.dryuf.js.forceFull", false))
			((MainXhtmlPresenter)rootPresenter.getLeadChild()).prepareJsPackage("org.dryuf");
		super.prepare();
	}
}
