package br.com.javana.persistence.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.javana.persistence.pojo.RemovedBoxPOJO;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;

public class RemovedBoxDAO extends BaseDAO<RemovedBoxPOJO> {

	public RemovedBoxDAO(Class<RemovedBoxPOJO> pClass) {
		super(RemovedBoxPOJO.class);

	}

	@SuppressWarnings("unchecked")
	public List<RemovedBoxPOJO> findBoxesByParams(Map<String, String> params) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria boxes = session.createCriteria(RemovedBoxPOJO.class);
			boxes.addOrder(Order.asc("code"));

			if (params.containsKey(ReportConstants.CLIENT_NAME_PARAMETER)) {
				boxes.add(Restrictions.eq("client", params.get(ReportConstants.CLIENT_NAME_PARAMETER)));
			}

			return boxes.list();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(29));
		} finally {
			this.closeSession();
		}

	}

}
