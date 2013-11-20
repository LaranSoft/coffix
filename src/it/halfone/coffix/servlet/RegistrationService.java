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
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.Gson;

/**
 * RegistrationService - 16/nov/2013
 *
 * @author Andrea La Rosa
 */
public class RegistrationService extends HttpServlet {

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
		String baseName = request.getParameter("d");
		String userName = request.getParameter("u");
		String password = request.getParameter("p");
		String confirmPassword = request.getParameter("cp");
		
		DatastoreService datastore = null;
		Transaction txn = null;
		
		Map<String, String> outcome = new HashMap<>();
		
		try {
			datastore = DatastoreServiceFactory.getDatastoreService();
			txn = datastore.beginTransaction();
			
			if(StringUtils.isEmpty(baseName)){
				throw new CoffixException("empty baseName", "200");
			}
			
			if(StringUtils.isEmpty(userName)){
				throw new CoffixException("empty userName", "201");
			}
			
			if(StringUtils.isEmpty(password)){
				throw new CoffixException("empty password", "202");
			}
			
			if(StringUtils.isEmpty(confirmPassword)){
				throw new CoffixException("empty confirmPassword", "203");
			}
			
			if(!password.equals(confirmPassword)){
				throw new CoffixException("password and confirmPassword doesn't match", "300");
			}
			
			Key userKey = KeyFactory.createKey(Keys.User.KIND, Keys.User.NAME);
			
			Query query = new Query(Entities.User.KIND, userKey).setFilter(new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.EQUAL, userName));
			Entity newUser = datastore.prepare(txn, query).asSingleEntity();
			
			if(newUser != null){
				throw new CoffixException("an user with username #" + userName + "# already exist", "301");
			}
			
			newUser = new Entity(Entities.User.KIND, userKey);
			
			newUser.setProperty(Entities.User.Property.BASENAME, baseName);
			newUser.setProperty(Entities.User.Property.COFFEES, 0);
			newUser.setProperty(Entities.User.Property.USERNAME, userName);
			newUser.setProperty(Entities.User.Property.PASSWORD, password);
			
			datastore.put(txn, newUser);
			
			txn.commit();
			
			User loggedUser = new User();
			loggedUser.setBaseName(baseName);
			loggedUser.setUsername(userName);
			loggedUser.setEntityKey(KeyFactory.keyToString(newUser.getKey()));
			
			request.getSession().setAttribute(SessionKeys.USER, loggedUser);
			
			outcome.put("status", "OK");
			
		} catch(CoffixException ce){
			ce.printStackTrace();
			outcome.put("errorCode", ce.getErrorCode());
			outcome.put("status", "KO");
		} catch(Throwable t){
			t.printStackTrace();
			outcome.put("status", "KO");
			outcome.put("errorCode", "100");
		} finally {
			if(txn.isActive()){
				txn.rollback();
			}
		}
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().write(new Gson().toJson(outcome));
    }
}