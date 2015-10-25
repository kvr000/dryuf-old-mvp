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

import net.dryuf.app.ClassMeta;
import net.dryuf.dao.DynamicDao;


public abstract class DataPresenter_SubMode<T> extends net.dryuf.mvp.ChildPresenter
{
	public				DataPresenter_SubMode(net.dryuf.mvp.Presenter parentPresenter, net.dryuf.core.Options options)
	{
		super(parentPresenter, options);
		this.dataProvider = getDataPresenter().getDataProvider();
		this.classMeta = getDataPresenter().getClassMeta();
		this.mode = getDataPresenter().getMode();
	}

	@SuppressWarnings("unchecked")
	public DataPresenter<T>		getDataPresenter()
	{
		return (DataPresenter<T>)getParentPresenter();
	}

	protected String		mode;
	protected DynamicDao<T, ?>	dataProvider;
	protected ClassMeta<T>		classMeta;
};


