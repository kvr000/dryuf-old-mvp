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

import net.dryuf.core.CallerContext;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Session;


public abstract class CommonRootPresenter extends net.dryuf.mvp.RootPresenter
{
	public				CommonRootPresenter(CallerContext callerContext, Request request)
	{
		super(callerContext, request);
	}

	public void			initUiContext(String contextPath)
	{
		this.getUiContext().setLocalizeContextPath(contextPath);
		this.uiContext.setLanguage(this.request.getParamDefault("lang", this.uiContext.getDefaultLanguage()));
	}

	public Session			getSession()
	{
		return getRequest().getSession();
	}

	public Session			forceSession()
	{
		return getRequest().forceSession();
	}

	@Override
	public void			outputFormat(String fmt, Object... args)
	{
		StringBuilder sb = new StringBuilder();
		int ai = 0;
		for (int i = 0; i < fmt.length(); i++) {
			if (fmt.charAt(i) == '%') {
				switch (fmt.charAt(++i)) {
				case '%':
					sb.append('%');
					break;

				case 's':
					sb.append((String)args[ai++]);
					break;

				case 'S':
					sb.append(escapeText((String)args[ai++]));
					break;

				case 'A':
					sb.append("\""); sb.append((String)args[ai++]); sb.append("\"");
					break;

				case 'K':
					@SuppressWarnings("unchecked")
					net.dryuf.core.Textual<Object> textual = (net.dryuf.core.Textual<Object>)args[ai++];
					sb.append(escapeText(textual.format(args[ai++], null)));
					break;

				case 'O':
					sb.append(escapeText(args[ai++].toString()));
					break;

				case 'R':
					throw new RuntimeException("unsupported yet");

				case 'U':
					throw new RuntimeException("unsupported yet");

				case 'W':
					{
						Object cls = args[ai++];
						String msg = (String)args[ai++];
						sb.append(escapeText(this.uiContext.localize((String)cls, msg)));
					}
					break;

				default:
					throw new RuntimeException("invalid format character: "+fmt.charAt(i-1));
				}
			}
			else {
				sb.append(fmt.charAt(i));
			}
		}
		output(sb.toString());
	}

	public String			escapeText(String text)
	{
		return text;
	}

	public Presenter		createNotFoundPresenter()
	{
		throw new UnsupportedOperationException("NotFoundPresenter unsupported");
	}

	public Presenter		createDeniedPresenter()
	{
		throw new UnsupportedOperationException("DeniedPresenter unsupported");
	}

	public Presenter		createUnallowedMethodPresenter()
	{
		throw new UnsupportedOperationException("UnallowedMethodPresenter unsupported");
	}
};
