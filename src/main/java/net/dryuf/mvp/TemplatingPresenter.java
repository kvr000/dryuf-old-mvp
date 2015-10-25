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
import net.dryuf.mvp.ChildPresenter;
import net.dryuf.mvp.Presenter;
import net.dryuf.xml.util.XmlFormat;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;


public abstract class TemplatingPresenter extends ChildPresenter
{
	public				TemplatingPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);

		replaceDirect = options.getOptionDefault("replaceDirect", Collections.<String, String>emptyMap());
		replaceEscape = options.getOptionDefault("replaceEscape", Collections.<String, String>emptyMap());
	}

	public void			render()
	{
		String content = readResource();
		for (Map.Entry<String, String> replaceEntry: replaceDirect.entrySet()) {
			content = Pattern.compile(replaceEntry.getKey(), Pattern.LITERAL).matcher(content).replaceAll(replaceEntry.getValue());
		}
		for (Map.Entry<String, String> replaceEntry: replaceEscape.entrySet()) {
			content = Pattern.compile(replaceEntry.getKey(), Pattern.LITERAL).matcher(content).replaceAll(XmlFormat.escapeXml(replaceEntry.getValue()));
		}
		output(content);
	}

	public abstract String		readResource();

	protected Map<String, String>	replaceDirect;

	protected Map<String, String>	replaceEscape;
}