package dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DAO  {
	SessionFactory sessFact = null;
	Session session = null;
	ShoutoutDAO customerDAO = null;
	Transaction transaction = null;

	public void init() throws Exception {
		this.sessFact = HibernateUtil.getSessionFactory();
		customerDAO = new ShoutoutDAO();
	}

	public void terminate() {
		this.sessFact.close();
	}

	
	public ShoutoutDAO getCustomerDAO() {
		return customerDAO;
	}

	public SessionFactory getSessFact() {
		return sessFact;
	}

	public Session openSession() {
		this.session = this.sessFact.getCurrentSession();
		return session;
	}

	public void beginTransaction() {
		Session session = this.openSession();
		this.transaction = session.beginTransaction();
	}

	public void commitTransaction() {
		if (this.transaction != null) {
			this.transaction.commit();
			this.transaction = null;
			this.session = null;
		} else {
			throw new RuntimeException("There is current transaction");
		}
	}

	public void rollbackTransaction() {
		if (this.transaction != null) {
			this.transaction.rollback();
			this.transaction = null;
			this.session = null;
		} else {
			throw new RuntimeException("There is current transaction");
		}

	}
	
	class ShoutoutDAO {
		public Shoutout createShoutout(Shoutout shoutout) throws Exception {
			session.save(shoutout);
			return shoutout;
		}
		
		public List<Object> getAllShoutoutTuples() {
			List<Shoutout> shoutouts = null;
			String hql = "From Shoutout";
			Query query = session.createQuery(hql);
			List results = query.list();
			return results;
		}
		
	}
}
