package it.halfone.coffix.filter;

import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.User;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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
			if(request.getMethod().equals("POST")){
				resp.setCharacterEncoding("UTF-8");
			    resp.setContentType("text/plain");		
			    resp.getWriter().write("redirect_0");
			    return;
			} 
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// doNothing()
	}

}
