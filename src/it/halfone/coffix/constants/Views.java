package it.halfone.coffix.constants;

import javax.servlet.http.HttpServletRequest;

/**
 * Views - 22/ott/2013
 *
 * @author Andrea La Rosa
 */
public abstract class Views {
	
	/**
	 * @param request
	 * @return
	 */
	private static String getPath(HttpServletRequest request){
		Boolean isMobile = (Boolean) request.getAttribute("mobile");
		if(isMobile == null || isMobile == false){
			return "/views/";
		} else return "/views/";
		//} else return "/views/mobile/";
	}
	
	public static String login(HttpServletRequest request){
		return getPath(request) + "loginPage.jsp";
	}
	
	public static String home(HttpServletRequest request){
		return getPath(request) + "homePage.jsp";
	}
	
	public static String groupOverview(HttpServletRequest request){
		return getPath(request) + "groupOverviewPage.jsp";
	}
	
	public static String registration(HttpServletRequest request){
		return getPath(request) + "registrationPage.jsp";
	}
	
	public static String chooseOfferer(HttpServletRequest request){
		return getPath(request) + "chooseOffererPage.jsp";
	}
	
	public static String manageGroup(HttpServletRequest request){
		return getPath(request) + "manageGroupPage.jsp";
	}
	
	public static String chooseOffereds(HttpServletRequest request){
		return getPath(request) + "chooseOfferedsPage.jsp";
	}
}
