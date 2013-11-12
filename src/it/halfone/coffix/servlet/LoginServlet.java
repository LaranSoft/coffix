package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.Servlets;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Context;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import javax.servlet.http.HttpServletRequest;

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

/**
 * LoginServlet - 20/ott/2013
 * <br>
 * Il funzionamento della Servlet non ricade in nessuno dei tre casi RServlet, 2StepRServlet o WServlet.
 * <br>
 * Ci sono tre modi diversi per arrivare alla servlet di Login:
 * <ul>
 * <li>Il client effettua una GET puntando direttamente alla servlet, senza alcun parametro. In questo caso la Servlet restituisce 
 * la pagina login.jsp con variabile fwd che punta alla Home Servlet</li>
 * <li>Il client effettua una $POST puntando direttamente alla servlet, passando i parametri username e password. In questo caso
 * viene tentata l'autenticazione, e se questa ha esito positivo allora il client farà una richiesta in GET alla pagina specificata dal
 * parametro fwd.</li>
 * <li>Il client effettua una chiamata di qualunque tipo ad una qualsiasi altra servlet (con N parametri, N >= 0). In questo caso, se il 
 * filtro apposito non riconosce l'avvenuta autenticazione dell'utente, redirigerà questo alla pagina login.jsp con parametro fwd
 * valorizzato con la chiamata originale.</li>
 * </ul>
 *
 * @author Andrea La Rosa
 */
public class LoginServlet extends TwoStepServlet {

	private static final long serialVersionUID = -8574616948128217904L;

	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#firstStep(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    protected Context firstStep(HttpServletRequest req) {
    	Context ctx = new Context();
    	ctx.put("u", req.getParameter("u"));
    	ctx.put("p", req.getParameter("p"));
    	ctx.put("fwd", req.getParameter("fwd"));
	    return ctx;
    }

	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#secondStep(javax.servlet.http.HttpServletRequest, it.halfone.coffix.dao.Context)
	 */
    @Override
    protected String secondStep(HttpServletRequest request, Context ctx) {
    	String username = ctx.get("u");
		String password = ctx.get("p");
		
		String forwardPage = Views.login(request);
		
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
			
			forwardPage = ctx.get("fwd");
			if(StringUtils.isEmpty(forwardPage)){
				forwardPage = Servlets.HOME_SERVLET;
			}
			
		} catch(CoffixException ce){
			ce.printStackTrace();
			forwardPage = Views.login(request);
			request.setAttribute("errorCode", ce.getErrorCode());
			request.setAttribute("fwd", ctx.get("fwd"));
		} catch(Throwable t){
			t.printStackTrace();
			forwardPage = Views.login(request);
			request.setAttribute("errorCode", "100");
			request.setAttribute("fwd", ctx.get("fwd"));
		}
		
		return forwardPage;
    }
}