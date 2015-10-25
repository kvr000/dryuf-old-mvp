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

import java.util.Set;


public interface PresenterDivider
{
	/**
	 * Gets list of all pages.
	 *
	 * @return
	 * 	set of all pages
	 */
	public Set<String>		getPageList();

	/**
	 * Checks for page definition.
	 *
	 * @return 1
	 * 	if the page is accessible
	 * @return 0
	 * 	if the page does not exist
	 * @return -1
	 * 	if the access is denied
	 */
	public int			checkPage(Presenter presenter, String page);

	/**
	 * Checks for page definition, including all the steps until process()+
	 * @return
	 * 	created page
	 * @return null
	 * 	if not found
	 */
	public Presenter		tryPage(Presenter presenter);

	/**
	 * Checks for page definition, including all the steps until process()+
	 * Processes already retrieved path element+
	 * @return
	 * 	created page
	 * @return null
	 * 	if not found
	 */
	public Presenter		tryPageConsumed(Presenter presenter, String page);

	/**
	 * Checks for page definition or language, including all the steps until process()+
	 * Processes already retrieved path element+
	 * @return
	 * 	created page
	 * @return null
	 * 	if the passed page was language and it was consumed
	 */
	public Presenter		tryLangConsumed(Presenter presenter, String page);

	/**
	 * Process the current URL and create appropriate structure.
	 *
	 * @param presenter
	 * 	owning presenter
	 *
	 * @return
	 * 	value returned from created presenter process call
	 */
	public boolean			process(Presenter presenter);

	/**
	 * Process the passed URL element and create appropriate structure.
	 *
	 * @param presenter
	 * 	owning presenter
	 * @param page
	 * 	next element to process
	 *
	 * @return
	 * 	value returned from created presenter process call
	 */
	public boolean			processConsumed(Presenter presenter, String page);
}
