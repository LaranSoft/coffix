package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.CofferPartecipant;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
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
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * ConfirmCofferService - 20/nov/2013
 *
 * @author Andrea La Rosa
 */
public class ConfirmCofferService extends HttpServlet {

	private static final long serialVersionUID = -2094701910751764910L;

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
		Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
		
		Map<String, String> outcome = new HashMap<>();
		
		try {
			String action = req.getParameter("a");
			if(StringUtils.isEmpty(action)){
				throw new CoffixException("no action specified", "200");
			}
			
			String[] actionComponents = action.split(";");
			if(actionComponents.length != 3){
				throw new CoffixException("invalid action components number", "201");
			}
			
			String cofferKeyEncoded = actionComponents[0];
			String requestingUsername = actionComponents[1];
			String confirmAction = actionComponents[2];
			
			if(!"1".equals(confirmAction) && !"0".equals(confirmAction)){
				throw new CoffixException("invalid value for parameter confirmAction: #" + confirmAction + "#", "202");
			}
			
			if(!requestingUsername.equals(loggedUser.getUsername())){
				throw new CoffixException(loggedUser.getUsername() + " can't confirm or negate for coffered " + requestingUsername, "203");
			}
			
			Key cofferKey = KeyFactory.stringToKey(cofferKeyEncoded);
			
			Entity coffer = datastore.get(txn, cofferKey);
			if(coffer == null){
				throw new CoffixException("no coffer with key #" + cofferKeyEncoded + "#", "204");
			}
			
			Gson reader = new Gson();
			boolean requestingUserIsPartecipating = false;
			boolean atLeastOneNegation = false;
			boolean negate = false;
			
			CofferPartecipant offerer = reader.fromJson((String) coffer.getProperty(Entities.Coffer.Property.OFFERER), CofferPartecipant.class);
			
			if(offerer.getUsername().equals(requestingUsername)){
				requestingUserIsPartecipating = true;
				if("1".equals(confirmAction)){ // annulla la negazione
					offerer.setHasNegate(false);
					negate = false;
				} else if("0".equals(confirmAction)){ // nega
					offerer.setHasNegate(true);
					negate = true;
				}
			}
			coffer.setProperty(Entities.Coffer.Property.OFFERER, reader.toJson(offerer));
			
			atLeastOneNegation = atLeastOneNegation || negate;
			
			Type collectionType = new TypeToken<Collection<CofferPartecipant>>(){}.getType();
			Collection<CofferPartecipant> offereds = reader.fromJson((String) coffer.getProperty(Entities.Coffer.Property.OFFEREDS), collectionType);
			
			for(CofferPartecipant offered : offereds){
				negate = offered.hasNegate();
				if(offered.getUsername().equals(requestingUsername)){
					requestingUserIsPartecipating = true;
					
					if("1".equals(confirmAction)){ // annulla la negazione
						offered.setHasNegate(false);
						negate = false;
					} else if("0".equals(confirmAction)){ // nega
						offered.setHasNegate(true);
						negate = true;
					}
				}
				
				atLeastOneNegation = atLeastOneNegation || negate;
			}
			
			coffer.setProperty(Entities.Coffer.Property.STATUS, atLeastOneNegation ? -1 : 0);
			
			if(!requestingUserIsPartecipating){
				throw new CoffixException("requesting user: #" + requestingUsername + "# isn't in this coffer", "205");
			}
			
			coffer.setProperty(Entities.Coffer.Property.OFFEREDS, reader.toJson(offereds));
			datastore.put(txn, coffer);
			
			txn.commit();
			
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
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(outcome));
	}
}
