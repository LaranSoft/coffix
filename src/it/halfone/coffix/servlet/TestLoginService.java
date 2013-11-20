package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;

/**
 * TestLoginService - 16/nov/2013
 *
 * @author Andrea La Rosa
 */
public class TestLoginService extends HttpServlet {

	private static final long serialVersionUID = -445170354729752266L;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String username = request.getParameter("u");
		String password = request.getParameter("p");
		
		Map<String, String> outcome = new HashMap<>();
		
		try {
			if(StringUtils.isEmpty(username)){
				throw new CoffixException("empty username", "200");
			}
			
			if(StringUtils.isEmpty(password)){
				throw new CoffixException("empty password", "201");
			}
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Key userKey = KeyFactory.createKey(Keys.User.KIND, Keys.User.NAME);
			
			Filter usernamefilter = new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.EQUAL, username);
			Filter passwordfilter = new FilterPredicate(Entities.User.Property.PASSWORD, FilterOperator.EQUAL, password);
			
			Query query = new Query(Entities.User.KIND, userKey).setFilter(CompositeFilterOperator.and(usernamefilter, passwordfilter));
			Entity userEntity = datastore.prepare(query).asSingleEntity();
			
			if(userEntity == null){
				throw new CoffixException("wrong username or password", "300");
			}
			
			User loggedUser = new User();
			loggedUser.setBaseName((String) userEntity.getProperty(Entities.User.Property.BASENAME));
			loggedUser.setEntityKey(KeyFactory.keyToString(userEntity.getKey()));
			loggedUser.setUsername(username);
			request.getSession().setAttribute(SessionKeys.USER, loggedUser);
			
			outcome.put("status", "OK");
		} catch(CoffixException ce){
			ce.printStackTrace();
			outcome.put("errorCode", ce.getErrorCode());
			outcome.put("status", "KO");
		} catch(Throwable t){
			t.printStackTrace();
			outcome.put("status", "KO");
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().write(new Gson().toJson(outcome));
    }
}