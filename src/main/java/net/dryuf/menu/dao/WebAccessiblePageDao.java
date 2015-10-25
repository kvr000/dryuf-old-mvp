package net.dryuf.menu.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.menu.WebAccessiblePage;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface WebAccessiblePageDao extends net.dryuf.dao.DynamicDao<WebAccessiblePage, net.dryuf.menu.WebAccessiblePage.Pk>
{
	public WebAccessiblePage	refresh(WebAccessiblePage obj);
	public WebAccessiblePage	loadByPk(net.dryuf.menu.WebAccessiblePage.Pk pk);
	public List<WebAccessiblePage>	listAll();
	public void			insert(WebAccessiblePage obj);
	public void			insertTxNew(WebAccessiblePage obj);
	public WebAccessiblePage	update(WebAccessiblePage obj);
	public void			remove(WebAccessiblePage obj);
	public boolean			removeByPk(net.dryuf.menu.WebAccessiblePage.Pk pk);
	public List<WebAccessiblePage>	listByCompos(String compos);
	public long			removeByCompos(String compos);

	public net.dryuf.menu.WebAccessiblePage.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<WebAccessiblePage> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<WebAccessiblePage> holder);
	public WebAccessiblePage	createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<WebAccessiblePage> retrieveDynamic(EntityHolder<?> composition, net.dryuf.menu.WebAccessiblePage.Pk pk);
	public WebAccessiblePage	updateDynamic(EntityHolder<WebAccessiblePage> roleObject, net.dryuf.menu.WebAccessiblePage.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.menu.WebAccessiblePage.Pk pk);
	public long			listDynamic(List<EntityHolder<WebAccessiblePage>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

	public WebAccessiblePage	loadByPageCode(String providerName, String pageCode);

}
