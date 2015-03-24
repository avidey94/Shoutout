package rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.ClientResponse.Status;

import dao.Customer;
import dao.Order;
import dao.OrderEntryService;
import dao.Product;

@Path("/api")
public class Restful {
	private OrderEntryService service = null;

	public Restful() {
		service = OrderEntryService.getInstance();
	}

	public OrderEntryService getService() {
		return service;
	}

	public void setService(OrderEntryService service) {
		this.service = service;
	}

	private String toJson(Object value) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(value);
		return json;
	}

	protected Response prepareResponse(boolean isOk, Object resData) {
		try {
			GenericEntity<String> entity = new GenericEntity<String>(
					toJson(resData)) {
			};
			Response response = Response.status(Status.ACCEPTED).build();
			if (isOk) {
				response = Response.ok(entity).build();
			} else {
				// TBD - report failure
				response = Response.ok(entity).build();
			}
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	protected Response prepareResponse(Throwable ex) throws Exception {
		ex.printStackTrace();
		return this.prepareResponse(false, ex.getMessage());
	}

	// Send GET command: http://localhost:8080/hw2/ordersys/test
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test(@Context HttpServletRequest request,
			@Context HttpHeaders headers) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("_________________________HTTP request info_________________________"
				+ "\n");
		sb.append("servletPath=" + request.getServletPath() + "\n");
		sb.append("serverName=" + request.getServerName() + "\n");
		sb.append("serverPort=" + request.getServerPort() + "\n");
		sb.append("pathInfo=" + request.getPathInfo() + "\n");
		// sb.append("servletContext=" + request.getServletContext() + "\n");
		sb.append("parameterMap=" + request.getParameterMap() + "\n");
		sb.append("headers=" + headers.getRequestHeaders() + "\n");
		sb.append("queryString=" + request.getQueryString() + "\n");
		String msg = sb.toString();
		System.out.println(msg);

		return prepareResponse(true, msg);
	}

	// Example: http://localhost:8080/hw2/api/customers?name=Avi
	@Path("/customers")
	@POST
	public Response createCustomer(@FormParam("name") String custName) {
		try {
			Customer c = new Customer();
			c.setName(custName);
			service.createCustomer(c);
			return prepareResponse(true, c);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	@Path("/products")
	@GET
	public Response listProducts() {
		try {
			List<Product> products = service.getAllProducts();
			return prepareResponse(true, products);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	@Path("/customers")
	@GET
	public Response listCustomers() {
		try {
			List<Customer> customers = service.getAllCustomers();
			return prepareResponse(true, customers);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	// Example: http://localhost:8080/hw2/api/products?name=P1&price=1.23
	// http://localhost:8080/hw2/api/products?name=P2&price=1.234
	@Path("/products")
	@POST
	public Response createProduct(@FormParam("name") String name,
			@FormParam("price") double price) {
		try {
			Product o = new Product();
			o.setName(name);
			o.setPrice(price);
			service.createProduct(o);
			return prepareResponse(true, o);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	// Example: http://localhost:8080/hw2/api/orders
	@Path("/orders")
	@GET
	public Response listOrders() {
		try {
			List<Map<String, Object>> dtos = service.getAllOrderDTOs();
			return prepareResponse(true, dtos);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	// Example: http://localhost:8080/hw2/api/orders/1
	@Path("/orders/{orderID}")
	@GET
	public Response getOrderByID(@PathParam("orderID") int orderID) {
		try {
			Order order = service.getOrder(orderID);
			return prepareResponse(true, order);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	// Example: http://localhost:8080/hw2/api/orders?custID=1&productIDs=1,2
	@Path("/orders")
	@POST
	public Response submitOrder(@FormParam("custID") int custID,
			@FormParam("productIDs") String productIds) {
		try {
			Order o = service.submitOrder(custID, productIds);
			Map<String, Object> dto = null;
			if (o != null) {
				dto = service.getOrderAttributeMap(o);
			}
			return prepareResponse(true, dto);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	// Example: http://localhost:8080/hw2/api/orders/1
	@Path("/orders/{custID}")
	@DELETE
	public Response deleteOrder(@PathParam("custID") int orderID) {
		try {
			service.deleteOrder(orderID);
			return prepareResponse(true, "Order Deleted");
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	@Path("/products/{productID}/{newPrice}")
	@PUT
	public Response updateProductPrice(@PathParam("productID") int productID,
			@PathParam("newPrice") double newPrice) {
		try {
			Product product = service.updateProduct(productID, newPrice);

			return prepareResponse(true, product);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

	// Example: http://localhost:8080/hw2/api/orders/1
	@Path("/orders/{custID}/{productID}")
	@PUT
	public Response modifyOrder(@PathParam("custID") int orderID,
			@PathParam("productID") int productID) {
		try {
			// service.deleteOrder(orderID);
			return prepareResponse(true, "Order modified");
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

}
