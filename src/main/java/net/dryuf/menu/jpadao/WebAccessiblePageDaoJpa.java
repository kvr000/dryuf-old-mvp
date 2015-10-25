package net.dryuf.menu.jpadao;

import net.dryuf.menu.WebAccessiblePage;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WebAccessiblePageDaoJpa extends net.dryuf.dao.DryufDaoContext<WebAccessiblePage, net.dryuf.menu.WebAccessiblePage.Pk> implements net.dryuf.menu.dao.WebAccessiblePageDao
{

	public				WebAccessiblePageDaoJpa()
	{
		super(WebAccessiblePage.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<WebAccessiblePage>	listByCompos(String compos)
	{
		return (List<WebAccessiblePage>)entityManager.createQuery("FROM WebAccessiblePage WHERE pk.providerName = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(String compos)
	{
		return entityManager.createQuery("DELETE FROM WebAccessiblePage obj WHERE obj.pk.providerName = ?1").setParameter(1, compos).executeUpdate();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public WebAccessiblePage	loadByPageCode(String providerName, String pageCode)
	{
		@SuppressWarnings("unchecked")
		List<WebAccessiblePage> result = entityManager.createQuery("FROM WebAccessiblePage WHERE providerName = ?1 AND pageCode = ?2").setParameter(1, providerName).setParameter(2, pageCode).getResultList();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

}
