package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import dao.Shoutout;

/**
 * Servlet implementation class LoginServlet
 */
public class AddShoutout extends HttpServlet {


public void doGet(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	    
	 DAO dao = new DAO();
     Shoutout shoutout = new Shoutout();
     shoutout.setsText(request.getParameter("text"));
     shoutout = dao.getShoutoutDAO().createShoutout(shoutout);
	   		    

	        
     HttpSession session = request.getSession(true);	    
     session.setAttribute("currentSessionUser",shoutout); 
     response.sendRedirect("index.html"); //logged-in page      		
} 
		
		
catch (Throwable theException) 	    
{
     System.out.println(theException); 
}
       }
	}