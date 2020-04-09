package br.com.javana.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

	private static SessionFactory factory;

	private static final ThreadLocal sessionThread = new ThreadLocal();

	private static final ThreadLocal transactionThread = new ThreadLocal();

	static {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure();
		factory = cfg.buildSessionFactory();
	}

	@SuppressWarnings("unchecked")
	public static Session getSession() {
		Session session = (Session) sessionThread.get();
		if (session == null || !session.isOpen()) {
			session = factory.openSession();
			sessionThread.set(session);
		}
		return (Session) sessionThread.get();
	}

	@SuppressWarnings("unchecked")
	public static void closeSession() {
		Session session = (Session) sessionThread.get();
		if (session != null && session.isOpen()) {
			sessionThread.set(null);
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static void beginTransaction() {
		Transaction transaction = getSession().beginTransaction();
		transactionThread.set(transaction);
	}

	@SuppressWarnings("unchecked")
	public static void commitTransaction() {
		Transaction transaction = (Transaction) transactionThread.get();
		if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
			transaction.commit();
			transactionThread.set(null);
		}
	}

	@SuppressWarnings("unchecked")
	public static void rollbackTransaction() {
		Transaction transaction = (Transaction) transactionThread.get();
		if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
			transaction.rollback();
			transactionThread.set(null);
		}
	}

}
