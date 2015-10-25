package net.dryuf.menu.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.menu.WebLanguage;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface WebLanguageDao extends net.dryuf.dao.DynamicDao<WebLanguage, net.dryuf.menu.WebLanguage.Pk>
{
	public WebLanguage		refresh(WebLanguage obj);
	public WebLanguage		loadByPk(net.dryuf.menu.WebLanguage.Pk pk);
	public List<WebLanguage>	listAll();
	public void			insert(WebLanguage obj);
	public void			insertTxNew(WebLanguage obj);
	public WebLanguage		update(WebLanguage obj);
	public void			remove(WebLanguage obj);
	public boolean			removeByPk(net.dryuf.menu.WebLanguage.Pk pk);
	public List<WebLanguage>	listByCompos(String compos);
	public long			removeByCompos(String compos);

	public net.dryuf.menu.WebLanguage.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<WebLanguage> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<WebLanguage> holder);
	public WebLanguage		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<WebLanguage> retrieveDynamic(EntityHolder<?> composition, net.dryuf.menu.WebLanguage.Pk pk);
	public WebLanguage		updateDynamic(EntityHolder<WebLanguage> roleObject, net.dryuf.menu.WebLanguage.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.menu.WebLanguage.Pk pk);
	public long			listDynamic(List<EntityHolder<WebLanguage>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
