package br.com.javana.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.javana.persistence.pojo.UserPOJO;
import br.com.javana.utils.MessageLoader;

public class UserDAO extends BaseDAO<UserPOJO> {

	public UserDAO(Class<UserPOJO> pClass) {
		super(UserPOJO.class);
	}

	@SuppressWarnings("unchecked")
	public List<UserPOJO> findByClientDept(String client, String department) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(UserPOJO.class);
			Criteria deptCli = crit.createCriteria("deptClient");
			deptCli.createCriteria("department").add(Restrictions.like("name", department));
			deptCli.createCriteria("client").add(Restrictions.like("name", client));
			crit.addOrder(Order.asc("name"));

			List<UserPOJO> result = crit.list();

			return result;

		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(57));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserPOJO> findByClientId(long id) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(UserPOJO.class);
			Criteria deptCli = crit.createCriteria("deptClient");
			deptCli.createCriteria("client").add(Restrictions.eq("id", id));

			List<UserPOJO> result = crit.list();

			return result;

		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(65));
		} finally {
			this.closeSession();
		}
	}

}
