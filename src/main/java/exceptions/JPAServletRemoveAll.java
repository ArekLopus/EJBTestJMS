package exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import exceptions.exc.User;

@WebServlet("/excRemoveAll")
@SuppressWarnings("unchecked")
public class JPAServletRemoveAll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;
	
	@Resource
	UserTransaction ut;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h3>JPA - Remove All Entries</h3>");
		
		try {
			
			//EntityTransaction ut = em.getTransaction();
			
			ut.begin();
			
			int update = em.createQuery("delete from User").executeUpdate();
			System.out.println("Updated rows: " + update);
			
			ut.commit();
			
			Query query = em.createQuery("select u from User u");
			
			List<User> list = query.getResultList();
			System.out.println("Size after update: " + list.size());
			
		} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException 
				| RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			System.out.println("Exception thrown: " + e.getMessage());
		}
		
	}
}
