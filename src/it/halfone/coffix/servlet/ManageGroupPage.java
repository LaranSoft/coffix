package it.halfone.coffix.servlet;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * ManageGroupPage - 17/nov/2013
 *
 * @author Andrea La Rosa
 */
public class ManageGroupPage extends HttpServlet {
	
	private static final long serialVersionUID = 8663805151922385604L;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute(SessionKeys.USER);
		
		String groupId = request.getParameter("groupId");
		
		request.setAttribute("groupId", groupId);
		request.setAttribute("user", user.getBaseName());
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
		Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
		
		Entity groupEntity = datastore.prepare(query).asSingleEntity();
		
		String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
		
		Gson reader = new Gson();
		
		Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
		Collection<PartecipatingGroupUser> partecipatingUserList = reader.fromJson(partecipatingUserListStringified, partecipatingUserListType);
		
		Collection<String> partecipatingDisplayNameList = new ArrayList<>();
		for(PartecipatingGroupUser partecipatingUser : partecipatingUserList){
			partecipatingDisplayNameList.add(partecipatingUser.getDisplayName());
		}
		
		String invitedUserMapStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.INVITED_USER_MAP)).getValue();
		Map<String, String> invitedUserMap = reader.fromJson(invitedUserMapStringified, HashMap.class);
		
		request.setAttribute("groupPartecipantList", partecipatingDisplayNameList);
		request.setAttribute("invitedUserMap", invitedUserMap);
		request.setAttribute("groupName", groupEntity.getProperty(Entities.Group.Property.NAME));
		
		String forwardPage = Views.manageGroup(request);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
		dispatcher.forward(request, response);
	}
}
