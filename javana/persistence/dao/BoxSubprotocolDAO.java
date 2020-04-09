package br.com.javana.persistence.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.javana.dto.SubProtocolDTO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;

public class BoxSubprotocolDAO extends BaseDAO<BoxSubProtocolPOJO> {

	public BoxSubprotocolDAO(Class<BoxSubProtocolPOJO> pClass) {
		super(pClass);
	}

	public BoxSubProtocolPOJO findByBoxAndSubId(long boxId, long subId) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("box").add(Restrictions.like("id", boxId));
			crit.createCriteria("subprotocol").add(Restrictions.like("id", subId));
			crit.add(Restrictions.isNull("backDate"));

			BoxSubProtocolPOJO pojo = (BoxSubProtocolPOJO) crit.uniqueResult();

			return pojo;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(30));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> checkForUnavailableBoxes(List<Long> boxes) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			Criteria box = crit.createCriteria("box");
			box.add(Restrictions.in("code", boxes));
			crit.add(Restrictions.isNull("backDate"));
			crit.createCriteria("subprotocol").createCriteria("protocol").addOrder(Order.asc("number"));
			box.addOrder(Order.asc("code"));
			List<BoxSubProtocolPOJO> pojos = crit.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(31));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> findBoxesByProtocol(long number) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("box").addOrder(Order.asc("code"));
			Criteria sub = crit.createCriteria("subprotocol");
			sub.createCriteria("requester").createCriteria("deptClient").createCriteria("department").addOrder(
					Order.asc("name"));
			sub.createCriteria("protocol").add(Restrictions.eq("number", number));

			List<BoxSubProtocolPOJO> pojos = sub.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(30));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public BoxSubProtocolPOJO findByNumberFullyOpen(Long value) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").createCriteria("protocol").add(Restrictions.eq("number", value));
			List<BoxSubProtocolPOJO> subs = crit.list();

			boolean aux = false;

			for (BoxSubProtocolPOJO pojo : subs) {
				if (pojo.getBackDate() != null) {
					aux = true;
				}
			}

			if (aux == false) {
				return subs.get(0);
			} else {
				return null;
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(32));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> findBoxesBySubId(long subprotocolId) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").add(Restrictions.like("id", subprotocolId));

			return crit.list();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(30));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Long> findBoxCodesBySubId(SubProtocolDTO selectedSubprotocol) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").add(Restrictions.like("id", selectedSubprotocol.getId()));
			List<BoxSubProtocolPOJO> pojos = crit.list();
			List<Long> codes = new ArrayList<Long>();

			for (BoxSubProtocolPOJO pojo : pojos) {
				codes.add(pojo.getBox().getCode());
			}

			return codes;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(33));
		} finally {
			this.closeSession();
		}
	}

	public BoxSubProtocolPOJO findByBoxCodeAndSubId(Long code, long id) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").add(Restrictions.like("id", id));
			crit.createCriteria("box").add(Restrictions.eq("code", code));

			return (BoxSubProtocolPOJO) crit.uniqueResult();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(30));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> findBoxesCodesBySubId(long id) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").add(Restrictions.like("id", id));

			return crit.list();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(33));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> checkForUnavailableBoxesByInterval(Long from, Long to) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("box").add(Restrictions.between("code", from, to));
			crit.add(Restrictions.isNull("backDate"));
			List<BoxSubProtocolPOJO> pojos = crit.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(34));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> checkForUnavailableBoxesByProtocolNumber(Long number) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").createCriteria("protocol").add(Restrictions.eq("number", number));
			crit.add(Restrictions.isNull("backDate"));
			List<BoxSubProtocolPOJO> pojos = crit.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(35));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings( { "unchecked", "deprecation" })
	public List<BoxSubProtocolPOJO> findByParams(Map<String, String> params) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);

			Criteria deptClient = null;
			Criteria subprot = null;

			if (!params.isEmpty()) {
				subprot = crit.createCriteria("subprotocol");
				deptClient = subprot.createCriteria("requester").createCriteria("deptClient");
			}

			if (params.containsKey(ReportConstants.CLIENT_NAME_PARAMETER)) {
				deptClient.createCriteria("client").add(
						Restrictions.ilike("name", params.get(ReportConstants.CLIENT_NAME_PARAMETER)));
			}

			if (params.containsKey(ReportConstants.DEPARTMENT_NAME_PARAMETER)) {
				deptClient.createCriteria("department").add(
						Restrictions.ilike("name", params.get(ReportConstants.DEPARTMENT_NAME_PARAMETER)));
			}

			if (params.containsKey(ReportConstants.FROM_PARAMETER)) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				try {
					Date to = (Date) format.parseObject(params.get(ReportConstants.TO_PARAMETER));
					to.setHours(23);
					to.setMinutes(59);
					to.setSeconds(59);
					Criteria prot = subprot.createCriteria("protocol");
					prot.add(Restrictions.between("leaveDate", format.parseObject(params
							.get(ReportConstants.FROM_PARAMETER)), to));
					prot.addOrder(Order.asc("number"));
				} catch (ParseException e) {
					throw new PersistenceException(MessageLoader.getInstance().getMessage(48));
				}

			} else {
				crit.add(Restrictions.isNull("backDate"));
				crit.createCriteria("box").addOrder(Order.asc("code"));
			}

			return crit.list();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(36));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public boolean findOpenProtocolsByClient(Long clientId) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").createCriteria("requester").createCriteria("deptClient").createCriteria(
					"client").add(Restrictions.eq("id", clientId));
			crit.add(Restrictions.isNull("backDate"));
			List<BoxSubProtocolPOJO> pojos = crit.list();

			if (pojos.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(37));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> findByClientId(long id) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("subprotocol").createCriteria("requester").createCriteria("deptClient").createCriteria(
					"client").add(Restrictions.eq("id", id));

			return crit.list();
		} catch (Exception e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(38));
		} finally {
			this.closeSession();
		}
	}

	public BoxSubProtocolPOJO checkForUnavailableBoxesByCode(Long value) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("box").add(Restrictions.eq("code", value));
			crit.add(Restrictions.isNull("backDate"));

			return (BoxSubProtocolPOJO) crit.uniqueResult();
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(39));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> boxHistory(Long boxCode) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("box").add(Restrictions.eq("code", boxCode));
			crit.addOrder(Order.asc("backDate"));
			return crit.list();
		} catch (Exception e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(40));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> findByBoxCodes(List<Long> codes) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			crit.createCriteria("box").add(Restrictions.in("code", codes));
			return crit.list();
		} catch (Exception e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(41));
		} finally {
			this.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findBackBetweenDates(String from, String to) throws PersistenceException {

		Session session = null;
		try {
			session = this.getOpenSession();
			String query = "select box.code as code, boxsub.backDate as date, protocol.number as number, p.name as name " +
				"from box_sub_protocol as boxsub " +
				"left join sub_protocol as sub on boxsub.id_sub_protocol = sub.id " +
				"left join box as box on box.id = boxsub.id_box " +
				"left join protocol as protocol on protocol.id = sub.id_protocol " +
				"left join person as p on sub.id_user = p.id " +
				"where backdate between '" + from + "' and '" + to +
				"' order by protocol.number, p.name, box.code";
			return session.createSQLQuery(query).addScalar("code", Hibernate.LONG).addScalar("number", Hibernate.LONG)
				.addScalar("name", Hibernate.STRING).list();

		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(42));
		} finally {
			this.closeSession();
		}

	}

	@SuppressWarnings("unchecked")
	public List<BoxSubProtocolPOJO> checkBoxesAreAvailable(List<Long> boxes) throws PersistenceException {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(BoxSubProtocolPOJO.class);
			Criteria box = crit.createCriteria("box");
			box.add(Restrictions.in("code", boxes));
			crit.add(Restrictions.isNotNull("backDate"));
			box.addOrder(Order.asc("code"));
			List<BoxSubProtocolPOJO> pojos = crit.list();

			return pojos;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(69));
		} finally {
			this.closeSession();
		}
	}

}
