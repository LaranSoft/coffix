package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.lang.reflect.Type;
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
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * ManageInvitationService - 18/nov/2013
 *
 * @author Andrea La Rosa
 */
public class ManageInvitationService extends HttpServlet {

	private static final long serialVersionUID = -3842031949717287808L;

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
		String action = req.getParameter("action");
		String groupId = req.getParameter("groupId");
		String username = req.getParameter("username");
		
		DatastoreService datastore = null;
		Transaction txn = null;
		
		Map<String, String> outcome = new HashMap<>();
		
		try {
			datastore = DatastoreServiceFactory.getDatastoreService();
			txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
			
			if(StringUtils.isEmpty(action)){
				throw new CoffixException("empty action", "200");
			}
			
			if(!"1".equals(action) && !"0".equals(action)){
				throw new CoffixException("invalid action", "201");
			}
			
			Key loggedUserEntityKey = KeyFactory.stringToKey(loggedUser.getEntityKey());
			Entity loggedUserEntity = datastore.get(loggedUserEntityKey);
			
			Entity receivingActionEntity = loggedUserEntity;
			
			Collection<String> invitationList = null;
			Collection<String> groupList = null;
			
			if(!StringUtils.isEmpty(username)){
				invitationList = (Collection<String>) loggedUserEntity.getProperty(Entities.User.Property.INVITATION);
				groupList = (Collection<String>) loggedUserEntity.getProperty(Entities.User.Property.GROUP_LIST);
				
				if((invitationList == null || !invitationList.contains(groupId)) && (groupList == null || !groupList.contains(groupId))){
					throw new CoffixException("invalid request: user #" + loggedUser.getUsername() + "# can't operate on group #" + groupId + "#", "202");
				}
				
				Key userKey = KeyFactory.createKey(Keys.User.KIND, Keys.User.NAME);
				
				Query query = new Query(Entities.User.KIND, userKey).setFilter(new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.EQUAL, username));
				receivingActionEntity = datastore.prepare(txn, query).asSingleEntity();
			} 
			
			if(receivingActionEntity == null){
				throw new CoffixException("user with username #" + username + "# not found", "203");
			}
			
			invitationList = (Collection<String>) receivingActionEntity.getProperty(Entities.User.Property.INVITATION);
			if(invitationList == null || !invitationList.contains(groupId)){
				throw new CoffixException("user #" + receivingActionEntity.getProperty(Entities.User.Property.USERNAME) + " is not invited in group #" + groupId + "#", "205");
			}
			
			Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
			
			Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
			Entity groupEntity = datastore.prepare(txn, query).asSingleEntity();
			
			if(groupEntity == null){
				throw new CoffixException("unexisting group #" + groupId + "#", "204");
			}
			
			invitationList.remove(groupId);
			receivingActionEntity.setProperty(Entities.User.Property.INVITATION, invitationList);
			
			String invitedUserMapStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.INVITED_USER_MAP)).getValue();
			Map<String, String> invitedUserMap = new Gson().fromJson(invitedUserMapStringified, HashMap.class);
			
			invitedUserMap.remove((String) receivingActionEntity.getProperty(Entities.User.Property.USERNAME));
			invitedUserMapStringified = new Gson().toJson(invitedUserMap);
			groupEntity.setProperty(Entities.Group.Property.INVITED_USER_MAP, new Text(invitedUserMapStringified));
			
			if("1".equals(action)){
				groupList = (Collection<String>) receivingActionEntity.getProperty(Entities.User.Property.GROUP_LIST);
				if(groupList == null){
					groupList = new HashSet<>();
				}
				groupList.add(groupId);
				receivingActionEntity.setProperty(Entities.User.Property.GROUP_LIST, groupList);
				
				String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
				
				Gson rw = new Gson(); 
				
				Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
				Collection<PartecipatingGroupUser> partecipatingUserList = rw.fromJson(partecipatingUserListStringified, partecipatingUserListType);
				
				partecipatingUserList.add(new PartecipatingGroupUser((String) receivingActionEntity.getProperty(Entities.User.Property.USERNAME), (String) receivingActionEntity.getProperty(Entities.User.Property.BASENAME)));
				partecipatingUserListStringified = rw.toJson(partecipatingUserList);
				groupEntity.setProperty(Entities.Group.Property.PARTECIPATING_USERS, new Text(partecipatingUserListStringified));
			}
			
			datastore.put(txn, receivingActionEntity);
			datastore.put(txn, groupEntity);
			
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
