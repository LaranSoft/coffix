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
 * InviteUserService - 21/nov/2013
 *
 * @author Andrea La Rosa
 */
public class InviteUserService extends HttpServlet {

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

		Map<String, Object> outcome = new HashMap<>();
		
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
			
			outcome.put("status", "OK");
			outcome.put("data", invitatedUserDescription);
			
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