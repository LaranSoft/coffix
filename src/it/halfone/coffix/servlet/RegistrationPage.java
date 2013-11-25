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
 * RegistrationPage - 17/nov/2013
 *
 * @author Andrea La Rosa
 */
public class RegistrationPage extends HttpServlet{

	private static final long serialVersionUID = -4980249267119093908L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PageDescription pd = new PageDescription();
		pd.getCssFiles().add(Paths.COMMON_CSS + "/registration/registration.css");
		pd.getCssFiles().add(Paths.CSS + "/" + req.getAttribute("userAgentMainType") + "/registration/registration.css");
		
		pd.getJsFiles().add(Paths.JS + "/registration/registration.js");
		
		Map<String, String> bundles = new HashMap<>();
		bundles.put("usernameLabel", Configuration.getInstance().get("REGISTRATION.REGISTRATION_FORM.USERNAME_LABEL"));
		bundles.put("passwordLabel", Configuration.getInstance().get("REGISTRATION.REGISTRATION_FORM.PASSWORD_LABEL"));
		bundles.put("confirmPasswordLabel", Configuration.getInstance().get("REGISTRATION.REGISTRATION_FORM.CONFIRM_PASSWORD_LABEL"));
		bundles.put("displayNameLabel", Configuration.getInstance().get("REGISTRATION.REGISTRATION_FORM.DISPLAY_NAME_LABEL"));
		bundles.put("buttonLabel", Configuration.getInstance().get("REGISTRATION.REGISTRATION_FORM.BUTTON_LABEL"));
		
		pd.getData().put("bundles", bundles);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(pd));
	}
}
