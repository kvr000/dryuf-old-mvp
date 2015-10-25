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

package net.dryuf.mvp.stat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class W3cHtmlPresenter extends W3cXhtmlPresenter
{
	public				W3cHtmlPresenter(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
	}

	public void			render()
	{
		try {
			this.outputFormat("<a href=\"http://validator.w3.org/check?uri=%S;doctype=Inline\"><img border=\"0\" src=\"http://www.w3.org/Icons/valid-html401-%s\" alt=\"Valid HTML 4.01!\" height=\"31\" width=\"88\" /></a>",
				URLEncoder.encode(this.getRootPresenter().getFullUrl(), "UTF-8"), this.w3cColor);
		}
		catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
}
