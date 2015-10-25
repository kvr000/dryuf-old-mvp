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

package net.dryuf.mvp.tenv;

import net.dryuf.core.Options;
import net.dryuf.mvp.ChildPresenter;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.DeniedPresenter;
import net.dryuf.mvp.NotFoundPresenter;



public class TestPresenter extends ChildPresenter
{
	public static final int		PS_Std				= 0;
	public static final int		PS_NotFound			= 1;
	public static final int		PS_Denied			= 2;

	public				TestPresenter(Presenter parentPresenter_, Options options)
	{
		super(parentPresenter_, options);
	}

	@Override
	public Presenter		createNotFoundPresenter()
	{
		state = PS_NotFound;
		return new NotFoundPresenter(this, Options.NONE);
	}

	@Override
	public Presenter		createDeniedPresenter()
	{
		state = PS_Denied;
		return new DeniedPresenter(this, Options.NONE);
	}

	public int			getState()
	{
		return state;
	}

	protected int			state = PS_Std;
};
