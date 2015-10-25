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


public abstract class AbstractPresenterDivider extends java.lang.Object implements PresenterDivider
{
	public Presenter		tryPage(Presenter presenter)
	{
		return this.tryPageConsumed(presenter, presenter.getRootPresenter().getPathElement());
	}

	public boolean			process(Presenter presenter)
	{
		return this.processConsumed(presenter, presenter.getRootPresenter().getPathElement());
	}

	public boolean			processConsumed(Presenter presenter, String page)
	{
		Presenter subPresenter;
		if ((subPresenter = this.tryPageConsumed(presenter, page)) != null) {
			return subPresenter.process();
		}
		return presenter.createNotFoundPresenter().process();
	}

	public Presenter		tryLangConsumed(Presenter presenter, String page)
	{
		if (page != null) {
			if (!presenter.getUiContext().checkLanguage(page))
				return presenter.createNotFoundPresenter();
			presenter.getRootPresenter().setRealPath("");
			page = presenter.getRootPresenter().getPathElement();
		}
		else {
			presenter.getRootPresenter().setRealPath("");
		}
		Presenter child = tryPageConsumed(presenter, page);
		return child;
	}

	protected void			setPresenterTitle(Presenter presenter, String page)
	{
		if (pageLocalizeClass != null) {
			if (page.isEmpty()) {
				presenter.setTitle("");
			}
			else {
				String title;
				if (!(title = presenter.localize(pageLocalizeClass, page)).isEmpty())
					presenter.setTitle(title);
			}
		}
	}

	protected Class<?>		pageLocalizeClass;

	public AbstractPresenterDivider	setPageLocalizeClass(Class<?> pageLocalizeClass_)
	{
		this.pageLocalizeClass = pageLocalizeClass_;
		return this;
	}
}
