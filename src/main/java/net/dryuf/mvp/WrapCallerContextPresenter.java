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
import net.dryuf.core.RoleContext;


public class WrapCallerContextPresenter extends net.dryuf.mvp.ChildPresenter
{
	@SuppressWarnings("unchecked")
	public			WrapCallerContextPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
		this.setCallerContext(RoleContext.createMapped(parentPresenter.getCallerContext(), (String[]) options.getOptionMandatory("rolesMapping")));
		this.subPresenter = (Class<? extends Presenter>) options.getOptionDefault("subPresenter", null);
		this.subOptions = options.getOptionDefault("subOptions", net.dryuf.core.Options.NONE);
	}

	@Override
	public boolean			process()
	{
		if (this.subPresenter != null) {
			Presenter.createSubPresenter(this.subPresenter, this, this.subOptions);
		}
		return super.process();
	}

	public Class<? extends Presenter> subPresenter;
	public Options			subOptions;
};


