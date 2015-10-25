package net.dryuf.menu.jpadao;

import net.dryuf.menu.WebProvider;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WebProviderDaoJpa extends net.dryuf.dao.DryufDaoContext<WebProvider, String> implements net.dryuf.menu.dao.WebProviderDao
{

	public				WebProviderDaoJpa()
	{
		super(WebProvider.class);
	}

}
