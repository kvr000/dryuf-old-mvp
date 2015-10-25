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

package net.dryuf.mvp.servlet.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

/**
 * Implementation of LocaleResolver that uses a locale attribute in the
 * request, set by filter
 */
public class RequestAttributeLocaleResolver extends AbstractLocaleResolver
{
	protected Logger		logger = LogManager.getLogger(getClass().getName());

	public				RequestAttributeLocaleResolver(String locales, String defaultLocale)
	{
		for (String localePair: locales.split(",\\s*")) {
			String[] localeDef = localePair.split(":\\s*");
			if (localeDef.length != 2)
				throw new ArrayIndexOutOfBoundsException("locale definition must consist of key:locale comma separated pairs: '"+localePair+"', locales='"+locales+"', defaultLocale='"+defaultLocale+"'");
			registeredLocales.put(localeDef[0], new Locale(localeDef[1]));
		}
		Locale localeDef = registeredLocales.get(defaultLocale);
		logger.info("using default locale: "+localeDef.getLanguage());
		setDefaultLocale(registeredLocales.get(defaultLocale));
	}

	@Override
	public Locale			resolveLocale(HttpServletRequest request)
	{
		String localeStr = (String)request.getAttribute("language");
		Locale locale = null;
		if (localeStr != null) {
			locale = registeredLocales.get(localeStr);
		}
		if (locale == null) {
			locale = determineDefaultLocale(request);
		}
		//logger.info("using locale "+locale.getLanguage());
		return locale;
	}

	/**
	 * Determine the default locale for the given request,
	 * Called if no locale session attribute has been found.
	 * <p>The default implementation returns the specified default locale,
	 * if any, else falls back to the request's accept-header locale.
	 * @param request the request to resolve the locale for
	 * @return the default locale (never <code>null</code>)
	 * @see #setDefaultLocale
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	protected Locale		determineDefaultLocale(HttpServletRequest request)
	{
		return  getDefaultLocale();
	}

	@Override
	public void			setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
	{
		// ignore
		//logger.info("setting locale "+locale.getLanguage());
	}

	Map<String, Locale>		registeredLocales = new HashMap<String, Locale>();
}
