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

import java.lang.reflect.Constructor;
import com.google.common.base.Function;

import net.dryuf.core.Options;


public class PresenterElement extends java.lang.Object
{
	protected			PresenterElement(String page, boolean needSlash, String requiredRole, Function<? extends Presenter, Presenter> creator)
	{
		this.page = page;
		this.needSlash = needSlash;
		this.requiredRole = requiredRole;
		this.creator = creator;
	}

	public static PresenterElement	createClassed(String page, boolean needSlash, String requiredRole, final Class<? extends Presenter> presenterClass, final Options options)
	{
		final Constructor<? extends Presenter> constructor;
		try {
			constructor = presenterClass.getConstructor(Presenter.class, Options.class);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new PresenterElement(page, needSlash, requiredRole,  new Function<Presenter, Presenter>() {
			@Override
			public Presenter		apply(Presenter parentPresenter)
			{
				try {
					return ((Presenter)constructor.newInstance(parentPresenter, options)).init();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public static PresenterElement	createFunction(String page, boolean needSlash, String requiredRole, Function<? extends Presenter, Presenter> creator)
	{
		return new PresenterElement(page, needSlash, requiredRole, creator);
	}

	protected String		page;

	public String			getPage()
	{
		return this.page;
	}

	protected boolean		needSlash;

	public boolean			getNeedSlash()
	{
		return this.needSlash;
	}

	protected String		requiredRole;

	public String			getRequiredRole()
	{
		return this.requiredRole;
	}

	protected Function<? extends Presenter, Presenter> creator;

	public Function<? extends Presenter, Presenter> getCreator()
	{
		return this.creator;
	}
}
