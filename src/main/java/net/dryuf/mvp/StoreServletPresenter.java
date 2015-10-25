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

import java.net.URLConnection;

import net.dryuf.core.Options;
import net.dryuf.io.FileData;
import net.dryuf.service.file.FileStoreService;
import net.dryuf.mvp.Presenter;
import net.dryuf.text.mime.MimeTypeService;


public class StoreServletPresenter extends net.dryuf.mvp.GenericFilePresenter
{
	public				StoreServletPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
		fileStoreService = getCallerContext().getBeanTyped((String) options.getOptionMandatory("fileStoreService"), FileStoreService.class);
		if ((basePath = (String)options.getOptionDefault("basePath", null)) == null)
			basePath = parentPresenter.getRootPresenter().getLastElement();
		if ((fileName = (String)options.getOptionDefault("fileName", null)) == null)
			fileName = parentPresenter.getRootPresenter().getLastElement();
		clientName = (String)options.getOptionDefault("clientName", null);
	}

	@Override
	protected String		getForcedContentType()
	{
		return clientName != null ? (getCallerContext().getBeanTyped("mimeTypeService", MimeTypeService.class)).guessContentTypeFromName(clientName) : null;
	}

	@Override
	protected FileData		resolveFileData()
	{
		return fileStoreService.getFile(basePath, fileName);
	}

	protected String		basePath;

	protected String		fileName;

	protected String		clientName;

	protected FileStoreService	fileStoreService;
}
