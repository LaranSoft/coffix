package it.halfone.coffix.servlet;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Context;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
 * ManageGroupServlet - 21/ott/2013
 * <br>
 * <h1>2StepServlet</h1>
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class ManageGroupServlet extends TwoStepServlet {
	
	private static final long serialVersionUID = 8750779382456897818L;
	
	@Override
	protected Context firstStep(HttpServletRequest req) {
		Context ctx = new Context();
		ctx.put("groupId", req.getParameter("groupId"));
		return ctx;
	}
	
	@Override
	protected String secondStep(HttpServletRequest request, Context ctx) {
		User user = (User) request.getSession().getAttribute(SessionKeys.USER);
		
		String groupId = ctx.get("groupId");
		
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
		
		return Views.manageGroup(request);
	}
}
