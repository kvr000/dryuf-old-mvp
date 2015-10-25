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
import java.util.Map;
import org.apache.commons.codec.Charsets;
import net.dryuf.core.StringUtil;


public class TranslationExport extends net.dryuf.mvp.proc.JsonCachedExport
{
	public				TranslationExport(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
	}

	public InputStream		prepareData(String file, ModifiedInfo modified)
	{
		String classname = file;

		String[] match;
		if ((match = StringUtil.matchText("^((\\w+\\.)*\\w+)/$", classname)) == null) {
			throw new net.dryuf.core.ReportException("invalid classname: "+classname);
		}

		return this.getCached("default", match[1], "translation", modified);
	}

	public InputStream		buildData(String subid, String classname, String ext)
	{
		StringBuilder out = new StringBuilder("{");
		for (Map.Entry<String, String> entry: getUiContext().getClassLocalization(classname).entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			out.append("\"").append(key.replace("\"", "\\\"")).append("\":\"").append(value.replace("\"", "\\\"")).append("\",");
		}
		if (out.length() > 1)
			out.setLength(out.length()-1);
		out.append("}");
		return new ByteArrayInputStream(out.toString().getBytes(Charsets.UTF_8));
	}
}
