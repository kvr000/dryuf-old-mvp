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

package net.dryuf.mvp.proc;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.dryuf.core.Options;
import net.dryuf.mvp.Presenter;
import net.dryuf.srvui.Session;
import net.dryuf.core.StringUtil;


public class SessionPresenter extends net.dryuf.mvp.FinalEmptyXhtmlPresenter
{
	public				SessionPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	@Override
	public void			renderContent()
	{
		Session session = getRootPresenter().getSession();
		if (session == null) {
			this.output("<p/>No session at all.");
			return;
		}

		this.output("<p/>Session info:\n");
		this.output("<table border='1'>\n");
		this.renderRow("sid", this.getRootPresenter().getSession().getSessionId());
		this.renderRow("roles", StringUtil.join(", ", this.getCallerContext().getRoles()));
		this.output("</table>\n");

		if (getCallerContext().checkRole("devel")) {
			this.output("<p/>Session content:");
			this.output("<table border='1'>\n");
			for (Map.Entry<String, Object> entry: session.getAllAttributes().entrySet()) {
				this.outputFormat("<tr><td>%S</td><td>%S</td></tr>\n", entry.getKey(), String.valueOf(entry.getValue()));
			}
			this.output("</table>\n");
		}
	}

	public void			renderRow(String key, String value)
	{
		this.outputFormat("<tr><td>%S</td><td>%S</td></tr>\n", key, StringUtils.defaultString(value, "null"));
	}
}
