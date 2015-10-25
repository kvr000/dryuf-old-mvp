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

package net.dryuf.datagrid;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import net.dryuf.app.ClassMeta;
import net.dryuf.app.ClassMetaManager;
import net.dryuf.app.FieldDef;
import net.dryuf.dao.DynamicDao;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.Textual;
import net.dryuf.core.Options;
import net.dryuf.textual.TextualManager;
import net.dryuf.meta.ActionDef;
import net.dryuf.mvp.Presenter;
import net.dryuf.mvp.RootPresenter;
import net.dryuf.core.StringUtil;


public class DataPresenter<T> extends net.dryuf.mvp.ChildPresenter
{
	public				DataPresenter(Presenter parentPresenter, Options options)
	{
		super(parentPresenter, options);

		this.options = options;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Presenter		init()
	{
		super.init();

		if (dataClass == null)
			this.dataClass = (Class<T>) options.getOptionMandatory("dataClass");

		this.dataName = this.dataClass.getName().replace(".", "_");
		this.classMeta = net.dryuf.app.ClassMetaManager.openCached(this.getCallerContext().getAppContainer(), this.dataClass, this.dataView);

		this.ownerHolder = net.dryuf.core.EntityHolder.createRoleOnly(this.getCallerContext());

		return this;
	}

	public void			setDataProvider(DynamicDao<T, ?> dataProvider)
	{
		this.dataProvider = dataProvider;
	}

	public String			getDataClassName()
	{
		return this.dataClass.getName();
	}

	public String			urlDisplayKey(T entity)
	{
		return this.classMeta.urlDisplayKey(callerContext, entity);
	}

	public String			urlPkEntityKey(Object pk)
	{
		return this.classMeta.urlPkEntityKey(this.getCallerContext(), pk);
	}

	/**
	 * Formats external entity.
	 *
	 * @param refKey
	 * 	key of referenced entity
	 * @param refClass
	 * 	class of referenced entity
	 *
	 * @return
	 * 	formatted entity
	 */
	public String			formatExternal(Object refKey, Class<?> refClass)
	{
		@SuppressWarnings("unchecked")
		DynamicDao<Object, Object> refProvider = (DynamicDao<Object, Object>) getCallerContext().getBean(refClass.getName()+"-dao");
		@SuppressWarnings("unchecked")
		ClassMeta<Object> refMeta = (ClassMeta<Object>) ClassMetaManager.openCached(this.callerContext.getAppContainer(), refClass, "Default");
		EntityHolder<Object> refObject = refProvider.retrieveDynamic(EntityHolder.createRoleOnly(getCallerContext()), refKey);
		Object refEntity = refObject.getEntity();
		StringBuilder formatted = null;
		for (String refFieldName: refMeta.getRefFields()) {
			FieldDef<?> refFdef = refMeta.getField(refFieldName);
			if (formatted == null)
				formatted = new StringBuilder();
			else
				formatted.append(" ");
			formatted.append(TextualManager.formatTextualUnsafe(refFdef.needTextual(), this.getCallerContext(), refMeta.getEntityFieldValue(refEntity, refFieldName)));
		}
		return formatted.toString();
	}

	/**
	 * Formats reference.
	 *
	 * @param entity
	 * 	entity
	 * @param fdef
	 * 	field definition to be formatted
	 *
	 * @return
	 * 	formatted entity
	 */
	@SuppressWarnings("unchecked")
	public String			formatRef(T entity, FieldDef<?> fdef)
	{
		Object refPk = this.classMeta.getEntityFieldValue(entity, fdef.getName());
		if (fdef.getAssocClass() == null)
			throw new RuntimeException("Field "+fdef.getName()+" does not have association definition");
		ClassMeta<Object> refMeta = (ClassMeta<Object>) ClassMetaManager.openCached(getCallerContext().getAppContainer(), fdef.getAssocClass(), "Default");
		DynamicDao<Object, Object> refProvider = (DynamicDao<Object, Object>) this.getCallerContext().getBean(refMeta.getDataClassName()+"-dao");
		EntityHolder<Object> refObject = refProvider.retrieveDynamic(EntityHolder.createRoleOnly(getCallerContext()), refPk);
		if (refObject == null)
			return null;
		Object refEntity = refObject.getEntity();
		StringBuilder formatted = null;
		for (String refFieldName: refMeta.getRefFields()) {
			FieldDef<?> refFdef = refMeta.getField(refFieldName);
			if (formatted == null)
				formatted = new StringBuilder();
			else
				formatted.append(" ");
			formatted.append(TextualManager.formatTextualUnsafe(refFdef.needTextual(), this.getCallerContext(), refMeta.getEntityFieldValue(refEntity, refFieldName)));
		}
		return formatted.toString();
	}

	/**
	 * Formats reference.
	 *
	 * @param entity
	 * 	entity
	 * @param fieldName
	 * 	field name to be formatted
	 *
	 * @return
	 * 	formatted entity
	 */
	public String			formatRefName(T entity, String fieldName)
	{
		return this.formatRef(entity, this.classMeta.getField(fieldName));
	}

	/**
	 * Formats value of specific field.
	 *
	 * @param entity
	 * 	entity
	 * @param fdef
	 * 	field definition
	 *
	 * @return
	 * 	formatted value
	 */
	public String			formatValue(T entity, FieldDef<?> fdef)
	{
		Object internal = this.classMeta.getEntityFieldValue(entity, fdef.getName());
		return internal == null ? "" : TextualManager.formatTextualUnsafe(fdef.needTextual(), this.getCallerContext(), internal);
	}

	/**
	 * Formats value of specific field.
	 *
	 * @param entity
	 * 	entity
	 * @param fieldName
	 * 	field name
	 *
	 * @return
	 * 	formatted value
	 */
	public String			formatValueName(T entity, String fieldName)
	{
		return this.formatValue(entity, this.classMeta.getField(fieldName));
	}

	/**
	 * Formats single child.
	 *
	 * @param child
	 * 	child entity
	 * @param childClass
	 * 	child class
	 *
	 * @return
	 * 	formatted entity
	 */
	@SuppressWarnings("unchecked")
	public String			formatChild(Object child, Class<?> childClass)
	{
		ClassMeta<Object> childMeta = (ClassMeta<Object>) ClassMetaManager.openCached(this.getCallerContext().getAppContainer(), childClass, "Default");
		return formatChildInternal(child, childMeta, null).toString();
	}

	/**
	 * Formats single child, internal implementation.
	 *
	 * @param child
	 * 	child entity
	 * @param childMeta
	 * 	child class meta information
	 * @param formatted
	 * 	formatted string
	 *
	 * @return
	 * 	formatted entity
	 */
	@SuppressWarnings("unchecked")
	public StringBuilder		formatChildInternal(Object child, ClassMeta<?> childMeta, StringBuilder formatted)
	{
		for (FieldDef<?> fdef: childMeta.getFields()) {
			if (fdef.getAssocType() == FieldDef.AST_Compos)
				continue;
			Object value = ((ClassMeta<Object>)childMeta).getEntityFieldValue(child, fdef.getName());
			if (fdef.getEmbedded() != null) {
				formatted = formatChildInternal(value, fdef.getEmbedded(), formatted);
			}
			else {
				if (formatted == null)
					formatted = new StringBuilder();
				else
					formatted.append(" ");
				if (fdef.getAssocType() == FieldDef.AST_Reference) {
					formatted.append(this.formatExternal(value, fdef.getAssocClass()));
				}
				else {
					formatted.append(TextualManager.formatTextualUnsafe(fdef.needTextual(), this.getCallerContext(), value));
				}
			}
		}
		return formatted;
	}

	/**
	 * Formats children contained in field.
	 *
	 * @param entity
	 * 	entity
	 * @param fdef
	 * 	field definition
	 *
	 * @return
	 * 	list of formatted entities
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<String>	formatChildrenList(T entity, final FieldDef<?> fdef)
	{
		Collection<Object> internal = (Collection<Object>)this.classMeta.getEntityFieldValue(entity, fdef.getName());
		if (internal.size() > 0) {
			if (internal.iterator().next() instanceof Comparable) {
				List<Comparable> sorted = new LinkedList<Comparable>();
				sorted.addAll((Collection)internal);
				Collections.sort(sorted);
				internal = (Collection)sorted;
			}
			return Collections2.transform(internal, new Function<Object, String>() {
				public String apply(Object child)
				{
					return formatChild(child, fdef.getAssocClass());
				}
			});
		}
		else {
			return (Collection)internal;
		}
	}

	/**
	 * Formats children list contained in field.
	 *
	 * @param entity
	 * 	entity
	 * @param fieldName
	 * 	field name
	 *
	 * @return
	 * 	list of formatted entities
	 */
	public Collection<String>	formatChildrenListName(T entity, String fieldName)
	{
		return this.formatChildrenList(entity, this.classMeta.getField(fieldName));
	}

	/**
	 * Formats children list contained in field, concatenating using separator.
	 *
	 * @param entity
	 * 	entity
	 * @param fdef
	 * 	field definition
	 * @param separator
	 * 	separator used to join entities
	 *
	 * @return
	 * 	single string of concatenated entities
	 */
	public String			formatChildren(T entity, FieldDef<?> fdef, String separator)
	{
		return StringUtil.join(separator, this.formatChildrenList(entity, fdef));
	}

	/**
	 * Formats children list contained in field, concatenating using separator.
	 *
	 * @param entity
	 * 	entity
	 * @param fieldName
	 * 	field name
	 * @param separator
	 * 	separator used to join entities
	 *
	 * @return
	 * 	single string of concatenated entities
	 */
	public String			formatChildrenName(T entity, String fieldName, String separator)
	{
		return this.formatChildren(entity, this.classMeta.getField(fieldName), separator);
	}

	/**
	 * Formats field.
	 *
	 * @param entity
	 * 	entity
	 * @param fdef
	 * 	field definition
	 *
	 * @return
	 * 	formatted entity
	 */
	public String			formatField(T entity, FieldDef<?> fdef)
	{
		if (fdef.getAssocType() == FieldDef.AST_Children) {
			return this.formatChildren(entity, fdef, ", ");
		}
		else if (fdef.getAssocClass() != null) {
			return this.formatRef(entity, fdef);
		}
		else {
			return this.formatValue(entity, fdef);
		}
	}

	/**
	 * Formats field.
	 *
	 * @param entity
	 * 	entity
	 * @param fieldName
	 * 	field name
	 *
	 * @return
	 * 	formatted entity
	 */
	public String			formatFieldName(T entity, String fieldName)
	{
		return this.formatField(entity, this.classMeta.getField(fieldName));
	}

	public List<ActionDef>		getGlobalActionList()
	{
		return this.classMeta.getGlobalActionList(this.getCallerContext());
	}

	public String[]			readDisplayKey(String first)
	{
		List<String> keys = new LinkedList<String>();
		for (@SuppressWarnings("unused") String key: this.classMeta.getDisplayKeys()) {
			if (first == null) {
				if ((first = this.getRootPresenter().getPathElement()) == null) {
					this.createNotFoundPresenter();
					return null;
				}
				else if (rootPresenter.needPathSlash(true) == null) {
					new net.dryuf.mvp.FalseDummyPresenter(this);
					return null;
				}
			}
			keys.add(first);
			first = null;
		}
		return keys.toArray(StringUtil.STRING_EMPTY_ARRAY);
	}

	public EntityHolder<T>		loadByDisplayKey(String[] keys)
	{
		int keyIdx = 0;
		Map<String, Object> filter = new LinkedHashMap<String, Object>();
		for (String fieldName: this.classMeta.getDisplayKeys()) {
			FieldDef<?> fdef = this.classMeta.getField(fieldName);
			filter.put(fdef.getName(), TextualManager.convertTextualUnsafe(fdef.needTextual(), this.getCallerContext(), keys[keyIdx++]));
		}
		List<EntityHolder<T>> objects = new LinkedList<EntityHolder<T>>();
		this.dataProvider.listDynamic(objects, this.ownerHolder, filter, null, null, null);
		return objects.size() != 0 ? objects.get(0) : null;
	}

	public long			loadList()
	{
		dataProvider.keepContextTransaction(getCallerContext());
		listData = new LinkedList<EntityHolder<T>>();
		return this.dataProvider.listDynamic(listData, this.ownerHolder, this.listFilter, this.listOrder, listPageNum*listPageSize, listPageSize);
	}

	public void			loadingSingle(String[] displayKey)
	{
		dataProvider.keepContextTransaction(getCallerContext());
	}

	public void			loadingAction(String[] displayKey, String action)
	{
	}

	public DataPresenterRenderer<T, List<EntityHolder<T>>, ?> forceModelList(List<EntityHolder<T>> list)
	{
		this.listData = list;
		return this.setRenderingList();
	}

	@SuppressWarnings("unchecked")
	public boolean			removeObject(net.dryuf.core.CallerContext role, Object key)
	{
		return ((DynamicDao<Object, Object>)this.dataProvider).deleteDynamic(EntityHolder.createRoleOnly(role), key);
	}

	public Presenter		createModeOper(String mode)
	{
		return new DataPresenter_ModeOper<T>(this, net.dryuf.core.Options.NONE);
	}

	public Presenter		createModeList(String mode)
	{
		return new DataPresenter_ModeList<T>(this, net.dryuf.core.Options.NONE);
	}

	public Presenter		createModeCreate(String mode)
	{
		return new DataPresenter_ModeCreate<T>(this, net.dryuf.core.Options.NONE);
	}

	public Presenter		createModeInfo(String mode)
	{
		return new DataPresenter_ModeInfo<T>(this, net.dryuf.core.Options.NONE);
	}

	public Presenter		createModeEdit(String mode)
	{
		return new DataPresenter_ModeEdit<T>(this, net.dryuf.core.Options.NONE);
	}

	public Presenter		createModeRemove(String mode)
	{
		return new DataPresenter_ModeRemove<T>(this, net.dryuf.core.Options.NONE);
	}

	public boolean			createModeInfoAndProcessLast()
	{
		RootPresenter rootPresenter = getRootPresenter();
		rootPresenter.putBackLastElement();
		return createModeInfo("info").process();
	}

	public boolean			process()
	{
		relativeUrl = getRootPresenter().getReversePath();
		return super.process();
	}

	public boolean			processMore(String element)
	{
		String action;
		String[] displayKey;
		EntityHolder<T> obj;
		if (element.startsWith("-")) {
			if (element.equals("-page-")) {
				this.getRootPresenter().putBackLastElement();
				return this.processMode("list");
			}
			else if (element.equals("-new-")) {
				return this.processMode("new");
			}
			else if (element.equals("-oper-")) {
				return this.processMode("oper");
			}
		}
		if ((displayKey = this.readDisplayKey(element)) == null && this.getLeadChild() != null) {
			return this.getLeadChild().process();
		}
		else if (this.getRootPresenter().needPathSlash(true) == null) {
			return false;
		}
		if ((action = rootPresenter.getPathElement()) != null) {
			loadingAction(displayKey, action);
		}
		else {
			loadingSingle(displayKey);
		}
		if ((obj = this.loadByDisplayKey(displayKey)) != null) {
			this.currentObject = obj;
			if (action != null) {
				return this.processObjectAction(action);
			}
			else {
				return this.processObjectMode(this.getRequest().getParamDefault("mode", "info"));
			}
		}
		else {
			return this.processRootAction(element);
		}
	}

	public boolean			processObjectMode(String mode)
	{
		switch (mode) {
		case "info":
		case "edit":
		case "remove":
			return this.processMode(mode);

		default:
			return this.processObjectOther(mode);
		}
	}

	public boolean			processObjectAction(String action)
	{
		return createModeInfoAndProcessLast();
	}

	public boolean			processObjectOther(String mode)
	{
		return this.createDefaultPresenter().process();
	}

	public boolean			processRootAction(String element)
	{
		return this.createNotFoundPresenter().process();
	}

	@Override
	public boolean			processCommon()
	{
		return this.processMode("list");
	}

	public boolean			processMode(String mode)
	{
		this.mode = mode;
		switch (this.mode) {
		case "oper":
			this.modePresenter = this.createModeOper(this.mode);
			break;

		case "list":
			this.modePresenter = this.createModeList(this.mode);
			break;

		case "info":
			this.modePresenter = this.createModeInfo(this.mode);
			break;

		case "edit":
			this.modePresenter = this.createModeEdit(this.mode);
			break;

		case "new":
			this.modePresenter = this.createModeCreate(this.mode);
			break;

		case "remove":
			this.modePresenter = this.createModeRemove(this.mode);
			break;

		default:
			throw new RuntimeException("invalid mode specified for DataPresenter: "+this.mode);
		}
		return this.modePresenter.process();
	}

	public String			formatKey(T entity)
	{
		StringBuilder sb = new StringBuilder();
		for (String pk: this.classMeta.getAdditionalPkFields()) {
			FieldDef<?> fdef = this.classMeta.getField(pk);
			@SuppressWarnings("unchecked")
			Textual<Object> textual = (Textual<Object>) TextualManager.createTextualUnsafe(fdef.needTextual(), this.getCallerContext());
			sb.append("_").append(textual.formatKey(this.classMeta.getEntityFieldValue(entity, pk)));
		}
		return sb.replace(0, 1, "").toString();
	}

	public Map<String, Object>	convertKey(String text)
	{
		text += "_";
		Map<String, Object> key = new LinkedHashMap<String, Object>();
		for (String pk: this.classMeta.getAdditionalPkFields()) {
			FieldDef<?> fdef = this.classMeta.getField(pk);
			@SuppressWarnings("unchecked")
			Textual<Object> textual = (Textual<Object>) TextualManager.createTextualUnsafe(fdef.needTextual(), this.getCallerContext());
			String[] match;
			if ((match = StringUtil.matchText("^_([^_]*)(.*)$", text)) == null)
				throw new RuntimeException("failed to convert key "+text+" from object "+this.dataClass.getName());
			key.put(fdef.getName(), textual.convertKey(match[1]));
			text = match[2];
		}
		return key;
	}

	@SuppressWarnings("unchecked")
	public DataPresenterRenderer<T, List<EntityHolder<T>>, ?> setRenderingList()
	{
		return (DataPresenterRenderer<T, List<EntityHolder<T>>, ?>) this.setRendering(new net.dryuf.datagrid.ListRenderer<T, Object>());
	}

	@SuppressWarnings("unchecked")
	public DataPresenterRenderer<T, EntityHolder<T>, ?>	setRenderingInfo()
	{
		return (DataPresenterRenderer<T, EntityHolder<T>, ?>) this.setRendering(new net.dryuf.datagrid.InfoRenderer<T, Object>());
	}

	public DataPresenterRenderer<T, EntityHolder<T>, ?> setRenderingEdit()
	{
		throw new UnsupportedOperationException();
		//return this.setRendering(new net.dryuf.datagrid.EditRenderer(this.getCallerContext()), rendererModel);
	}

	public DataPresenterRenderer<T, EntityHolder<T>, ?> setRenderingCreate()
	{
		throw new UnsupportedOperationException();
		//return this.setRendering(new net.dryuf.datagrid.CreateRenderer(this.getCallerContext()), rendererModel);
	}

	public DataPresenterRenderer<T, EntityHolder<T>, ?> setRenderingRemove()
	{
		throw new UnsupportedOperationException();
		//return this.setRendering(new net.dryuf.datagrid.RemoveRenderer(this.getCallerContext()), rendererModel);
	}

	@SuppressWarnings("unchecked")
	public DataPresenterRenderer<T, ?, ?>	setRendering(DataPresenterRenderer<T, ?, ?> renderer)
	{
		this.renderer = (DataPresenterRenderer<T, Object, Object>) renderer;
		return this.renderer;
	}

	public void			prepare()
	{
		if (this.renderer != null) {
			this.rendererModel = this.renderer.prepare(this, mode.equals("list") ? this.listData : this.currentObject);
		}
		else {
			super.prepare();
		}
	}

	public void			render()
	{
		if (this.renderer != null) {
			this.renderer.render(this, mode.equals("list") ? this.listData : this.currentObject, this.rendererModel);
		}
		else {
			super.render();
		}
	}

	private Options			options;

	protected EntityHolder<?>	ownerHolder;

	public EntityHolder<?>		getOwnerHolder()
	{
		return this.ownerHolder;
	}

	protected DataPresenterRenderer<T, Object, Object> renderer;

	public DataPresenterRenderer<T, Object, Object> getRenderer()
	{
		return this.renderer;
	}

	protected Object		rendererModel;

	public Object			getRendererModel()
	{
		return this.rendererModel;
	}

	protected String		relativeUrl = "";

	public String			getRelativeUrl()
	{
		return this.relativeUrl;
	}

	public void			setRelativeUrl(String relativeUrl_)
	{
		this.relativeUrl = relativeUrl_;
	}

	protected ClassMeta<T>		classMeta;

	public ClassMeta<T>		getClassMeta()
	{
		return this.classMeta;
	}

	protected DynamicDao<T, ?>	dataProvider;

	public DynamicDao<T, ?>		getDataProvider()
	{
		return this.dataProvider;
	}

	protected Presenter		modePresenter;

	public Presenter		getModePresenter()
	{
		return this.modePresenter;
	}

	protected String		mode;

	public String			getMode()
	{
		return this.mode;
	}

	protected Class<T>		dataClass;

	public Class<T>			getDataClass()
	{
		return this.dataClass;
	}

	public DataPresenter<T>		setDataClass(Class<T> dataClass_)
	{
		this.dataClass = dataClass_;
		return this;
	}

	protected String		dataView = "Default";

	public String			getDataView()
	{
		return this.dataView;
	}

	public DataPresenter<T>		setDataView(String dataView_)
	{
		this.dataView = dataView_;
		return this;
	}

	protected String		dataName;

	public String			getDataName()
	{
		return this.dataName;
	}

	protected String		reqName;

	public String			getReqName()
	{
		return this.reqName;
	}

	protected Map<String, Object>	listFilter;

	public Map<String, Object>	getListFilter()
	{
		return this.listFilter;
	}

	public void			setListFilter(Map<String, Object> listFilter_)
	{
		this.listFilter = listFilter_;
	}

	protected List<String>		listOrder;

	public List<String>		getListOrder()
	{
		return this.listOrder;
	}

	protected List<EntityHolder<T>>	listData;

	public List<EntityHolder<T>>	getListData()
	{
		return this.listData;
	}

	protected long			listTotal;

	public long			getListTotal()
	{
		return this.listTotal;
	}

	public void			setListTotal(long listTotal_)
	{
		this.listTotal = listTotal_;
	}

	protected long			listPageSize = 20L;

	public long			getListPageSize()
	{
		return this.listPageSize;
	}

	public void			setListPageSize(long listPageSize_)
	{
		this.listPageSize = listPageSize_;
	}

	protected long			listPageNum = 0L;

	public long			getListPageNum()
	{
		return this.listPageNum;
	}

	public void			setListPageNum(long listPageNum_)
	{
		this.listPageNum = listPageNum_;
	}

	protected EntityHolder<T>	currentObject;

	public EntityHolder<T>		getCurrentObject()
	{
		return this.currentObject;
	}
};


