package rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.ClientResponse.Status;

import dao.OrderEntryService;
import dao.Shoutout;

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



	// Example: http://localhost:8080/hw2/api/products?name=P1&price=1.23
	// http://localhost:8080/hw2/api/products?name=P2&price=1.234
	@Path("/shoutout")
	@POST
	public Response createShoutout(@FormParam("text") String text) {
		try {
			Shoutout shoutout = new Shoutout();
			shoutout.setsText(text);
			service.createShoutout(shoutout);
			return prepareResponse(true, shoutout);
		} catch (Exception ex) {
			String msg = ex.toString();
			return prepareResponse(false, msg);
		}
	}

}
