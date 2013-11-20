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
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gson.Gson;

/**
 * ChangePasswordService - 18/nov/2013
 *
 * @author Andrea La Rosa
 */
public class ChangePasswordService extends HttpServlet {
	
	private static final long serialVersionUID = 926941681120918425L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		User loggedUser = (User) req.getSession().getAttribute(SessionKeys.USER);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withDefaults());
		
		Map<String, Object> outcome = new HashMap<>();
			
		try {
			String actualPassword = req.getParameter("a");
			if(StringUtils.isEmpty(actualPassword)){
				throw new CoffixException("no actual password specified", "200");
			}
			
			String newPassword = req.getParameter("n");
			if(StringUtils.isEmpty(newPassword)){
				throw new CoffixException("no new password specified", "201");
			}
			
			String confirmNewPassword = req.getParameter("cn");
			if(StringUtils.isEmpty(confirmNewPassword)){
				throw new CoffixException("no confirm new password specified", "202");
			}
				
			if(!confirmNewPassword.equals(newPassword)){
				throw new CoffixException("new password and confirm new password doesn't match", "203");
			}
			
			Key userKey = KeyFactory.createKey(Keys.User.KIND, Keys.User.NAME);
			
			Filter usernamefilter = new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.EQUAL, loggedUser.getUsername());
			Filter passwordfilter = new FilterPredicate(Entities.User.Property.PASSWORD, FilterOperator.EQUAL, actualPassword);
			
			Query query = new Query(Entities.User.KIND, userKey).setFilter(CompositeFilterOperator.and(usernamefilter, passwordfilter));
			Entity userEntity = datastore.prepare(query).asSingleEntity();
			
			if(userEntity == null){
				throw new CoffixException("actual password doesn't match", "204");
			}
			
			userEntity.setProperty(Entities.User.Property.PASSWORD, newPassword);
			
			datastore.put(txn, userEntity);			
				
			txn.commit();
			
			outcome.put("status", "OK");
				
		} catch(CoffixException ce){
			ce.printStackTrace();
			outcome.put("errorCode", ce.getErrorCode());
			outcome.put("status", "KO");
		} catch(Throwable t){
			outcome.put("errorCode", "100");
			outcome.put("status", "KO");
			t.printStackTrace();
		} finally {
			if(txn.isActive()){
				txn.rollback();
			}
		}
		
		resp.setCharacterEncoding("UTF-8");
	    resp.setContentType("text/plain");		
	    resp.getWriter().write(new Gson().toJson(outcome));
	}

}
