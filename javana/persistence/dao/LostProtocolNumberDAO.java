package br.com.javana.persistence.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.javana.persistence.pojo.LostProtocolNumberPOJO;
import br.com.javana.utils.MessageLoader;

public class LostProtocolNumberDAO extends BaseDAO<LostProtocolNumberPOJO> {

	public LostProtocolNumberDAO(Class<LostProtocolNumberPOJO> pClass) {
		super(pClass);
	}

	public void deleteByNumber(long number) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria numbers = session.createCriteria(LostProtocolNumberPOJO.class);
			numbers.addOrder(Order.desc("number"));
			numbers.setMaxResults(1);

			LostProtocolNumberPOJO pojo = (LostProtocolNumberPOJO) numbers.uniqueResult();

			if (pojo != null) {
				this.beginTransaction();

				session.delete(pojo);

				this.commitTransaction();
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(63));
		} finally {
			this.closeSession();
		}
	}

	public long getFirst() {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria numbers = session.createCriteria(LostProtocolNumberPOJO.class);
			numbers.addOrder(Order.asc("number"));
			numbers.setMaxResults(1);

			LostProtocolNumberPOJO pojo = (LostProtocolNumberPOJO) numbers.uniqueResult();

			if (pojo != null) {
				return pojo.getNumber();
			}
			return 0;
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new HibernateException(e);
		} finally {
			this.closeSession();
		}
	}

}
