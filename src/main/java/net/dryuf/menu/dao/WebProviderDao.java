package net.dryuf.menu.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.menu.WebProvider;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface WebProviderDao extends net.dryuf.dao.DynamicDao<WebProvider, String>
{
	public WebProvider		refresh(WebProvider obj);
	public WebProvider		loadByPk(String pk);
	public List<WebProvider>	listAll();
	public void			insert(WebProvider obj);
	public void			insertTxNew(WebProvider obj);
	public WebProvider		update(WebProvider obj);
	public void			remove(WebProvider obj);
	public boolean			removeByPk(String pk);

	public String			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<WebProvider> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<WebProvider> holder);
	public WebProvider		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<WebProvider> retrieveDynamic(EntityHolder<?> composition, String pk);
	public WebProvider		updateDynamic(EntityHolder<WebProvider> roleObject, String pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, String pk);
	public long			listDynamic(List<EntityHolder<WebProvider>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
