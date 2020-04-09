package br.com.javana.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.javana.persistence.pojo.DepartmentClientPOJO;
import br.com.javana.persistence.pojo.DepartmentPOJO;
import br.com.javana.utils.MessageLoader;

public class DepartmentClientDAO extends BaseDAO<DepartmentClientPOJO> {

	public DepartmentClientDAO(Class pClass) {
		super(DepartmentClientPOJO.class);
	}

	public DepartmentClientPOJO findByClientAndDepartment(String company, String department) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(DepartmentClientPOJO.class);
			crit.createCriteria("department").add(Restrictions.like("name", department));
			crit.createCriteria("client").add(Restrictions.like("name", company));

			DepartmentClientPOJO result = (DepartmentClientPOJO) crit.uniqueResult();

			return result;

		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(51));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<DepartmentPOJO> findByClient(String company) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(DepartmentClientPOJO.class);
			crit.createCriteria("client").add(Restrictions.like("name", company));
			crit.createCriteria("department").addOrder(Order.asc("name"));

			List<DepartmentClientPOJO> pojos = crit.list();

			List<DepartmentPOJO> departments = new ArrayList<DepartmentPOJO>();

			for (DepartmentClientPOJO pojo : pojos) {
				departments.add(pojo.getDepartment());
			}

			return departments;

		} catch (ObjectNotFoundException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(3));
		}

		catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(52));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<DepartmentClientPOJO> findByClientId(long id) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(DepartmentClientPOJO.class);
			crit.createCriteria("client").add(Restrictions.eq("id", id));

			return crit.list();

		}

		catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(52));
		} finally {
			this.closeSession();
		}
	}

}
