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

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import net.dryuf.core.Options;
import net.dryuf.io.FileData;
import net.dryuf.core.StringUtil;
import net.dryuf.srvui.Request;
import net.dryuf.srvui.Response;


public abstract class GenericFilePresenter extends net.dryuf.mvp.ChildPresenter
{
	public				GenericFilePresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	protected String		getForcedContentType()
	{
		return null;
	}

	protected abstract FileData	resolveFileData();

	protected long			getCacheTimeout()
	{
		return getCallerContext().getConfigValue("net.dryuf.mvp.cachePeriod", Long.valueOf(24*3600*1000));
	}

	public boolean			processCommon()
	{
		FileData fileData;
		if ((fileData = resolveFileData()) == null) {
			if (getResponse().getCurrentStatus() == 0)
				parentPresenter.createNotFoundPresenter();
			return errorReturn;
		}
		try {
			serveFile(fileData);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			fileData.close();
		}
		return false;
	}

	protected void			writeFile(FileData fileData) throws IOException
	{
		if (rangeStart >= 0) {
			IOUtils.copyLarge(fileData.getInputStream(), getResponse().getOutputStream(), rangeStart, rangeEnd-rangeStart);
		}
		else {
			IOUtils.copy(fileData.getInputStream(), getResponse().getOutputStream());
		}
	}

	protected void			serveFile(FileData fileData) throws IOException
	{
		Request request = getRequest();
		Response response = getResponse();

		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		String range = request.getHeader("Range");

		String[] rangeMatch;
		if (range != null && fileData.getSize() >= 0) {
			if ((rangeMatch = StringUtil.matchText("^bytes=(\\d*)-(\\d*)$", range)) == null)
				throw new RuntimeException("Invalid Range header: "+range);
			rangeStart = rangeMatch[1].isEmpty() ? -1 : Long.valueOf(rangeMatch[1]);
			rangeEnd = rangeMatch[2].isEmpty() ? -1 : Long.valueOf(rangeMatch[2]);
			if (rangeStart < 0) {
				if (rangeEnd < 0)
					throw new RuntimeException("Invalid Range header, both start and end are empty: "+range);
				rangeStart = fileData.getSize()-rangeEnd;
				rangeEnd = fileData.getSize();
			}
			else if (rangeEnd < 0) {
				rangeEnd = fileData.getSize();
			}
			else {
				++rangeEnd;
			}
			if (rangeStart < 0 || rangeEnd > fileData.getSize() || rangeStart > rangeEnd) {
				// note that we consider rangeStart(inclusive) == rangeEnd(exclusive) as valid request for 0 bytes
				response.sendStatus(416, "Requested Range Not Satisfiable");
				response.setHeader("Content-Range", "*/"+fileData.getSize());
				return;
			}
		}

		long modified = fileData.getModifiedTime();

		if (ifModifiedSince > 0 && modified > 0 && fileData.getModifiedTime() <= ifModifiedSince) {
			long cachePeriod;
			response.setDateHeader("Last-Modified", modified);
			response.sendStatus(304, "Not Modified");
			if ((cachePeriod = getCacheTimeout()) > 0) {
				response.setHeader("Cache-Control", "max-age="+cachePeriod/1000);
				response.setDateHeader("Expires", System.currentTimeMillis()+cachePeriod);
			}
			return;
		}
		else {
			long cachePeriod;
			String contentType = getForcedContentType();
			if (contentType == null && (contentType = fileData.getContentType()) == null)
				contentType = "application/octet-stream";
			response.setContentType(contentType);
			long size = fileData.getSize();
			if (rangeStart >= 0) {
				response.setHeader("Content-Range", "bytes "+rangeStart+"-"+(rangeEnd-1)+"/"+size);
				response.setLongHeader("Content-Length", rangeEnd-rangeStart);
			}
			else if (size >= 0) {
				response.setLongHeader("Content-Length", size);
			}
			if ((cachePeriod = getCacheTimeout()) > 0) {
				response.setHeader("Cache-Control", "max-age="+cachePeriod/1000);
				response.setDateHeader("Expires", System.currentTimeMillis()+cachePeriod);
			}
			if (modified >= 0)
				response.setDateHeader("Last-Modified", modified);
			if (!getRequest().getMethod().equals("HEAD"))
				writeFile(fileData);
		}
	}

	/** start of range header (or -1) */
	protected long			rangeStart = -1;

	/** end of range header (or -1) */
	protected long			rangeEnd = -1;

	/** force return value from process */
	protected boolean		errorReturn = true;
}
