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

package net.dryuf.menu.mvp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import net.dryuf.core.AppContainer;
import net.dryuf.core.AppContainerAware;
import net.dryuf.core.Options;
import net.dryuf.menu.WebAccessiblePage;
import net.dryuf.menu.dao.WebAccessiblePageDao;
import net.dryuf.mvp.AbstractPresenterDivider;
import net.dryuf.mvp.Presenter;


public class PreloadedWebMenuPresenterDivider extends AbstractPresenterDivider implements AppContainerAware
{
	public				PreloadedWebMenuPresenterDivider()
	{
	}

	@Override
	public void			afterAppContainer(@NotNull AppContainer appContainer)
	{
		if (webProvider == null)
			throw new IllegalArgumentException("webProvider not specified");
		rootPages = new HashMap<String, WebAccessiblePage>();
		for (WebAccessiblePage page: webAccessiblePageDao.listByCompos(webProvider)) {
			if (page.getPageCode().indexOf("/") < 0)
				rootPages.put(page.getPageCode(), page);
		}
	}

	@Override
	public Set<String>		getPageList()
	{
		throw new UnsupportedOperationException("not supported yet");
	}

	@Override
	public int			checkPage(Presenter presenter, String page)
	{
		if (page == null)
			page = "";

		WebAccessiblePage impl = rootPages.get(page);
		if (impl != null) {
			if (!presenter.getCallerContext().checkRole(impl.getReqRole())) {
				return -1;
			}
			else {
				return 1;
			}
		}
		else {
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Presenter		tryPageConsumed(Presenter presenter, String page)
	{
		if (page == null)
			page = "";

		WebAccessiblePage impl = rootPages.get(page);
		if (impl != null) {
			Presenter child;
			if (presenter.getRootPresenter().needPathSlash(impl.getNeedSlash()) == null)
				return new net.dryuf.mvp.BoolDummyPresenter(presenter, !impl.getNeedSlash());
			if (!presenter.getCallerContext().checkRole(impl.getReqRole())) {
				return presenter.createDeniedPresenter();
			}
			try {
				child = Presenter.createSubPresenter((Class<? extends Presenter>)Class.forName(impl.getPresenterImpl()), presenter, Options.NONE);
			}
			catch (ClassNotFoundException ex) {
				throw new RuntimeException(ex);
			}
			setPresenterTitle(presenter, page);
			return child;
		}
		else {
			return null;
		}
	}

	protected Map<String, WebAccessiblePage> rootPages;

	protected String		webProvider;

	public void			setWebProvider(String webProvider_)
	{
		this.webProvider = webProvider_;
	}

	@Inject
	WebAccessiblePageDao		webAccessiblePageDao;
}
