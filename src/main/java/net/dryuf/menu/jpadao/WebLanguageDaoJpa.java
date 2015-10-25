package net.dryuf.menu.jpadao;

import net.dryuf.menu.WebLanguage;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WebLanguageDaoJpa extends net.dryuf.dao.DryufDaoContext<WebLanguage, net.dryuf.menu.WebLanguage.Pk> implements net.dryuf.menu.dao.WebLanguageDao
{

	public				WebLanguageDaoJpa()
	{
		super(WebLanguage.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<WebLanguage>	listByCompos(String compos)
	{
		return (List<WebLanguage>)entityManager.createQuery("FROM WebLanguage WHERE pk.providerName = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(String compos)
	{
		return entityManager.createQuery("DELETE FROM WebLanguage obj WHERE obj.pk.providerName = ?1").setParameter(1, compos).executeUpdate();
	}

}
