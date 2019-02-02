package exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.exc.AppException;
import exceptions.exc.ExceptionTestEJB;
import exceptions.exc.User;

@WebServlet("/excAppException")
@SuppressWarnings("unchecked")
public class JPAAppExceptionRollbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	ExceptionTestEJB et;
	
	@PersistenceContext
	EntityManager em;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>Exceptions - Application Exception no Rollback</h3>");
		
		try {
			Query query = em.createQuery("select u from User u");
			List<User> list = query.getResultList();
			System.out.println("Size: " + list.size());
			System.out.println(list);
			
			et.testAppException();
			
		} catch (AppException e) {
			System.out.println("Exception Thrown: " + e.getMessage());
		}
		
	}
}
