package it.halfone.coffix.filter;

import it.halfone.coffix.utils.UAgentInfo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class UserAgentFilter implements Filter{

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// doNothing()
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = req instanceof HttpServletRequest ? (HttpServletRequest) req : null;
		
		String userAgent = request.getHeader("User-Agent");
	    String httpAccept = request.getHeader("Accept");

	    UAgentInfo detector = new UAgentInfo(userAgent, httpAccept);

	    boolean isMobile = detector.detectTierTablet() || detector.detectTierIphone();
    	request.setAttribute("mobile", isMobile);
    	request.setAttribute("userAgentMainType", isMobile ? "mobile" : "desktop");

	    chain.doFilter(req, resp);
	}

	@Override
	public void destroy() {
		//doNothing()
	}
	
}
