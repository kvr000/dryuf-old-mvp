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

package net.dryuf.datagrid;


public class DataPresenter_ModeCreate <T> extends net.dryuf.datagrid.DataPresenter_SubMode<T>
{
	public				DataPresenter_ModeCreate(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
	}

	public void			prepare()
	{
		createSubPresenter(net.dryuf.mvp.jsuse.dryuf.DryufJsRegister.class, this, net.dryuf.core.Options.NONE).prepare();
		super.prepare();
	}

	public void			render()
	{
		this.outputFormat(
			"<div id=\"net-dryuf-datagrid-DataPresenter-new\">\n"+
			"<script type=\"text/javascript\">\n"+
			"net.dryuf.requireAsync(function() {\n"+
			"		new net.dryuf.datagrid.DataGridPresenter(\n"+
			"			null,\n"+
			"			document.getElementById(\"net-dryuf-datagrid-DataPresenter-new\"),\n"+
			"			{\n"+
			"				dataClassName:		%J,\n"+
			"				mode:			'new',\n"+
			"				rpcPath:		%J,\n"+
			"				manager:		new net.dryuf.datagrid.DataGridRedirManager(null, null, { redirTarget: \"../\", }),\n"+
			"			}\n"+
			"		);\n"+
			"	},\n"+
			"	'net.dryuf.datagrid.DataGridRedirManager',\n"+
			"	'net.dryuf.datagrid.DataGridPresenter');\n"+
			"</script>\n"+
			"</div>\n",
			this.getDataPresenter().getDataClass().getName(),
			"../-oper-/"
		);
	}
}
