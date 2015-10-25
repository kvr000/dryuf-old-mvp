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

package net.dryuf.mvp.proc;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import net.dryuf.core.Options;
import net.dryuf.io.FileData;
import net.dryuf.service.image.CaptchaService;
import net.dryuf.srvui.Response;
import net.dryuf.mvp.GenericFilePresenter;
import net.dryuf.mvp.FormPresenter;


public class CaptchaExport extends GenericFilePresenter
{
	public				CaptchaExport(net.dryuf.mvp.Presenter parent_presenter, Options options)
	{
		super(parent_presenter, net.dryuf.core.Options.NONE);
	}

	@Override
	protected FileData		resolveFileData()
	{
		if (getRootPresenter().getPathElement() != null)
			return null;
		CaptchaService captchaService = getCallerContext().getBeanTyped("captchaService", CaptchaService.class);
		FileData captchaFile = captchaService.generateCaptcha();
		FormPresenter.setRequestCaptcha(getRequest(), captchaFile.getName());
		return captchaFile;
	}

	@Override
	protected void			serveFile(FileData fileData) throws IOException
	{
		Response response = getResponse();

		response.setContentType(fileData.getContentType());
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, no-store");
		long size = fileData.getSize();
		if (size >= 0)
			response.setLongHeader("Content-Length", size);

		IOUtils.copy(fileData.getInputStream(), response.getOutputStream());
	}
}
