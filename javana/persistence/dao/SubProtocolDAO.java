package br.com.javana.persistence.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.javana.persistence.pojo.SubProtocolPOJO;
import br.com.javana.utils.MessageLoader;

public class SubProtocolDAO extends BaseDAO<SubProtocolPOJO> {

	public SubProtocolDAO(Class<SubProtocolPOJO> pClass) {
		super(pClass);
	}

	@SuppressWarnings("unchecked")
	public List<SubProtocolPOJO> findByProtocolId(long protocolId) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(SubProtocolPOJO.class);
			crit.createCriteria("protocol").add(Restrictions.eq("id", protocolId));
			List<SubProtocolPOJO> pojos = crit.list();
			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(64));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<SubProtocolPOJO> findByProtocolNumber(Long protocolNumber) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(SubProtocolPOJO.class);
			crit.createCriteria("protocol").add(Restrictions.eq("number", protocolNumber));
			List<SubProtocolPOJO> pojos = crit.list();
			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(54));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<SubProtocolPOJO> findByIds(Set<Long> subIds) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(SubProtocolPOJO.class);
			crit.add(Restrictions.in("id", subIds));
			return crit.list();
		} catch (Exception e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(54));
		} finally {
			this.closeSession();
		}
	}

}
