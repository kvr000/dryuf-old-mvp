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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.dryuf.core.StringUtil;
import net.dryuf.net.util.UrlUtil;
import net.dryuf.srvui.PageUrl;
import net.dryuf.xml.util.XmlFormat;


public class NeedLoginPresenter extends net.dryuf.mvp.ChildPresenter
{
	public				NeedLoginPresenter(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
		Object messageObj = options.getOptionDefault("messageClass", NeedLoginPresenter.class.getName());
		this.messageClass = messageObj instanceof String ? (String)messageObj : ((Class<?>)messageObj).getName();
		this.message = options.getOptionDefault("message", "You need to --login-- to continue.");
	}

	public void			render()
	{
		super.render();
		message = XmlFormat.escapeXml(this.localize(this.messageClass, this.message));
		String[] split;
		if ((split = StringUtil.matchText("^(.*)--(.*?)--(.*)$", message)) != null) {
			try {
				message = split[1]+"<a href=\""+XmlFormat.escapeXml(UrlUtil.appendQuery(this.getRootPresenter().stringifyRef(PageUrl.createPaged("login")), "redir=/"+URLEncoder.encode(this.getRootPresenter().getCurrentPath(), "UTF-8")))+"\">"+split[2]+"</a>"+split[3];
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		this.output("<p>"+message+"</p>\n");
	}

	public String			message;
	public String			messageClass;
};
