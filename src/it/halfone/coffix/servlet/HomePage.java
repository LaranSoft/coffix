package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * HomePage - 16/nov/2013
 *
 * @author Andrea La Rosa
 */
public class HomePage extends HttpServlet{

	private static final long serialVersionUID = 4228452589834800392L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(SessionKeys.USER);
		try {
			if(user != null) {
				req.setAttribute("user", user.getBaseName());
				
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Key loggedUserEntityKey = KeyFactory.stringToKey(user.getEntityKey());
				Entity loggedUserEntity = datastore.get(loggedUserEntityKey);
				
				Collection<String> groupListProperty = (Collection<String>) loggedUserEntity.getProperty(Entities.User.Property.GROUP_LIST);
				if(groupListProperty != null) {
					Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
					
					Query query = new Query(Entities.Group.KIND, groupKey);
					query.setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.IN, groupListProperty));
					
					List<Entity> groupEntityList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
					
					Map<String, String> groupMap = new HashMap<>();
					for(Entity groupEntity : groupEntityList) {
						groupMap.put((String) groupEntity.getProperty(Entities.Group.Property.NAME), (String) groupEntity.getProperty(Entities.Group.Property.UUID));
					}
					
					req.setAttribute("groupMap", groupMap);
				}
				
				Collection<String> pendingInviteListProperty = (Collection<String>) loggedUserEntity.getProperty(Entities.User.Property.INVITATION);
				if(pendingInviteListProperty != null) {
					Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
					
					Query query = new Query(Entities.Group.KIND, groupKey);
					query.setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.IN, pendingInviteListProperty));
					
					List<Entity> groupEntityList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
					
					Map<String, String> groupMap = new HashMap<>();
					for(Entity groupEntity : groupEntityList) {
						groupMap.put((String) groupEntity.getProperty(Entities.Group.Property.NAME), (String) groupEntity.getProperty(Entities.Group.Property.UUID));
					}
					
					req.setAttribute("invitationMap", groupMap);
				}
			}
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Views.home(req));
			dispatcher.forward(req, resp);
		} catch(EntityNotFoundException e) {
			e.printStackTrace();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
