package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.Servlets;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Context;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;
import it.halfone.coffix.exception.CoffixWarning;

import javax.servlet.http.HttpServletRequest;

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

/**
 * RegistrationServlet - 21/ott/2013
 * <br>
 * <h1>2StepServlet</h1>
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class RegistrationServlet extends TwoStepServlet {

	private static final long serialVersionUID = -6335944751669832845L;

	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#firstStep(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    protected Context firstStep(HttpServletRequest req) {
	    Context ctx = new Context();
	    ctx.put("u", req.getParameter("u"));
	    ctx.put("d", req.getParameter("d"));
	    ctx.put("p", req.getParameter("p"));
	    ctx.put("cp", req.getParameter("cp"));
	    return ctx;
    }

	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#secondStep(javax.servlet.http.HttpServletRequest, it.halfone.coffix.dao.Context)
	 */
    @Override
    protected String secondStep(HttpServletRequest request, Context ctx) {
    	String baseName = ctx.get("d");
		String userName = ctx.get("u");
		String password = ctx.get("p");
		String confirmPassword = ctx.get("cp");
		
		String forwardPage = Servlets.HOME_SERVLET;
		DatastoreService datastore = null;
		Transaction txn = null;
		
		try {
			datastore = DatastoreServiceFactory.getDatastoreService();
			txn = datastore.beginTransaction();
			
			if(StringUtils.isEmpty(baseName) && StringUtils.isEmpty(userName) && StringUtils.isEmpty(password) && StringUtils.isEmpty(confirmPassword)){
				throw new CoffixWarning("no parameters found");
			}
			
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
			
		} catch(CoffixWarning cw){
			forwardPage = Views.registration(request);
		} catch(CoffixException ce){
			ce.printStackTrace();
			request.setAttribute("errorCode", ce.getErrorCode());
			forwardPage = Views.registration(request);
		} catch(Throwable t){
			t.printStackTrace();
			forwardPage = Views.registration(request);
			request.setAttribute("errorCode", "100");
		} finally {
			if(txn.isActive()){
				txn.rollback();
			}
		}
		
		return forwardPage;
    }
}
