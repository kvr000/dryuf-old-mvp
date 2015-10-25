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

import org.apache.commons.io.FilenameUtils;

import net.dryuf.core.Options;
import net.dryuf.io.ResourceResolver;
import net.dryuf.io.FileData;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.RootPresenter;
import net.dryuf.mvp.GenericFilePresenter;


public class ResourcesPresenter extends GenericFilePresenter
{
	public				ResourcesPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);

		if ((this.basePath = options.getOptionDefault("basePath", null)) == null)
			basePath = getRootPresenter().getRealPath();
		String resourceResolverName = options.getOptionDefault("resourceResolverName", "resourceResolver");
		resourceResolver = getCallerContext().getBeanTyped(resourceResolverName, ResourceResolver.class);
	}

	@Override
	public boolean			process()
	{
		return this.processFinal();
	}

	protected long			getCacheTimeout()
	{
		return resourceResolver.getCacheTimeout(fileExtension);
	}

	protected String		getPathInfo()
	{
		RootPresenter rootPresenter = getRootPresenter();
		this.urlBase = rootPresenter.getCurrentPath();
		while ((rootPresenter.getPathElementSafe()) != null) {
		}
		String currentPath = rootPresenter.getCurrentPath();
		fileExtension = FilenameUtils.getExtension(currentPath);
		String fullPath = basePath+currentPath.substring(this.urlBase.length());
		if (resourceResolver.checkFileType(fullPath) == 0) {
			if (rootPresenter.needPathSlash(true) == null)
				return null;
			return handlePathDirectory(fullPath);
		}
		return fullPath;
	}

	protected String		handlePathDirectory(String path)
	{
		throw new RuntimeException("directory listing unsupported");
	}

	@Override
	protected FileData		resolveFileData()
	{
		String path = getPathInfo();
		if (path == null) {
			errorReturn = false;
			return null;
		}
		FileData fileData;
		if ((fileData = resourceResolver.getResource(path)) == null)
			return null;
		return fileData;
	}

	@Override
	protected void			writeFile(FileData fileData) throws IOException
	{
		//if (resourceResolver.getCompressPolicy(fileExtension) != null) {
		//}
		super.writeFile(fileData);
	}

	protected String		urlBase;

	public String			getUrlBase()
	{
		return this.urlBase;
	}

	protected String		basePath;

	public String			getBasePath()
	{
		return this.basePath;
	}

	protected String		fileExtension;

	public String			getFileExtension()
	{
		return this.fileExtension;
	}

	protected ResourceResolver	resourceResolver;
}
