package dao;

import java.util.List;

import javax.persistence.Tuple;



public class TestServiceLayer {
	public static void main(String[] args) throws Exception {
		OrderEntryService service = new OrderEntryService();
		service.init();
		
		createSampleData(service);
		loadSampleData(service);
		service.terminate();
	}

	private static void createSampleData(OrderEntryService service) {
		Customer c = new Customer();
		c.setName("Avi");
		service.createCustomer(c);
		
		Customer c1 = new Customer();
		c1.setName("baba");
		service.createCustomer(c1);
		
		
		Product p = new Product();
		p.setName("P-0");
		//p.setProduct_order(o);
		service.createProduct(p);
		
		Product p1 = new Product();
		p1.setName("P-1");
		//p1.setProduct_order(o1);
		service.createProduct(p1);
		
		Order o = new Order();
		o.setCustomer(c1);
		o.setTotal(100);
		
		OrderProduct op1 = new OrderProduct();
		op1.setOrder(o);
		op1.setProduct(p);
		
		OrderProduct op2 = new OrderProduct();
		op2.setOrder(o);
		op2.setProduct(p1);
		o.getOrderProduct().add(op1);
		o.getOrderProduct().add(op2);
		service.createOrder(o);
		System.out.println(o);

		Order o1 = new Order();
		o1.setCustomer(c);
		o1.setTotal(200);
		service.createOrder(o1);
		
		//service.deleteOrder(o.getId());
	}
	
	private static void loadSampleData(OrderEntryService service) {
		System.out.println("LOADING DATA!!");
		//get order by ID
		
		//works
		System.out.println("----GET ALL ORDERS----");
		List<Order> orders = service.getAllOrders();
		for (Order order : orders) {
			System.out.println(order);
			System.out.println(service.getOrderAttributeMap(order));
		}
		System.out.println("----GET ALL ORDERS----\n");
		
		//works
		System.out.println("----GET AN ORDER BY ID----");
		Order myOrder = service.getOrder(1);
		System.out.println(myOrder);
		System.out.println("----GET AN ORDER BY ID----\n");
		
		//works
		System.out.println("----GET ALL PRODUCTS----");
		List<Product> products = service.getAllProducts();
		for (Product product : products) {
			System.out.println(product);
		}
		System.out.println("----GET ALL PRODUCTS----\n");
		
		
		//works
		System.out.println("----GET ALL CUSTOMER tuples----");
		List<Object> tuples = service.getAllCustomerTuples();
		for (Object customer : tuples) {
			System.out.println(customer);
		}
		System.out.println("----GET ALL CUSTOMERS tuples----\n");
		
		/*
		//works
		System.out.println("----GET ALL CUSTOMERS----");
		List<Customer> customers = service.getAllCustomers();
		for (Customer customer : customers) {
			System.out.println(customer);
		}
		System.out.println("----GET ALL CUSTOMERS----\n");
		
		//service.updateProduct(1, 54.5);
		*/
		//Order o2 = service.deleteOrder(2);
		
	}
}
