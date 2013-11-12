package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.CofferPartecipant;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

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
 * ConfirmCofferServlet - 22/ott/2013
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
public class ConfirmCofferServlet extends HttpServlet {

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
		
		String responseMessage = "";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
		
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
			
		} catch(CoffixException ce){
			ce.printStackTrace();
			responseMessage = "error_" + ce.getErrorCode();
		} catch(Throwable t){
			t.printStackTrace();
			responseMessage = "error_100";
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
