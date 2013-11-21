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
 * RegisterCofferService - 21/nov/2013
 *
 * @author Andrea La Rosa
 */
public class RegisterCofferService extends HttpServlet {
	
	private static final long serialVersionUID = 3443023373860791017L;
	
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
		
		String groupId = req.getParameter("groupId");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
			
		Map<String, String> outcome = new HashMap<>();
		
		try {
			String key = req.getParameter("k");
			if(StringUtils.isEmpty(key)){
				throw new CoffixException("no coffer key specified", "200");
			}
			
			String action = req.getParameter("a");
			if(StringUtils.isEmpty(action)){
				throw new CoffixException("no action specified", "201");
			}
				
			if(!"1".equals(action) && !"0".equals(action)){
				throw new CoffixException("invalid action specified #" + action + "#", "202");
			}
				
			Key cofferKey = KeyFactory.stringToKey(key);
				
			Entity coffer = datastore.get(txn, cofferKey);
			if(coffer == null){
				throw new CoffixException("no coffer with key #" + key + "#", "203");
			}
			
			if(!((String) coffer.getProperty(Entities.Coffer.Property.GROUP)).equals(groupId)){
				throw new CoffixException("coffer with key #" + key + "# is not associated with group #" + groupId + "#", "204");
			}
			
			Key loggedUserEntityKey = KeyFactory.stringToKey(loggedUser.getEntityKey());
			Entity loggedUserEntity = datastore.get(loggedUserEntityKey);
			
			Collection<String> groupList = (Collection<String>) loggedUserEntity.getProperty(Entities.User.Property.GROUP_LIST);
			
			if(groupList == null || !groupList.contains(groupId)){
				throw new CoffixException("invalid request: user #" + loggedUser.getUsername() + "# can't operate on group #" + groupId + "#", "205");
			}
			
			Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
			
			Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
			Entity groupEntity = datastore.prepare(txn, query).asSingleEntity();
			
			if(groupEntity == null){
				throw new CoffixException("unexisting group #" + groupId + "#", "206");
			}
			
			long state = (long) coffer.getProperty(Entities.Coffer.Property.STATUS);
			long expireTime = (long) coffer.getProperty(Entities.Coffer.Property.EXPIRATION_TIME);
			long now = Calendar.getInstance().getTimeInMillis();
			
			boolean hasExpired = expireTime <= now;
				
			if(!hasExpired){
				throw new CoffixException("can't operate on a non-expired coffer", "205");
			}
			
			if("0".equals(action) && state != -1){
				throw new CoffixException("can't cancel a valid coffer", "206");
			}
				
			if("1".equals(action) && state == -1){
				throw new CoffixException("can't register an invalid coffer", "207");
			}
			
			if("1".equals(action)){
				Gson reader = new Gson();
				
				Type offeredsType = new TypeToken<Collection<CofferPartecipant>>(){}.getType();
				Collection<CofferPartecipant> offereds =  reader.fromJson((String) coffer.getProperty(Entities.Coffer.Property.OFFEREDS), offeredsType);
				Collection<String> offeredsUsernameSet = new HashSet<>();
				
				for(CofferPartecipant offered : offereds){
					offeredsUsernameSet.add(offered.getUsername());
				}
				CofferPartecipant offerer = reader.fromJson((String) coffer.getProperty(Entities.Coffer.Property.OFFERER), CofferPartecipant.class);
				String offererUsername = offerer.getUsername();
				int count = 0;
				
				String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
				Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
				Collection<PartecipatingGroupUser> partecipatingUserList = reader.fromJson(partecipatingUserListStringified, partecipatingUserListType);
					
				for(PartecipatingGroupUser partecipatingUser : partecipatingUserList){
					if(offeredsUsernameSet.contains(partecipatingUser.getUsername())){
						long coffees = partecipatingUser.getCoffees();
						partecipatingUser.setCoffees(coffees-1);
						count++;
					} else if(offererUsername.equals(partecipatingUser.getUsername())){
						long coffees = partecipatingUser.getCoffees();
						partecipatingUser.setCoffees(coffees + offeredsUsernameSet.size());
					}
				}
				
				if(count != offeredsUsernameSet.size()){
					throw new CoffixException("not sum 0 operation on coffee matrix", "208");
				}
				
				partecipatingUserListStringified = reader.toJson(partecipatingUserList);
				groupEntity.setProperty(Entities.Group.Property.PARTECIPATING_USERS, new Text(partecipatingUserListStringified));
				
				datastore.put(txn, groupEntity);
			}
			datastore.delete(txn, cofferKey);
				
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
