package dao;

import java.util.List;

public class OrderEntryService {
	private static OrderEntryService thisInstance = null;
	private DAO dao;
	
	public OrderEntryService() {
		//this.loadTestData();
	}

	public static OrderEntryService getInstance() {
		synchronized (OrderEntryService.class) {
			if (thisInstance == null) {
				try {
					thisInstance = new OrderEntryService();
					thisInstance.init();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return thisInstance;
	}
	
	private void loadTestData() {
		Shoutout c = new Shoutout();
		c.setsText("Avi");
		this.createShoutout(c);

	}

	public void init() throws Exception {
		dao = new DAO();
		dao.init();
	}

	public void terminate() {
		dao.terminate();
	}

	public List<Shoutout> getAllOrders() {
		List<Shoutout> shoutouts = null;
		dao.beginTransaction();
		try {
			shoutouts = dao.getShoutoutDAO().getAllShoutoutTuples();
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return shoutouts;
	}


	// @POST
	// @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Shoutout createShoutout(Shoutout shoutout) {
		dao.beginTransaction();
		try {
			dao.getShoutoutDAO().createShoutout(shoutout);
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return shoutout;
	}

	

}
