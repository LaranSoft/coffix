package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gson.Gson;

/**
 * CreateGroupService - 18/nov/2013
 *
 * @author Andrea La Rosa
 */
public class CreateGroupService extends HttpServlet {

	private static final long serialVersionUID = 2557683064644200641L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(SessionKeys.USER);
		
		DatastoreService datastore = null;
		Transaction txn = null;
		
		Map<String, Object> outcome = new HashMap<>();
		
		try {
			datastore = DatastoreServiceFactory.getDatastoreService();
			txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
			
			String groupName = req.getParameter("n");
			
			if(StringUtils.isEmpty(groupName)){
				throw new CoffixException("param #n# missing", "200");
			}
			
			if(groupName.length() < 4 || groupName.length() > 30){
				throw new CoffixException("group name must be between 4 and 30 characters. GroupName #" + groupName + "#", "201");
			}
			
			
			String groupUUID = UUID.randomUUID().toString();
			String userKey = user.getEntityKey();
			
			Entity userEntity = datastore.get(txn, KeyFactory.stringToKey(userKey));
			Collection<String> groupList = (Collection<String>) userEntity.getProperty(Entities.User.Property.GROUP_LIST);
			if(groupList == null){
				groupList = new ArrayList<>();
			}
			groupList.add(groupUUID);
			userEntity.setProperty(Entities.User.Property.GROUP_LIST, groupList);
			
			Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
			
			Collection<PartecipatingGroupUser> partecipatingUserList = new ArrayList<>();
			partecipatingUserList.add(new PartecipatingGroupUser(user.getUsername(), user.getBaseName()));
			
			Gson writer = new Gson();
			
			String partecipatingUserListStringified = writer.toJson(partecipatingUserList);
			
			String invitedUserMapStringified = writer.toJson(new HashMap<>());
			
			Entity newGroupEntity = new Entity(Entities.Group.KIND, groupKey);
			newGroupEntity.setProperty(Entities.Group.Property.CREATOR, userKey);
			newGroupEntity.setProperty(Entities.Group.Property.NAME, groupName);
			newGroupEntity.setProperty(Entities.Group.Property.UUID, groupUUID);
			newGroupEntity.setProperty(Entities.Group.Property.PARTECIPATING_USERS, new Text(partecipatingUserListStringified));
			newGroupEntity.setProperty(Entities.Group.Property.INVITED_USER_MAP, new Text(invitedUserMapStringified));
			
			datastore.put(txn, newGroupEntity);
			datastore.put(txn, userEntity);
			
			txn.commit();
			
			Map<String, String> groupMap = new HashMap<>();
			groupMap.put(groupName, groupUUID);
			
			outcome.put("status", "OK");
			outcome.put("data", groupMap);
			
		} catch(CoffixException ce){
			ce.printStackTrace();
			outcome.put("errorCode", ce.getErrorCode());
			outcome.put("status", "KO");
		} catch(Throwable t){
			t.printStackTrace();
			outcome.put("errorCode", "100");
			outcome.put("status", "KO");
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
