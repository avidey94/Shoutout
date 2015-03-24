package rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.Customer;
import dao.Order;
import dao.OrderEntryService;
import dao.OrderProduct;
import dao.Product;

public class MyAppServletContextListener implements ServletContextListener {
	private OrderEntryService service = null;

	//@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContextListener destroyed");
	}

	// Run this before web application is started
	//@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ServletContextListener started");
		OrderEntryService service = OrderEntryService.getInstance();
		this.initalizeDB(service);
	}
	
	private void initalizeDB(OrderEntryService service) {
		//test data goes here
	}
}