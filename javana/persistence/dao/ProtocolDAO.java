package br.com.javana.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.javana.persistence.pojo.ProtocolPOJO;
import br.com.javana.utils.MessageLoader;

public class ProtocolDAO extends BaseDAO<ProtocolPOJO> {

	public ProtocolDAO(Class<ProtocolPOJO> pClass) {
		super(pClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.javana.persistence.dao.IProtocolDAO#findByNumber(long) Só
	 *      traz os protocolos que nao estejam cancelados
	 */
	public ProtocolPOJO findByNumber(long number, boolean includeInvalid) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria protocols = session.createCriteria(ProtocolPOJO.class);
			protocols.add(Restrictions.eq("number", number));

			if (!includeInvalid) {
				protocols.add(Restrictions.eq("status", 0));
			}

			protocols.setMaxResults(1);

			ProtocolPOJO pojo = (ProtocolPOJO) protocols.uniqueResult();

			if (pojo == null) {
				return null;
			} else {
				return pojo;
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(54));
		} finally {
			this.closeSession();
		}
	}

	public long pickNumber() throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria protocols = session.createCriteria(ProtocolPOJO.class);
			protocols.addOrder(Order.desc("number"));
			protocols.setMaxResults(1);

			ProtocolPOJO lastProtocol = (ProtocolPOJO) protocols.uniqueResult();

			if (lastProtocol == null) {
				return 0;
			} else {
				return lastProtocol.getNumber();
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(55));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProtocolPOJO> getAllNumbers() throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			List<ProtocolPOJO> results = session.createCriteria(ProtocolPOJO.class).addOrder(Order.asc("number"))
					.list();

			return results;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(56));
		} finally {
			this.closeSession();
		}
	}

}
