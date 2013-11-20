package it.halfone.coffix.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RegistrationPage - 17/nov/2013
 *
 * @author Andrea La Rosa
 */
public class RegistrationPage extends HttpServlet{

	private static final long serialVersionUID = -4980249267119093908L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String forwardPage = "/views/test/registrationPage.jsp";
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
		dispatcher.forward(req, resp);
	}
}
