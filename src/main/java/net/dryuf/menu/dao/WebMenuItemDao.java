package net.dryuf.menu.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.menu.WebMenuItem;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface WebMenuItemDao extends net.dryuf.dao.DynamicDao<WebMenuItem, net.dryuf.menu.WebAccessiblePage.Pk>
{
	public WebMenuItem		refresh(WebMenuItem obj);
	public WebMenuItem		loadByPk(net.dryuf.menu.WebAccessiblePage.Pk pk);
	public List<WebMenuItem>	listAll();
	public void			insert(WebMenuItem obj);
	public void			insertTxNew(WebMenuItem obj);
	public WebMenuItem		update(WebMenuItem obj);
	public void			remove(WebMenuItem obj);
	public boolean			removeByPk(net.dryuf.menu.WebAccessiblePage.Pk pk);
	public List<WebMenuItem>	listByCompos(net.dryuf.menu.WebAccessiblePage.Pk compos);
	public long			removeByCompos(net.dryuf.menu.WebAccessiblePage.Pk compos);

	public net.dryuf.menu.WebAccessiblePage.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<WebMenuItem> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<WebMenuItem> holder);
	public WebMenuItem		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<WebMenuItem> retrieveDynamic(EntityHolder<?> composition, net.dryuf.menu.WebAccessiblePage.Pk pk);
	public WebMenuItem		updateDynamic(EntityHolder<WebMenuItem> roleObject, net.dryuf.menu.WebAccessiblePage.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.menu.WebAccessiblePage.Pk pk);
	public long			listDynamic(List<EntityHolder<WebMenuItem>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

	public List<net.dryuf.menu.WebMenuItem> listRooted(String providerName);

	public List<net.dryuf.menu.WebMenuItem> listByParent(String providerName, String parentItem);

	public List<net.dryuf.menu.WebAccessiblePage> listPagesRooted(String providerName);

	public List<net.dryuf.menu.WebAccessiblePage> listPagesByParent(String providerName, String parentItem);

	public WebMenuItem		loadByPageCode(String providerName, String pageCode);

}
