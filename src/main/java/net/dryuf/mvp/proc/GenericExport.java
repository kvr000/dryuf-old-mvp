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

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import net.dryuf.core.Options;
import net.dryuf.srvui.Response;


public abstract class GenericExport extends net.dryuf.mvp.ChildPresenter
{
	public static class ModifiedInfo
	{
		public Long			modified = null;
	};

	public				GenericExport(net.dryuf.mvp.Presenter parent_presenter, Options options)
	{
		super(parent_presenter, net.dryuf.core.Options.NONE);
	}

	public boolean			process()
	{
		return this.processFinal();
	}

	public String			getPathInfo()
	{
		this.basepath = this.getRootPresenter().getCurrentPath();
		while ((this.getRootPresenter().getPathElement()) != null) {
		}
		return this.getRootPresenter().getCurrentPath().substring(this.basepath.length());
	}

	public boolean			processCommon()
	{
		String file = getPathInfo();
		rootPresenter.setLeadChild(this);
		try {
			ModifiedInfo modinfo = new ModifiedInfo();
			InputStream content;
			if ((content = this.prepareData(file, modinfo)) != null) {
				Response response = getRootPresenter().getResponse();
				response.setContentType(this.getMimeType());
				IOUtils.copy(content, response.getOutputStream());
			}
			else {
				this.reportNotFound();
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return false;
	}

	public void			reportError(int code, String msg)
	{
		getRootPresenter().getResponse().sendError(code, msg);
	}

	public void			reportNotFound()
	{
		getRootPresenter().getResponse().sendError(404, null);
	}

	public Long			getNotModifiedSince()
	{
		return null;
	}

	public abstract String		getMimeType();

	public abstract InputStream	prepareData(String file, ModifiedInfo modified);

	protected String		basepath;
}
