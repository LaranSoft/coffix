package it.halfone.coffix.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class GroupFilter implements Filter{

	@Override
	public void init(FilterConfig config) throws ServletException {
		// doNothing
	}
	
	@Override
	public void destroy() {
		// doNothing()
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		String groupId = req.getParameter("groupId");
		
		if(StringUtils.isEmpty(groupId)){
			HttpServletRequest request = req instanceof HttpServletRequest ? (HttpServletRequest) req : null;
			
			if(request.getMethod() == "POST"){
				resp.setCharacterEncoding("UTF-8");
			    resp.setContentType("text/plain");		
			    resp.getWriter().write("redirect_1");
				return;
			}
		}
		
		chain.doFilter(req, resp);
	}

}
