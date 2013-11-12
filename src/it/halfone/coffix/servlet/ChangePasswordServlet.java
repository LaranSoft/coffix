package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.CofferPartecipant;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * ChangePasswordServlet - 06/nov/2013
 * <br>
 * <h1>WServlet</h1>
 * <br>
 * Le WServlet si occupano di effettuare operazioni di scrittura e restituzione di un codice che rappresenti il risultato dell'operazione.
 * <br>
 * Accettano parametri in ingresso. Per questo motivo il client dovrebbe richiamarle sempre mediante il metodo $POST: non GET per evitare 
 * i parametri in query string e $ per evitare l'apparizione della dialog di conferma reinvio dati se si preme F5
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class ChangePasswordServlet extends HttpServlet {
	
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
		
		String responseMessage = "";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withDefaults());
			
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
				
		} catch(CoffixException ce){
			ce.printStackTrace();
			responseMessage = "error_" + ce.getErrorCode();
		} catch(Throwable t){
			responseMessage = "error_100";
			t.printStackTrace();
		} finally {
			if(txn.isActive()){
				txn.rollback();
			}
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(responseMessage);
	}

}
