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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.dryuf.srvui.Request;
import org.apache.commons.codec.Charsets;

import net.dryuf.core.CallerContext;


public class DummyRootPresenter extends net.dryuf.mvp.CommonRootPresenter
{
	public				DummyRootPresenter(CallerContext callerContext, Request request)
	{
		super(callerContext, request);
	}

	static public RootPresenter	createFullyDummy(CallerContext callerContext)
	{
		DummyRootPresenter presenter = new DummyRootPresenter(callerContext, new net.dryuf.srvui.DummyRequest());
		presenter.initUiContext("");
		return presenter;
	}

	public Presenter		createDeniedPresenter()
	{
		throw new UnsupportedOperationException("createDeniedPresenter");
	}

	public Presenter		createNotFoundPresenter()
	{
		throw new UnsupportedOperationException("createNotFoundPresenter");
	}

	public Presenter		createUnallowedMethodPresenter()
	{
		throw new UnsupportedOperationException("createUnallowedMethodPresenter");
	}

	@Override
	public void			output(String text)
	{
		try {
			outputStream.write(text.getBytes(Charsets.UTF_8));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ByteArrayOutputStream	getOutputStream()
	{
		return outputStream;
	}

	public byte[]			getOutput()
	{
		return outputStream.toByteArray();
	}

	protected ByteArrayOutputStream	outputStream = new ByteArrayOutputStream();
}
