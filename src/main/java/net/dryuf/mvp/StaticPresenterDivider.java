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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;


public class StaticPresenterDivider extends AbstractPresenterDivider
{
	public				StaticPresenterDivider(PresenterElement[] elements)
	{
		divider = new HashMap<String, PresenterElement>();
		for (PresenterElement element: elements) {
			divider.put(element.getPage(), element);
		}
	}

	public Set<String>		getPageList()
	{
		return this.divider.keySet();
	}

	public int			checkPage(Presenter presenter, String page)
	{
		if (page == null)
			page = "";
		PresenterElement pageDef = this.divider.get(page);
		if (pageDef == null)
			return 0;
		if (!presenter.getCallerContext().checkRole(pageDef.getRequiredRole())) {
			return -1;
		}
		return 1;
	}

	public Presenter		tryPageConsumed(Presenter presenter, String page)
	{
		RootPresenter rootPresenter = presenter.getRootPresenter();
		if (page == null)
			page = "";
		PresenterElement pageDef = this.divider.get(page);
		if (pageDef == null)
			return null;
		if (rootPresenter.needPathSlash(pageDef.getNeedSlash()) == null) {
			return new net.dryuf.mvp.BoolDummyPresenter(presenter, !pageDef.getNeedSlash());
		}
		else if (!presenter.getCallerContext().checkRole(pageDef.getRequiredRole())) {
			return presenter.createDeniedPresenter();
		}
		else {
			@SuppressWarnings("unchecked")
			Presenter child = ((Function<Presenter, Presenter>)pageDef.getCreator()).apply(presenter);
			setPresenterTitle(presenter, page);
			return child;
		}
	}

	protected Map<String, PresenterElement> divider;
}
