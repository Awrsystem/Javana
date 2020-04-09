package br.com.javana.persistence.dao;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleStateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import br.com.javana.business.ChildException;
import br.com.javana.persistence.HibernateUtil;
import br.com.javana.persistence.pojo.BasePOJO;
import br.com.javana.utils.MessageLoader;

public class BaseDAO<P extends BasePOJO> {
	/** Guarda a referencia do pojo passado na declaração */
	private Class gPojoClass;

	/** Guarda a sessionfactory do hibernate criada na contrução */
	private SessionFactory factory;

	/**
	 * guarda o logger do log4j por ser statico já pega na hora em que a classe
	 * eh carregada
	 */
	protected static Logger log = Logger.getLogger(BaseDAO.class.getName());

	/**
	 * @throws EngineG2PersistenceException
	 */
	public BaseDAO(Class<P> pClass) {
		super();

		gPojoClass = pClass;
	}

	public SessionFactory getSessionFactory() {
		return factory;
	}

	protected Session getOpenSession() {
		return HibernateUtil.getSession();
	}

	public void closeSession() {
		HibernateUtil.closeSession();
	}

	public void beginTransaction() {
		HibernateUtil.beginTransaction();
	}

	public void commitTransaction() {
		HibernateUtil.commitTransaction();
	}

	public void rollbackTransaction() {
		HibernateUtil.rollbackTransaction();
	}

	/**
	 * @param pojo
	 * @throws PersistenceException
	 * @throws EngineG2Exception
	 * 
	 */
	public Serializable insert(P pojo) throws PersistenceException {
		Session session = null;
		Serializable key = null;

		try {
			session = this.getOpenSession();

			this.beginTransaction();

			key = session.save(pojo);
			session.flush();

			this.commitTransaction();
			return key;

		} catch (HibernateException e) {
			this.rollbackTransaction();
			throw new PersistenceException(e);
		} finally {
			try {
				this.closeSession();
			} catch (RuntimeException e) {
				throw new SystemException(e);
			}
		}
	}

	/**
	 * @param pojo
	 * @throws EngineG2Exception
	 */
	public void delete(P pojo) throws Exception {
		Session session = null;

		try {
			session = this.getOpenSession();
			this.beginTransaction();

			session.delete(pojo);

			this.commitTransaction();

		} catch (ConstraintViolationException e) {
			throw new ChildException();
		} catch (StaleStateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(17));
		} catch (HibernateException e) {

			this.rollbackTransaction();

		} finally {
			try {
				this.closeSession();
			} catch (RuntimeException e) {
				throw new PersistenceException(MessageLoader.getInstance().getMessage(18));
			}
		}

	}

	/**
	 * @param pojo
	 * @throws EngineG2Exception
	 */
	public void update(P pojo) throws Exception {
		Session session = null;

		try {
			session = this.getOpenSession();

			this.beginTransaction();

			session.update(pojo);
			session.flush();

			this.commitTransaction();
		}

		catch (HibernateException e) {
			this.rollbackTransaction();
			throw new PersistenceException(MessageLoader.getInstance().getMessage(19));

		} finally {
			try {
				this.closeSession();
			} catch (RuntimeException e) {
				throw new PersistenceException(MessageLoader.getInstance().getMessage(18));	
			}
		}

	}

	/**
	 * @param basePojo
	 * @return List dos pojos encontrados.
	 * @throws EngineG2PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public List<P> findAllByNameOrder() throws Exception {
		Session session = null;
		try {
			session = this.getOpenSession();

			List<P> list = session.createQuery("from " + gPojoClass.getName() + " pojo order by pojo.name asc").list();

			return list;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(20));
		} finally {
			try {
				this.closeSession();
			} catch (RuntimeException e) {
				throw new PersistenceException(MessageLoader.getInstance().getMessage(18));
			}
		}
	}

	/**
	 * @param basePojo
	 * @return List dos pojos encontrados.
	 * @throws EngineG2PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public List<P> findAll() throws Exception {
		Session session = null;
		try {
			session = this.getOpenSession();

			List<P> list = session.createQuery("from " + gPojoClass.getName() + " pojo order by pojo.id asc").list();

			return list;
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(21));
		} finally {
			try {
				this.closeSession();
			} catch (RuntimeException e) {
				throw new PersistenceException(MessageLoader.getInstance().getMessage(18));
			}
		}
	}

	/**
	 * @param basePojo
	 * @param pk
	 * @return o Pojo Encontrado.
	 * @throws DemoPersistenceException
	 */
	@SuppressWarnings("unchecked")
	public P findByPK(Serializable pk) throws Exception {
		Session session = null;
		try {
			session = this.getOpenSession();
			P rpojo = (P) session.load(gPojoClass, pk);

			return rpojo;
		} catch (ClassCastException ec) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(22));
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(22));		
		}
	}

	@SuppressWarnings("unchecked")
	public List<P> findByName(String name) throws Exception {
		Session session = null;
		try {
			session = this.getOpenSession();
			Criteria crit = session.createCriteria(this.gPojoClass);
			crit.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE));
			List<P> result = crit.list();

			return result;

		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(23));
		} finally {
			this.closeSession();
		}

	}

	@SuppressWarnings("unchecked")
	public P findByExactName(String name) throws Exception {
		Session session = null;
		try {
			session = this.getOpenSession();

			Criteria crit = session.createCriteria(gPojoClass);
			crit.add(Restrictions.eq("name", name));
			List<P> result = crit.list();

			if (result.size() > 0) {
				return result.get(0);
			} else {
				return null;
			}
		} catch (HibernateException e) {
			throw new PersistenceException(MessageLoader.getInstance().getMessage(24));
		} finally {
			this.closeSession();
		}

	}

}
