package dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DAO  {
	SessionFactory sessFact = null;
	Session session = null;
	ShoutoutDAO shoutoutDAO = null;
	Transaction transaction = null;

	public void init() throws Exception {
		this.sessFact = HibernateUtil.getSessionFactory();
		shoutoutDAO = new ShoutoutDAO();
	}

	public void terminate() {
		this.sessFact.close();
	}

	
	public ShoutoutDAO getShoutoutDAO() {
		return shoutoutDAO;
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
	
	public class ShoutoutDAO {
		public Shoutout createShoutout(Shoutout shoutout) throws Exception {
			session.save(shoutout);
			return shoutout;
		}
		
		public List<Shoutout> getAllShoutoutTuples() {
			List<Shoutout> shoutouts = null;
			String hql = "From Shoutout";
			Query query = session.createQuery(hql);
			List results = query.list();
			return results;
		}
		
		public Shoutout getShoutout(int id) {
			Shoutout shoutout = null;
			String hql = "FROM Shoutout Where id = ?";
			Query query = session.createQuery(hql);
			query.setInteger(0, id);
			List results = query.list();
			if (results.size() > 0) {
				shoutout = (Shoutout) results.get(0);
			} else {
				System.out.println("No Customer Found");
			}
			return shoutout;
		}
	}
}
