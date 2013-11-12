package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.Gson;

/**
 * InviteUserServlet - 20/ott/2013
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
public class InviteUserServlet extends HttpServlet {

	private static final long serialVersionUID = -2809344872065238542L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		String username = req.getParameter("username");
		String groupId = req.getParameter("groupId");
		
		DatastoreService datastore = null;
		Transaction txn = null;
		String responseMessage = null;
		
		try {
			datastore = DatastoreServiceFactory.getDatastoreService();
			txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
			
			if(StringUtils.isEmpty(username)){
				throw new CoffixException("empty username", "200");
			}
			
			Key userKey = KeyFactory.createKey(Keys.User.KIND, Keys.User.NAME);
			
			Query query = new Query(Entities.User.KIND, userKey).setFilter(new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.EQUAL, username));
			Entity user = datastore.prepare(txn, query).asSingleEntity();
			
			if(user == null){
				throw new CoffixException("no user found", "201");
			}
			
			Collection<String> invitationList = (Collection<String>) user.getProperty(Entities.User.Property.INVITATION);
			if(invitationList == null){
				invitationList = new HashSet<>();
			}
			
			if(invitationList.contains(groupId)){
				throw new CoffixException("user already invited in group #" + groupId + "#", "202");
			}
			
			Collection<String> groupList = (Collection<String>) user.getProperty(Entities.User.Property.GROUP_LIST);
			if(groupList != null && groupList.contains(groupId)){
				throw new CoffixException("user already partecipating in group #" + groupId + "#", "203");
			}
			
			Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
			
			query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
			Entity groupEntity = datastore.prepare(txn, query).asSingleEntity();
			
			String invitedUserMapStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.INVITED_USER_MAP)).getValue();
			Map<String, String> invitedUserMap = new Gson().fromJson(invitedUserMapStringified, HashMap.class);
			
			invitedUserMap.put((String) user.getProperty(Entities.User.Property.USERNAME), (String) user.getProperty(Entities.User.Property.BASENAME));
			invitedUserMapStringified = new Gson().toJson(invitedUserMap);
			groupEntity.setProperty(Entities.Group.Property.INVITED_USER_MAP, new Text(invitedUserMapStringified));
			
			invitationList.add(groupId);
			user.setProperty(Entities.User.Property.INVITATION, invitationList);
			datastore.put(txn, user);
			datastore.put(txn, groupEntity);
			
			txn.commit();
			
			Map<String, String> invitatedUserDescription = new HashMap<>();
			invitatedUserDescription.put("username", (String) user.getProperty(Entities.User.Property.USERNAME));
			invitatedUserDescription.put("basename", (String) user.getProperty(Entities.User.Property.BASENAME));
			responseMessage = new Gson().toJson(invitatedUserDescription);
			
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