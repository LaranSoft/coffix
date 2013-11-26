package it.halfone.coffix.servlet;
import it.halfone.coffix.configuration.Configuration;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.Paths;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.PageDescription;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PageDescription pd = new PageDescription();
		pd.getCssFiles().add(Paths.COMMON_CSS + "/manageGroup/manageGroup.css");
		pd.getCssFiles().add(Paths.CSS + "/" + req.getAttribute("userAgentMainType") + "/manageGroup/manageGroup.css");
		
		pd.getJsFiles().add(Paths.JS + "/manageGroup/manageGroup.js");
		
		Map<String, String> bundles = new HashMap<>();
		bundles.put("group", Configuration.getInstance().get("COMMON.GROUP"));
		bundles.put("insertUsernameLabel", Configuration.getInstance().get("MANAGE_GROUP.INSERT_USERNAME.LABEL"));
		bundles.put("partecipantsLabel", Configuration.getInstance().get("MANAGE_GROUP.PARTECIPANTS.LABEL"));
		bundles.put("invitedWaitConfirm", Configuration.getInstance().get("MANAGE_GROUP.INVITED_WAIT_CONFIRM"));
		bundles.put("cancel", Configuration.getInstance().get("COMMON.CANCEL"));
		
		pd.getData().put("bundles", bundles);
		
		User user = (User) req.getSession().getAttribute(SessionKeys.USER);
		
		String groupId = req.getParameter("groupId");
		
		pd.getData().put("groupId", groupId);
		pd.getData().put("user", user.getBaseName());
		
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
		
		pd.getData().put("groupPartecipantList", partecipatingDisplayNameList);
		pd.getData().put("invitedUserMap", invitedUserMap);
		pd.getData().put("groupName", groupEntity.getProperty(Entities.Group.Property.NAME));

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(pd));
		
	}
}
