package it.halfone.coffix.filter;

import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.User;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AuthenticationFilter implements Filter{

	@Override
	public void destroy() {
		// doNothing()
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = req instanceof HttpServletRequest ? (HttpServletRequest) req : null;
		User user = (User) request.getSession().getAttribute(SessionKeys.USER);
		
		if(user == null){
			String requestedServlet = request.getRequestURI();
			if(requestedServlet.startsWith("/")){
				requestedServlet = requestedServlet.substring(1);
			}
			
			if(request.getMethod().equals("POST")){
				resp.setCharacterEncoding("UTF-8");
			    resp.setContentType("text/plain");		
			    resp.getWriter().write("redirect_0");
			} else {
				String fwd = requestedServlet;
				
				req.setAttribute("fwd", fwd);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher(Views.login(request));
				dispatcher.forward(req, resp);
			}
			return;
		}
		
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// doNothing()
	}

}
