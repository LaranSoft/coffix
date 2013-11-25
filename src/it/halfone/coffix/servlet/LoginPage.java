package it.halfone.coffix.servlet;

import it.halfone.coffix.configuration.Configuration;
import it.halfone.coffix.constants.Paths;
import it.halfone.coffix.dao.PageDescription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * LoginPage - 16/nov/2013
 *
 * @author Andrea La Rosa
 */
public class LoginPage extends HttpServlet{

	private static final long serialVersionUID = 4228452589834800392L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PageDescription pd = new PageDescription();
		pd.getCssFiles().add(Paths.COMMON_CSS + "/login/login.css");
		pd.getCssFiles().add(Paths.CSS + "/" + req.getAttribute("userAgentMainType") + "/login/login.css");
		
		pd.getJsFiles().add(Paths.JS + "/login/login.js");
		
		Map<String, String> bundles = new HashMap<>();
		bundles.put("usernameLabel", Configuration.getInstance().get("INDEX.LOGIN_FORM.USERNAME_LABEL"));
		bundles.put("passwordLabel", Configuration.getInstance().get("INDEX.LOGIN_FORM.PASSWORD_LABEL"));
		bundles.put("buttonLabel", Configuration.getInstance().get("INDEX.LOGIN_FORM.BUTTON_LABEL"));
		bundles.put("noRegistrationMessage", Configuration.getInstance().get("INDEX.NO_REGISTRATION.MESSAGE"));
		bundles.put("noRegistrationLink", Configuration.getInstance().get("INDEX.NO_REGISTRATION.LINK"));
		
		pd.getData().put("bundles", bundles);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(pd));
	}
}
