package br.com.javana.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.javana.dto.BoxCountDTO;
import br.com.javana.persistence.pojo.BoxPOJO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.persistence.pojo.DepartmentClientPOJO;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;

public class BoxDAO extends BaseDAO<BoxPOJO> {
	public BoxDAO(Class pClass) {
		super(BoxPOJO.class);
	}

	public long pickNumber() throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria boxes = session.createCriteria(BoxPOJO.class);
			boxes.addOrder(Order.desc("code"));
			boxes.setMaxResults(1);

			BoxPOJO lastBox = (BoxPOJO) boxes.uniqueResult();

			if (lastBox == null) {
				return 0;
			} else {
				return lastBox.getCode();
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(25));
		} finally {
			this.closeSession();
		}
	}

	public BoxPOJO findByCode(long code) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria boxes = session.createCriteria(BoxPOJO.class);
			boxes.add(Restrictions.eq("code", code));
			BoxPOJO lastBox = (BoxPOJO) boxes.uniqueResult();

			return lastBox;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(26));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxPOJO> getAllCodes(BoxPOJO box) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			List<BoxPOJO> results = session.createCriteria(BoxPOJO.class).addOrder(Order.asc("code")).list();

			return results;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(27));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxPOJO> findByInterval(Long from, Long to) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria boxes = session.createCriteria(BoxPOJO.class);
			boxes.add(Restrictions.between("code", from, to));
			boxes.addOrder(Order.asc("code"));

			return boxes.list();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(28));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxPOJO> findBoxesByParams(Map<String, String> params) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria boxes = session.createCriteria(BoxPOJO.class);
			boxes.addOrder(Order.asc("year"));
			boxes.addOrder(Order.asc("month"));
			boxes.addOrder(Order.asc("subject"));

			Criteria deptClient = null;

			if (!params.isEmpty()) {
				deptClient = boxes.createCriteria("deptClient");
			}

			if (params.containsKey(ReportConstants.CLIENT_NAME_PARAMETER)) {
				deptClient.createCriteria("client").add(
						Restrictions.eq("name", params.get(ReportConstants.CLIENT_NAME_PARAMETER)));
			}

			if (params.containsKey(ReportConstants.DEPARTMENT_NAME_PARAMETER)) {
				deptClient.createCriteria("department").add(
						Restrictions.eq("name", params.get(ReportConstants.DEPARTMENT_NAME_PARAMETER)));
			}

			if (params.containsKey(ReportConstants.MONTH_PARAMETER)) {
				String month = params.get(ReportConstants.MONTH_PARAMETER);
				if (!month.equals("")) {
					boxes.add(Restrictions.eq("month", params.get(ReportConstants.MONTH_PARAMETER)));
				}
			}

			if (params.containsKey(ReportConstants.YEAR_PARAMETER)) {
				boxes.add(Restrictions.eq("year", params.get(ReportConstants.YEAR_PARAMETER)));
			}

			List list = boxes.list();

			return list;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(29));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxPOJO> findByCodes(List<Long> codes) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxPOJO.class);
			crit.add(Restrictions.in("code", codes));
			crit.addOrder(Order.asc("code"));
			List<BoxPOJO> pojos = crit.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(26));
		} finally {
			this.closeSession();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see br.com.javana.persistence.dao.IBoxDAO#findByIntervalClientAvail(java.lang.Long,
	 *      java.lang.Long, java.lang.String) Verifica primeiro se as caixas
	 *      estao disponiveis e se sao do cliente selecionado Devolve somente as
	 *      caixas que são cliente e que estão disponiveis
	 */
	@SuppressWarnings("unchecked")
	public List<BoxPOJO> findByIntervalClientAvail(Long from, Long to, String client) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria boxes = session.createCriteria(BoxPOJO.class);
			boxes.createCriteria("deptClient").createCriteria("client").add(Restrictions.eq("name", client));
			boxes.add(Restrictions.between("code", from, to));
			boxes.addOrder(Order.asc("id"));

			List<BoxPOJO> clientBoxes = boxes.list();

			if (clientBoxes.size() > 0) {
				List<BoxSubProtocolPOJO> unavailable = new ArrayList<BoxSubProtocolPOJO>();

				Criteria av = session.createCriteria(BoxSubProtocolPOJO.class);
				av.add(Restrictions.in("box", clientBoxes));
				av.add(Restrictions.isNull("backDate"));
				unavailable = av.list();

				for (BoxSubProtocolPOJO sub : unavailable) {
					if (clientBoxes.contains(sub.getBox())) {
						clientBoxes.remove(sub.getBox());
					}
				}
			}

			return clientBoxes;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(59));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxCountDTO> boxCountByClient() throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria boxes = session.createCriteria(BoxPOJO.class).setProjection(
					Projections.projectionList().add(Projections.rowCount()).add(
							Projections.groupProperty("deptClient")));

			@SuppressWarnings("unused")
			List<Object[]> list = boxes.list();
			List<BoxCountDTO> dtos = new ArrayList<BoxCountDTO>();

			for (Object[] o : list) {
				BoxCountDTO dto = new BoxCountDTO();
				dto.setClient(((DepartmentClientPOJO) o[1]).getClient().getName());
				dto.setCount((Integer) o[0]);
				dtos.add(dto);
			}

			return dtos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(60));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxPOJO> findByClientId(long id) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxPOJO.class);
			crit.createCriteria("deptClient").createCriteria("client").add(Restrictions.eq("id", id));

			List<BoxPOJO> pojos = crit.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(61));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Long> checkOwnership(Set<Long> allBoxes, String client) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxPOJO.class);
			crit.add(Restrictions.in("code", allBoxes));
			crit.createCriteria("deptClient").createCriteria("client").add(Restrictions.eq("name", client));

			List<BoxPOJO> pojos = crit.list();
			List<Long> result = new ArrayList<Long>();

			for (BoxPOJO pojo : pojos) {
				result.add(pojo.getCode());
			}

			return result;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(62));
		} finally {
			this.closeSession();
		}
	}

}
