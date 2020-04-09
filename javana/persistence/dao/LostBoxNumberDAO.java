package br.com.javana.persistence.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.javana.persistence.pojo.LostBoxNumbersPOJO;
import br.com.javana.utils.MessageLoader;

public class LostBoxNumberDAO extends BaseDAO<LostBoxNumbersPOJO> {

	public LostBoxNumberDAO(Class<LostBoxNumbersPOJO> pClass) {
		super(pClass);
	}

	public void deleteByNumber(long number) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria numbers = session.createCriteria(LostBoxNumbersPOJO.class);
			numbers.addOrder(Order.desc("number"));
			numbers.setMaxResults(1);

			LostBoxNumbersPOJO pojo = (LostBoxNumbersPOJO) numbers.uniqueResult();

			if (pojo != null) {
				this.beginTransaction();

				session.delete(pojo);

				this.commitTransaction();
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(53));
		} finally {
			this.closeSession();
		}
	}
}
