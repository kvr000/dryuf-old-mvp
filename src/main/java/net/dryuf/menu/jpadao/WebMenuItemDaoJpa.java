package net.dryuf.menu.jpadao;

import net.dryuf.menu.WebMenuItem;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WebMenuItemDaoJpa extends net.dryuf.dao.DryufDaoContext<WebMenuItem, net.dryuf.menu.WebAccessiblePage.Pk> implements net.dryuf.menu.dao.WebMenuItemDao
{

	public				WebMenuItemDaoJpa()
	{
		super(WebMenuItem.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<WebMenuItem>	listByCompos(net.dryuf.menu.WebAccessiblePage.Pk compos)
	{
		return (List<WebMenuItem>)entityManager.createQuery("FROM WebMenuItem WHERE pageCode = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(net.dryuf.menu.WebAccessiblePage.Pk compos)
	{
		return entityManager.createQuery("DELETE FROM WebMenuItem obj WHERE obj.pk.pageCode = ?1").setParameter(1, compos).executeUpdate();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public List<net.dryuf.menu.WebMenuItem> listRooted(String providerName)
	{
		@SuppressWarnings("unchecked")
		List<net.dryuf.menu.WebMenuItem> result = (List<net.dryuf.menu.WebMenuItem>)entityManager.createQuery("SELECT mi FROM WebMenuItem mi WHERE providerName = ?1 parentItem IS NULL ORDER BY subOrder").setParameter(1, providerName).getResultList();
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public List<net.dryuf.menu.WebMenuItem> listByParent(String providerName, String parentItem)
	{
		@SuppressWarnings("unchecked")
		List<net.dryuf.menu.WebMenuItem> result = (List<net.dryuf.menu.WebMenuItem>)entityManager.createQuery("SELECT mi FROM WebMenuItem mi WHERE providerName = ?1 AND parentItem = ?2 ORDER BY subOrder").setParameter(1, providerName).setParameter(2, parentItem).getResultList();
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public List<net.dryuf.menu.WebAccessiblePage> listPagesRooted(String providerName)
	{
		@SuppressWarnings("unchecked")
		List<net.dryuf.menu.WebAccessiblePage> result = (List<net.dryuf.menu.WebAccessiblePage>)entityManager.createQuery("SELECT page FROM WebMenuItem mi, WebAccessiblePage page WHERE page.pk = mi.pageCode AND mi.pageCode.providerName = ?1 AND mi.parentItem IS NULL ORDER BY mi.subOrder").setParameter(1, providerName).getResultList();
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public List<net.dryuf.menu.WebAccessiblePage> listPagesByParent(String providerName, String parentItem)
	{
		@SuppressWarnings("unchecked")
		List<net.dryuf.menu.WebAccessiblePage> result = (List<net.dryuf.menu.WebAccessiblePage>)entityManager.createQuery("SELECT page FROM WebMenuItem mi, WebAccessiblePage page WHERE page.pk = mi.pageCode AND mi.pageCode.providerName = ?1 AND mi.parentItem = ?2 ORDER by mi.subOrder").setParameter(1, providerName).setParameter(2, parentItem).getResultList();
		return result;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public WebMenuItem		loadByPageCode(String providerName, String pageCode)
	{
		@SuppressWarnings("unchecked")
		List<WebMenuItem> result = entityManager.createQuery("FROM WebMenuItem WHERE providerName = ?1 AND pageCode = ?2").setParameter(1, providerName).setParameter(2, pageCode).getResultList();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

}
