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

import java.lang.String;

import net.dryuf.net.util.UrlUtil;
import net.dryuf.mvp.Presenter;
import net.dryuf.srvui.PageUrl;
import net.dryuf.web.jee.JeeWebRequest;
import net.dryuf.xml.util.XmlFormat;
import net.dryuf.core.Options;


public class WebRootPresenter extends net.dryuf.mvp.RootPresenter
{
	public				WebRootPresenter(net.dryuf.core.CallerContext callerContext_, JeeWebRequest request_)
	{
		super(callerContext_, request_);
	}

	public String			getContextPath()
	{
		return this.getRequest().getContextPath();
	}

	public String			escapeText(String text)
	{
		return net.dryuf.xml.util.XmlFormat.escapeXml(text);
	}

	@Override
	public Presenter		createNotFoundPresenter()
	{
		return new NotFoundPresenter(new EmptyXhtmlPresenter(this, Options.NONE), Options.NONE);
	}

	@Override
	public Presenter		createDeniedPresenter()
	{
		return new DeniedPresenter(new EmptyXhtmlPresenter(this, Options.NONE), Options.NONE);
	}

	@Override
	public Presenter		createUnallowedMethodPresenter()
	{
		return new UnallowedMethodPresenter(new EmptyXhtmlPresenter(this, Options.NONE), Options.NONE);
	}

	@Override
	public void			output(String text)
	{
		getResponse().getWriter().print(text);
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
					sb.append(XmlFormat.escapeXml((String)args[ai++]));
					break;

				case 'A':
					sb.append("\""); sb.append(XmlFormat.escapeXml((String)args[ai++])); sb.append("\"");
					break;

				case 'E':
					sb.append(XmlFormat.escapeXml(UrlUtil.encodeUrl((String)args[ai++])));
					break;

				case 'J':
					sb.append(XmlFormat.formatJsString((String)args[ai++]));
					break;

				case 'K':
					@SuppressWarnings("unchecked")
					net.dryuf.core.Textual<Object> textual = (net.dryuf.core.Textual<Object>)args[ai++];
					sb.append(XmlFormat.escapeXml(textual.format(args[ai++], null)));
					break;

				case 'O':
					sb.append(XmlFormat.escapeXml(args[ai++].toString()));
					break;

				case 'R':
					throw new RuntimeException("unsupported yet");

				case 'U':
					{
						String url = stringifyRef((PageUrl)args[ai++]);
						sb.append(XmlFormat.escapeXml(url));
					}
					break;

				case 'W':
					{
						Object cls = args[ai++];
						String msg = (String)args[ai++];
						sb.append(XmlFormat.escapeXml(cls instanceof Class ? this.uiContext.localize(((Class<?>)cls).getName(), msg) : this.uiContext.localize((String)cls, msg)));
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
}
