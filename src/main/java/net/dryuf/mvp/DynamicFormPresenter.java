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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dryuf.textual.TextualManager;
import org.apache.commons.lang3.StringUtils;

import net.dryuf.app.FieldDef;
import net.dryuf.core.InvalidValueException;
import net.dryuf.core.Textual;
import net.dryuf.core.Options;
import net.dryuf.io.FileData;
import net.dryuf.meta.ActionDef;
import net.dryuf.meta.FieldRoles;
import net.dryuf.srvui.PageUrl;
import net.dryuf.core.StringUtil;
import net.dryuf.xml.util.XmlFormat;


public abstract class DynamicFormPresenter extends net.dryuf.mvp.FormPresenter
{
	public				DynamicFormPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);
		formattedData = new HashMap<String, String>();
	}

	public static String		formatFormPrefix(String formClazz)
	{
		return formClazz.replace(".", "_")+"__";
	}

	public String			getDisplay(ArrayList<String> displayArgs, FieldDef<?> fieldDef)
	{
		String display = fieldDef.getDisplay();
		String match[] = StringUtil.matchText("^(\\w+)\\((.*)\\)$", display);
		if (match == null) {
			throw new InvalidValueException(display, "Invalid value for "+formClassName+"."+fieldDef.getName());
		}
		String displayType = match[1];
		String[] argsList = match[2].split(",\\s*");
		for (String arg: argsList)
			displayArgs.add(arg);
		if (displayType.equals("hidden")) {
		}
		else if (displayType.equals("text")) {
			if (argsList.length != 1)
				throw new InvalidValueException(display, "Invalid value for "+formClassName+"."+fieldDef.getName()+", text requires one argument");
		}
		else if (displayType.equals("password")) {
			if (argsList.length != 1)
				throw new InvalidValueException(display, "Invalid value for "+formClassName+"."+fieldDef.getName()+", password requires one argument");
		}
		else if (displayType.equals("textarea")) {
			if (argsList.length != 2)
				throw new InvalidValueException(display, "Invalid value for "+formClassName+"."+fieldDef.getName()+", textarea requires one argument");
		}
		return displayType;
	}

	public void			formOutputType(FieldDef<?> fieldDef, String displayType, String[] displayArgs, String formatted)
	{
		String name = fieldDef.getName();
		String full = this.formWebPrefix+name;
		if (fieldDef.getAssocClass() != null) {
			this.formOutputRef(fieldDef, displayType, displayArgs, formatted);
		}
		else if (displayType.equals("hidden")) {
			this.outputFormat("<input id=%A name=%A type=\"hidden\" value=%A />\n", full, full, formatted);
		}
		else if (displayType.equals("text")) {
			this.outputFormat("<input id=%A name=%A type=\"text\" width=%A value=%A />\n", full, full, displayArgs[0], formatted == null ? "" : formatted);
		}
		else if (displayType.equals("password")) {
			this.outputFormat("<input id=%A name=%A type=\"password\" width=%A />\n", full, full, displayArgs[0]);
		}
		else if (displayType.equals("textarea")) {
			this.outputFormat("<textarea id=%A name=%A cols=%A rows=%A>%S</textarea>\n", full, full, displayArgs[0], displayArgs[1], formatted == null ? "" : formatted);
		}
		else if (displayType.equals("checkbox")) {
			this.outputFormat("<input id=%A name=%A type=\"checkbox\"%s />\n", full, full, formatted.equals("true") ? " checked='checked'" : "");
		}
		else if (displayType.equals("captcha")) {
			this.outputFormat("<input id=%A name=%A type=\"text\" width=%A value=%A />\n", full, full, displayArgs[0], formatted == null ? "" : formatted);
			this.outputFormat("<br/><img src=\"%U\" onclick=\"javascript:this.src = this.src.replace('#.*', '')+'#'+(new Date()).getTime();\" />\n", PageUrl.createRooted("/captcha/"));
		}
		else if (displayType.equals("select")) {
			TextualManager.createTextualUnsafe((Class<? extends Textual<?>>) fieldDef.needTextual(), getCallerContext()).convert(formatted, null);
			this.outputFormat("<select id=%A name=%A width=%A>\n", full, full, displayArgs[0]);
			for (Entry<String, String> validEntry: getSelectList(fieldDef, displayArgs).entrySet()) {
				this.outputFormat("\t<option value=%A%s>%S</option>\n", validEntry.getKey(), formatted.equals(validEntry.getKey()) ? " selected='selected'" : "", validEntry.getValue());
			}
			this.output("</select>\n");
		}
		else if (displayType.equals("file")) {
			this.outputFormat("<input id=%A name=%A type=\"file\" />", full, full);
		}
		else {
			throw new InvalidValueException(displayType, this.formClassName+"."+name+": unknown display type");
		}
	}

	public Map<String, String>	getSelectList(FieldDef<?> fieldDef, String[] displayArgs)
	{
		int i = 0;
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "");
		for (String s: displayArgs[1].split("\\^"))
			map.put(String.valueOf(i++), s);
		return map;
	}

	public void			formOutputRef(FieldDef<?> fieldDef, String displayType, String[] displayArgs, Object internal)
	{
		throw new InvalidValueException(displayType, this.formClassName+"."+fieldDef.getName()+": ref display is not supported");
	}

	public void			plainOutputType(FieldDef<?> fieldDef, String displayType, String[] displayArgs, String formatted)
	{
		String name = fieldDef.getName();
		if (fieldDef.getAssocClass() != null) {
			this.plainOutputRef(fieldDef, displayType, displayArgs, formatted);
		}
		else if (displayType.equals("hidden")) {
		}
		else if (displayType.equals("text")) {
			this.output(XmlFormat.escapeXml(formatted));
		}
		else if (displayType.equals("password")) {
		}
		else if (displayType.equals("textarea")) {
			this.output(XmlFormat.escapeXml(formatted));
		}
		else if (displayType.equals("checkbox")) {
			this.output(XmlFormat.escapeXml(formatted));
		}
		else if (displayType.equals("select")) {
			int internal = (Integer)TextualManager.createTextualUnsafe((Class<? extends Textual<?>>) fieldDef.needTextual(), getCallerContext()).convert(formatted, null);
			this.output(XmlFormat.escapeXml(displayArgs[1].split("\\^\\s*")[internal]));
		}
		else if (displayType.equals("file")) {
		}
		else {
			throw new InvalidValueException(displayType, this.formClassName+"."+name+": unknown display type");
		}
	}

	public void			plainOutputRef(FieldDef<?> fieldDef, String displayType, String[] displayArgs, Object internal)
	{
		throw new InvalidValueException(displayType, this.formClassName+"."+fieldDef.getName()+": ref display is not supported");
	}

	public void			autoOutputType(FieldDef<?> fieldDef, String displayType, String[] displayArgs, String formatted)
	{
		FieldRoles fieldRoles = fieldDef.getRoles();
		if (this.getCallerContext().checkRole(fieldRoles.roleNew())) {
			this.formOutputType(fieldDef, displayType, displayArgs, formatted);
		}
		else if (this.getCallerContext().checkRole(fieldRoles.roleGet())) {
			this.plainOutputType(fieldDef, displayType, displayArgs, formatted);
		}
		else {
			this.plainOutputType(fieldDef, displayType, displayArgs, "?");
		}
	}

	public void			autoOutputRef(FieldDef<?> fieldDef, String displayType, String[] displayArgs, Object internal)
	{
		FieldRoles fieldRoles = fieldDef.getRoles();
		if (this.getCallerContext().checkRole(fieldDef.getRoles().roleNew())) {
			this.formOutputRef(fieldDef, displayType, displayArgs, internal);
		}
		else if (this.getCallerContext().checkRole(fieldRoles.roleGet())) {
			this.plainOutputRef(fieldDef, displayType, displayArgs, internal);
		}
		else {
			this.plainOutputRef(fieldDef, displayType, displayArgs, "?");
		}
	}

	public String			initData()
	{
		return null;
	}

	public void			initForm()
	{
		if ((this.initError = this.initData()) != null) {
			this.addMessage(Presenter.MSG_Error, this.initError);
			return;
		}
		for (FieldDef<?> fieldDef: getDisplayableFields()) {
			@SuppressWarnings("unchecked")
			Textual<Object> textual = (Textual<Object>) TextualManager.createTextualUnsafe((Class<? extends Textual<?>>)fieldDef.needTextual(), getCallerContext());
			Object internal = getBackingValue(fieldDef);
			this.formattedData.put(fieldDef.getName(), internal == null ? "" : textual.format(internal, null));
		}
	}

	public boolean			needMandatory(ActionDef action, FieldDef<?> fieldDef)
	{
		return fieldDef.getMandatory();
	}

	public boolean			retrieve(Map<String, String> errors, ActionDef action)
	{
		for (FieldDef<?> fieldDef: getDisplayableFields()) {
			String name = fieldDef.getName();
			@SuppressWarnings("unchecked")
			Textual<Object> textual = (Textual<Object>) TextualManager.createTextualUnsafe((Class<? extends Textual<?>>) fieldDef.needTextual(), getCallerContext());
			FieldRoles froles = fieldDef.getRoles();
			if (!this.getCallerContext().checkRole(froles.roleNew())) {
				if (this.getCallerContext().checkRole(froles.roleGet())) {
					this.formattedData.put(name, textual.format(getBackingValue(fieldDef), null));
				}
			}
			else {
				String error;
				int handling = -1; // -1 default, 0 empty, 1 set
				ArrayList<String> displayArgs = new ArrayList<String>();
				String displayType = this.getDisplay(displayArgs, fieldDef);
				String param;
				if (displayType.equals("checkbox")) {
					param = this.getRequest().getParamDefault(this.formWebPrefix+name, "off").equals("on") ? "true" : "false";
				}
				else if (displayType.equals("captcha")) {
					param = this.getRequest().getParamDefault(this.formWebPrefix+name, "");
					handling = 1;
					if (!param.equals(getRequestCaptcha(getRequest()))) {
						errors.put(name, this.localize("net.dryuf.textual.Captcha", "Incorrect captcha"));
					}
					this.formattedData.put(name, param);
				}
				else if (displayType.equals("file")) {
					FileData file = this.getRequest().getFile(this.getFormFieldName(name));
					param = null;
					if (file != null && StringUtils.isEmpty((param = file.getName()))) {
						//errors.put(name, this.localize(DynamicForm.class, "File upload failed, maybe because of its size"));
						//continue;
						param = null;
					}
					if ((handling = param != null ? 1 : 0) > 0) {
						putIntoMapString(this.formattedData, name, textual.prepare(param, file.getName()));
					}
				}
				else {
					param = this.getRequest().getParamDefault(this.formWebPrefix+name, "");
				}
				if (handling == 0 || (handling < 0 && putIntoMapString(this.formattedData, name, textual.prepare(param, "")).equals(""))) {
					if (fieldDef.getMandatory() && this.needMandatory(action, fieldDef)) {
						errors.put(name,  this.localize("net.dryuf.mvp.DynamicForm", "Field is mandatory"));
					}
					else {
						setBackingValue(fieldDef, null);
					}
				}
				else if ((error = textual.check(this.formattedData.get(name), "")) != null) {
					errors.put(name, error);
				}
				else {
					setBackingValue(fieldDef, textual.convert(this.formattedData.get(name), ""));
				}
			}
		}
		return errors.isEmpty();
	}

	public static String		putIntoMapString(Map<String, String> map, String key, String value)
	{
		map.put(key, value);
		return value;
	}

	public void			renderFieldsEdit()
	{
		for (FieldDef<?> fieldDef: getDisplayableFields()) {
			try {
				this.renderFieldEdit(fieldDef);
			}
			catch (RuntimeException ex) {
				throw new RuntimeException("Failed to render field "+fieldDef.getName()+": "+ex.toString(), ex);
			}
		}
	}

	public void			renderFieldEdit(FieldDef<?> fieldDef)
	{
		ArrayList<String> displayArgs = new ArrayList<String>();
		String displayType = this.getDisplay(displayArgs, fieldDef);
		if (!displayType.equals("hidden")) {
			this.outputFormat("<tr class=\"field\"><td class=\"key\"><label for=%A>%W:</label></td><td class=\"mandind\"></td><td class=\"value\">", this.formWebPrefix+fieldDef.getName(), this.formClassName, fieldDef.getName());
		}
		/*if (fieldDef.getRef() != null) {
			this.autoOutputRef(fieldDef, displayType, displayArgs.toArray(stringArray), Dryuf.getFieldValue(fieldDef, backingObject));
		}
		else*/ {
			this.autoOutputType(fieldDef, displayType, displayArgs.toArray(stringArray), this.formattedData.get(fieldDef.getName()));
		}
		if (!displayType.equals("hidden")) {
			this.output("</td></tr>\n");
		}
	}

	public void			renderFieldPlain(FieldDef<?> fieldDef)
	{
		ArrayList<String> displayArgs = new ArrayList<String>();
		String displayType = this.getDisplay(displayArgs, fieldDef);
		if (!displayType.equals("hidden")) {
			this.outputFormat("<tr class=\"plain\"><td class=\"key\">%W:</td><td class=\"mandind\"></td><td class=\"value\">", this.formClassName, fieldDef.getName());
		}
		/*if (fdef.getRef() != null) {
			this.plainOutputRef(fieldDef, displayType, displayArgs.toArray(stringArray), Dryuf.getFieldValue(fieldDef, backingObject));
		}
		else*/ {
			this.plainOutputType(fieldDef, displayType, displayArgs.toArray(stringArray), this.formattedData.get(fieldDef.getName()));
		}
		if (!displayType.equals("hidden")) {
			this.output("</td></tr>\n");
		}
	}

	public void			renderFieldsPlain()
	{
		for (FieldDef<?> fieldDef: getDisplayableFields()) {
			this.renderFieldPlain(fieldDef);
		}
	}

	public void			renderFields()
	{
		this.renderFieldsEdit();
	}

	public void			renderActionSubmit(ActionDef action)
	{
		this.outputFormat("<tr class=\"actions\"><td class=\"key\"></td><td class=\"mandind\" /><td class=\"value\"><input type=\"submit\" name=%A value=%A/></td></tr>\n", this.formWebPrefix+action.name(), action.name(), this.formClassName);
	}

	public void			renderSubmit()
	{
		for (ActionDef action: this.getActionDefs()) {
			this.renderActionSubmit(action);
		}
	}

	public void			render()
	{
		if (initError != null) {
			// ignore
		}
		else {
			this.outputFormat("<form name=%A method=\"POST\" enctype=\"multipart/form-data\">\n", this.formWebPrefix);
			this.output("<table class=\"net-dryuf-web-Form\">\n");
			this.renderFields();
			this.renderSubmit();
			this.output("</table>\n");
			this.output("</form>\n");
		}
	}

	public String			getFormFieldName(String fieldName)
	{
		return this.formWebPrefix+fieldName;
	}

	protected List<FieldDef<?>>	getDisplayableFields()
	{
		if (displayableFields == null)
			displayableFields = buildDisplayableFields();
		return displayableFields;
	}

	protected abstract List<FieldDef<?>> buildDisplayableFields();

	protected abstract void		setBackingValue(FieldDef<?> fieldDef, Object value);

	protected abstract Object	getBackingValue(FieldDef<?> fieldDef);

	protected String		selectFieldName;

	public String			getSelectFieldName()
	{
		return this.selectFieldName;
	}

	public void			setSelectFieldName(String selectFieldName_)
	{
		this.selectFieldName = selectFieldName_;
	}

	protected String		initError = null;

	protected static String[]	stringArray = new String[0];

	protected Map<String, String>	formattedData;

	protected List<FieldDef<?>>	displayableFields;
};
