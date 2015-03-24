package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		Customer c = new Customer();
		c.setName("Avi");
		this.createCustomer(c);
		
		Customer c1 = new Customer();
		c1.setName("baba");
		this.createCustomer(c1);
		
		
		Product p = new Product();
		p.setName("P-0");
		//p.setProduct_order(o);
		this.createProduct(p);
		
		Product p1 = new Product();
		p1.setName("P-1");
		//p1.setProduct_order(o1);
		this.createProduct(p1);
		
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
		this.createOrder(o);
		System.out.println(o);

		Order o1 = new Order();
		o1.setCustomer(c);
		o1.setTotal(200);
		this.createOrder(o1);
	}

	public void init() throws Exception {
		dao = new DAO();
		dao.init();
	}

	public void terminate() {
		dao.terminate();
	}

	public List<Order> getAllOrders() {
		List<Order> orders = null;
		dao.beginTransaction();
		try {
			orders = dao.getOrderDAO().getAllOrders();
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return orders;
	}
	
	public Map<String,Object> getCustomerAttributeMap(Customer c) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", c.getId());
		map.put("name", c.getName());
		return map;
	}

	public Map<String,Object> getProductAttributeMap(Product p) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", p.getId());
		map.put("name", p.getName());
		return map;
	}

	public Map<String,Object> getOrderAttributeMap(Order o) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", o.getId());
		map.put("total", o.getTotal());
		Set<OrderProduct> opList = o.getOrderProduct();
		map.put("productCount", opList.size());
		int count = opList.size();
		if (count > 0) {
			List<Map<String,Object>> prodList = new ArrayList<Map<String,Object>>();
			for (OrderProduct op : opList) {
				Product p = op.getProduct();
				Map<String,Object> attribs = getProductAttributeMap(p);
				prodList.add(attribs);
			}
			map.put("products", prodList);
		}
		return map;
	}

	public List<Map<String,Object>> getAllOrderDTOs() {
		List<Order> orders = this.getAllOrders();
		List<Map<String,Object>> dtos = new ArrayList<Map<String,Object>>();
		for (Order order : orders) {
			dtos.add(this.getOrderAttributeMap(order));
		}
		return dtos;
	}

	public Order getOrder(int id) {
		Order order = null;
		dao.beginTransaction();
		try {
			order = dao.getOrderDAO().getOrder(id);
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return order;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = null;
		dao.beginTransaction();
		try {
			customers = dao.getCustomerDAO().getAllCustomers();
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return customers;
	}

	public List<Product> getAllProducts() {
		List<Product> products = null;
		dao.beginTransaction();
		try {
			products = dao.getProductDAO().getAllProducts();
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return products;
	}
	public List<Map<String, Object>> getAllProductDTOs() {
		// TODO Auto-generated method stub
		return null;
	}


	/*
	public List<Customer> getAllCustomers() {
		List<Customer> customers = null;
		dao.beginTransaction();
		try {
			customers = dao.getCustomerDAO().getAllCustomers();
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return customers;
	}
	*/
	public List<Object> getAllCustomerTuples() {
		List<Object> customers = null;
		dao.beginTransaction();
		try {
			customers = dao.getCustomerDAO().getAllCustomerTuples();
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return customers;
	}
	public Order createOrder(Order o) {
		dao.beginTransaction();
		try {
			dao.getOrderDAO().createOrder(o);
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return o;
	}

	// @POST
	// @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Customer createCustomer(Customer c) {
		dao.beginTransaction();
		try {
			dao.getCustomerDAO().createCustomer(c);
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return c;
	}

	public Product createProduct(Product p) {
		dao.beginTransaction();
		try {
			dao.getProductDAO().createProduct(p);
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return p;
	}

	public Product updateProduct(int productID, double newPrice) {
		dao.beginTransaction();
		try {
			Product product = dao.getProductDAO().updateProduct(productID, newPrice);
			dao.commitTransaction();
			return product;
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return null;
	}

	public void deleteOrder(int orderID) {
		dao.beginTransaction();
		try {
			Order order = dao.getOrderDAO().getOrder(orderID);
			order.setOrderProduct(new HashSet<OrderProduct>());
			dao.getOrderDAO().updateOrder(order);
			dao.getOrderDAO().deleteOrder(orderID);
			dao.commitTransaction();
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
	}

	public Order submitOrder(int custID, String productIds) {
		dao.beginTransaction();
		try {
			Customer c = dao.getCustomerDAO().getCustomer(custID);
			List<Integer> pidList = new ArrayList<Integer>();
			if (productIds != null) {
				String[] ids = productIds.split(",");
				if (ids != null) {
					for (int i = 0; i < ids.length; i++) {
						Integer id = Integer.parseInt(ids[i]);
						pidList.add(id);
					}
				}
			}
			List<Product> products = dao.getProductDAO().getProducts(pidList);
			Order order = new Order();
			order.setCustomer(c);
			double totalPrice = 0.0;
			for (Product p : products) {
				OrderProduct op = new OrderProduct();
				op.setOrder(order);
				op.setProduct(p);
				order.getOrderProduct().add(op);
				totalPrice = totalPrice + p.getPrice();
			}
			order.setTotal(totalPrice);
			dao.getOrderDAO().createOrder(order);
			dao.commitTransaction();
			return order;
		} catch (Exception ex) {
			dao.rollbackTransaction();
		}
		return null;
	}

}
