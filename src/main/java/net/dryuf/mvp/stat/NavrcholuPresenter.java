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


public class NavrcholuPresenter extends net.dryuf.mvp.NoLeadChildPresenter
{
	public				NavrcholuPresenter(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);

		this.nav_site = (String) options.getOptionMandatory("site");
		this.nav_t = (String) options.getOptionMandatory("t");
	}

	public void			render()
	{
		this.outputFormat(
			"<script src=\"http://c1.navrcholu.cz/code?site=%S;t=%S\" type=\"text/javascript\"></script><noscript><div><a href=\"http://navrcholu.cz/\"><img src=\"http://c1.navrcholu.cz/hit?site=%S;t=%S;ref=;jss=0\" width=\"24\" height=\"24\" alt=\"NAVRCHOLU.cz\" style=\"border:none\" /></a></div></noscript>",
			this.nav_site, this.nav_t,
			this.nav_site, this.nav_t
		);
	}

	protected String		nav_site;
	protected String		nav_t;
};
