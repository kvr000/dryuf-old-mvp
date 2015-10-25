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

import net.dryuf.core.Options;
import net.dryuf.io.ResourceResolver;
import net.dryuf.srvui.MetaTag;
import net.dryuf.srvui.PageUrl;
import net.dryuf.srvui.PendingMessage;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.menu.VerticalMenuRenderer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainXhtmlPresenter extends net.dryuf.mvp.ChildPresenter
{
	public				MainXhtmlPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
	}

	@Override
	public Presenter		createNotFoundPresenter()
	{
		return new net.dryuf.mvp.NotFoundPresenter(this, Options.NONE);
	}

	@Override
	public Presenter		createDeniedPresenter()
	{
		return new net.dryuf.mvp.DeniedPresenter(this, Options.NONE);
	}

	@Override
	public Presenter		createUnallowedMethodPresenter()
	{
		return new net.dryuf.mvp.UnallowedMethodPresenter(this, Options.NONE);
	}

	public void			headDocumentUtf8()
	{
		this.getRootPresenter().getResponse().setContentType("application/xhtml+xml; charset=UTF-8");
		this.getRootPresenter().addMetaHttp("Content-Type", "application/xhtml+xml; charset=UTF-8");
	}

	@Override
	public void			setTitle(String title)
	{
		if (baseTitle == null) {
			baseTitle = !StringUtils.isEmpty(title) ? title+" - "+getSiteTitle() : getSiteTitle();
			title = baseTitle;
		}
		else if (!StringUtils.isEmpty(title)) {
			title = title+" - "+baseTitle;
		}
		super.setTitle(title);
	}

	public String			getSiteTitle()
	{
		return "";
	}

	@Override
	public void			prepare()
	{
		this.headDocumentUtf8();
		super.prepare();
	}

	public void			prepareJsPackage(String packageName)
	{
		ResourceResolver resourceResolver = getCallerContext().getBeanTyped("resourceResolver", ResourceResolver.class);
		try {
			for (String file: IOUtils.toString(resourceResolver.getMandatoryResourceAsStream("resources/"+packageName.replaceAll("\\.", "/")+"/_jspkg.files"), "UTF-8").split("\n")) {
				if (file.isEmpty())
					continue;
				getRootPresenter().addLinkedFile("js", PageUrl.createRooted("/resources/"+file));
			}
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void			render()
	{
		this.renderDocumentStart();
		this.renderHead();
		this.renderBody();
		this.renderDocumentEnd();
	}

	public void			renderDocumentStart()
	{
		this.output(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+
				//"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n"+
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
		);
	}

	public void			renderDocumentEnd()
	{
		this.output("</html>\n");
	}

	public void			renderHead()
	{
		this.output(
				"<head>\n"
				);
		this.renderMeta();
		this.renderFavicon();
		this.renderLinked();
		this.renderTitle();
		this.output(
				"</head>\n"
		);
	}

	public void			renderMeta()
	{
		for (Map<String, MetaTag> typedMetas: this.getRootPresenter().getMetas().values()) {
			for (MetaTag meta: typedMetas.values()) {
				this.outputFormat("<meta %s=%A content=%A />\n", meta.getType(), meta.getName(), meta.getContent());
			}
		}
	}

	public void			renderTitle()
	{
		this.outputFormat(
				"<title>%S</title>\n",
				getRootPresenter().getTitle()
		);
	}

	public void			renderFavicon()
	{
		this.outputFormat(
				"<link rel=\"SHORTCUT ICON\" href=\"%U\" />\n",
				PageUrl.createRooted("/favicon.ico")
		);
	}

	public void			renderLinked()
	{
		this.renderLinkedCss();
		this.renderLinkedJs();
	}

	public void			renderLinkedCss()
	{
		Collection<PageUrl> linked;
		if ((linked = this.getRootPresenter().getLinkedFiles("css")) != null) {
			Set<PageUrl> done = new HashSet<PageUrl>();
			for (PageUrl file: linked) {
				if (done.contains(file))
					continue;
				this.outputFormat("<link rel=\"STYLESHEET\" href=\"%U\" type=\"text/css\" />\n", file);
				done.add(file);
			}
		}
	}

	public void			renderLinkedJs()
	{
		Collection<PageUrl> linked;
		if ((linked = this.getRootPresenter().getLinkedFiles("js")) != null) {
			Set<PageUrl> done = new HashSet<PageUrl>();
			for (PageUrl file: linked) {
				if (file.getUrl().startsWith("@")) {
					if (file.getUrl().length() > 1 && done.contains(file))
						continue;
					this.outputFormat("<script type=\"text/javascript\">\n%s\n</script>\n", file.getOptions().getOptionMandatory("content"));
				}
				else {
					if (done.contains(file))
						continue;
					this.outputFormat("<script type=\"text/javascript\" src=\"%U\"></script>\n", file);
					done.add(file);
				}
			}
		}
	}

	public void			renderBodyAttributes()
	{
		if (this.getRootPresenter().getActiveField() != null) {
			this.outputFormat(" onload=%A", "javascript:document.getElementById('"+this.getRootPresenter().getActiveField()+"').focus()");
		}
	}

	public void			renderBody()
	{
		this.output("<body");
		this.renderBodyAttributes();
		this.output(">\n");
		this.renderBodyInner();
		this.output("</body>\n");
	}

	public void			renderBodyInner()
	{
		this.output(
				"<table width=\"100%\" cellspacing='0' cellpadding='0'><tr><td height='6' colspan='3'></td></tr><tr>\n"+
				"	<td width='140' valign='top'>\n"
				);
		this.renderMenu();
		this.output(
				"	</td>\n"+
				"	<td width='6'></td>\n"+
				"	<td class='page-inner' valign='top'>\n"
				);
		this.renderMessages();
		this.renderContent();
		this.output(
				"	</td></tr>\n"+
				"</table>\n"
				);
		this.renderStats();
	}

	public void			renderContent()
	{
		super.render();
	}

	public void			renderMenu()
	{
		new VerticalMenuRenderer().render(this, null, null);
	}

	public void			renderMessages()
	{
		List<PendingMessage> pendingMessages = getRootPresenter().getPendingMessages();

		if (!pendingMessages.isEmpty()) {
			this.output("<table class='page-messages'>\n");
			for (PendingMessage msg: pendingMessages) {
				this.outputFormat("<tr><td><div class=%A>%S</div></td></tr>\n", "msg_type_"+msg.getType(), msg.getMessage());
			}
			this.output("</table>\n");
		}
	}

	public void			addStat(Presenter presenter)
	{
		statPresenters.add(presenter);
	}

	public void			addW3CStat()
	{
		this.addStat(new net.dryuf.mvp.stat.W3cXhtmlPresenter(this, Options.NONE));
	}

	public void			addTimingStat()
	{
		if (getUiContext().getTiming()) {
			this.addStat(new net.dryuf.mvp.stat.TimingPresenter(this, Options.NONE));
		}
	}

	public void			renderStats()
	{
		if (!this.statPresenters.isEmpty()) {
			this.output(
					"<table align='right'><tr>\n"
					);
			for (Presenter presenter: this.statPresenters) {
				this.output("<td>");
				presenter.render();
				this.output("</td>\n");
			}
			this.output(
					"</tr></table>\n"
					);
		}
	}

	protected String		baseTitle;

	protected List<Presenter>	statPresenters = new LinkedList<Presenter>();
}
