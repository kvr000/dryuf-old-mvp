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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import net.dryuf.web.jee.JeeWebResponse;


public abstract class CachedExport extends net.dryuf.mvp.proc.GenericExport
{
	public				CachedExport(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
	}

	public boolean			processCommon()
	{
		String file = this.getPathInfo();
		ModifiedInfo modinfo = new ModifiedInfo();
		InputStream content;
		JeeWebResponse webResponse = (JeeWebResponse)getRootPresenter().getResponse();
		if ((content = this.prepareData(file, modinfo)) != null) {
			webResponse.setContentType(this.getMimeType());
			try {
				IOUtils.copy(content, webResponse.getOutputStream());
			}
			catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		else {
			if (modinfo.modified == null) {
				this.reportNotFound();
			}
			else {
				webResponse.sendError(304, "Not Modified");
			}
		}
		return false;
	}

	public InputStream		getCached(String subid, String obj_name, String ext, ModifiedInfo modinfo)
	{
		return this.buildData(subid, obj_name, ext);
	}

	public abstract InputStream	buildData(String subid, String obj_name, String ext);

	public boolean			nocache;
}
