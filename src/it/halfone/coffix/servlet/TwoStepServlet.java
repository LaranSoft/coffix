package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Servlets;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Context;
import it.halfone.coffix.dao.TwoStepInfo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TwoStepServlet - 21/ott/2013
 * <br>
 * Implementazione base delle servlet che seguono il protocollo TwoStep.
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public abstract class TwoStepServlet extends HttpServlet{

	private static final long serialVersionUID = 3580343192851376528L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TwoStepInfo info = (TwoStepInfo) req.getSession().getAttribute(SessionKeys.TWO_STEP_CONTEXT);
		String forwardPage = Servlets.LOGIN_SERVLET;
		
		String target = req.getRequestURI();
		if(target.startsWith("/")){
			target = target.substring(1);
		}
		
		if(info != null && target.equals(info.getTarget())){
			forwardPage = secondStep(req, info.getContext());
		}
		
		if(forwardPage.matches("/?" + target)){
			forwardPage = Views.login(req);
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
		dispatcher.forward(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Context context = firstStep(req);
		
		String target = req.getRequestURI();
		if(target.startsWith("/")){
			target = target.substring(1);
		}
		
		TwoStepInfo info = new TwoStepInfo(target, context);
		req.getSession().setAttribute(SessionKeys.TWO_STEP_CONTEXT, info);
		
		String response = context.getResponse();
		if(response == null){
			response = target;
		}
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(response);
	}
	
	protected abstract Context firstStep(HttpServletRequest req);
	
	protected abstract String secondStep(HttpServletRequest request, Context ctx);

}
