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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.dryuf.oper.MetaExport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.Charsets;

import net.dryuf.core.ReportException;
import net.dryuf.core.StringUtil;


public class MetaExportPresenter extends net.dryuf.mvp.proc.XmlCachedExport
{
	public				MetaExportPresenter(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
	}

	@Override
	public InputStream		prepareData(String file, ModifiedInfo modinfo)
	{
		String[] match;
		if ((match = StringUtil.matchText("^((\\w+\\.)*\\w+)/$", file)) == null)
			throw new ReportException("invalid object name: "+file);
		file = match[1];
		String viewName = StringUtils.defaultString(getRequest().getParam("view"), "Default");
		if (StringUtil.matchText("^\\w+$", viewName) == null)
			throw new ReportException("invalid object name: "+file);

		return this.getCached(viewName, file, "xml", modinfo);
	}

	public InputStream		buildData(String viewName, String className, String ext)
	{
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			return null;
		}
		return new ByteArrayInputStream(MetaExport.buildMeta(this.getCallerContext(), clazz, viewName, getRootPresenter().getContextPath()+"/_oper/"+className+"/").getBytes(Charsets.UTF_8));
	}
}
