package it.halfone.coffix.servlet;
import it.halfone.coffix.configuration.Configuration;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.Paths;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.Coffer;
import it.halfone.coffix.dao.PageDescription;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * GroupOverviewPage - 17/nov/2013
 *
 * @author Andrea La Rosa
 */
public class GroupOverviewPage extends HttpServlet {
	
	private static final long serialVersionUID = 1877555854786610617L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PageDescription pd = new PageDescription();
		pd.getCssFiles().add(Paths.COMMON_CSS + "/groupOverview/groupOverview.css");
		pd.getCssFiles().add(Paths.CSS + "/" + req.getAttribute("userAgentMainType") + "/groupOverview/groupOverview.css");
		
		pd.getJsFiles().add(Paths.JS + "/groupOverview/groupOverview.js");
		pd.getJsFiles().add(Paths.JS + "/" + req.getAttribute("userAgentMainType") + "/groupOverview/groupOverview.js");
		
		Map<String, String> bundles = new HashMap<>();
		bundles.put("noUserPresent", Configuration.getInstance().get("INDEX.NO_USER_PRESENT"));
		bundles.put("addUserToGroup", Configuration.getInstance().get("INDEX.ADD_USERS_TO_GROUP"));
		bundles.put("todayIs", Configuration.getInstance().get("INDEX.TODAY_IS"));
		bundles.put("otherwise", Configuration.getInstance().get("COMMON.QUESTION.OTHERWISE"));
		bundles.put("negate", Configuration.getInstance().get("COMMON.NEGATE"));
		bundles.put("confirm", Configuration.getInstance().get("INDEX.CANCEL_NEGATE"));
		bundles.put("createdBy", Configuration.getInstance().get("COMMON.CREATED_BY"));
		bundles.put("registerCoffer", Configuration.getInstance().get("INDEX.REGISTER_COFFER"));
		bundles.put("erase", Configuration.getInstance().get("COMMON.ERASE"));
		bundles.put("groupDetailsLink", Configuration.getInstance().get("GROUP_OVERVIEW.GROUP_DETAILS_LINK." + req.getAttribute("userAgentMainType")));
		pd.getData().put("bundles", bundles);
		
		User user = (User) req.getSession().getAttribute(SessionKeys.USER);
    	
		String groupId = req.getParameter("groupId");
		
		pd.getData().put("groupId", groupId);
		pd.getData().put("user", user.getBaseName());
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		String indexParam = req.getParameter("i");
		int index = 1;
		if(!StringUtils.isEmpty(indexParam)){
			index = Integer.parseInt(indexParam);
		}
		if(index < 1){
			index = 1;
		}
		
		Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
		Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
		
		Entity groupEntity = datastore.prepare(query).asSingleEntity();
		
		String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
		Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
		
		Collection<PartecipatingGroupUser> partecipatingUserList = new Gson().fromJson(partecipatingUserListStringified, partecipatingUserListType);
		List<PartecipatingGroupUser> partecipatingUserListToSort = new ArrayList<>(partecipatingUserList);
		
		if(partecipatingUserList.size() > 1){
			Collections.sort(partecipatingUserListToSort);
			
			if(index > partecipatingUserListToSort.size()){
				index = 1;
			}
			
			pd.getData().put("nextOfferer", partecipatingUserListToSort.get(index-1).getDisplayName());
			pd.getData().put("index", index+1);
		}
		
		Key cofferKey = KeyFactory.createKey(Keys.Coffer.KIND, Keys.Coffer.NAME);
		query = new Query(Entities.Coffer.KIND, cofferKey).setFilter(new FilterPredicate(Entities.Coffer.Property.GROUP, FilterOperator.EQUAL, groupId)).addSort(Entities.Coffer.Property.EXPIRATION_TIME, SortDirection.ASCENDING);
		List<Entity> coffeeEntityList = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		
		List<Coffer> cofferList = new ArrayList<Coffer>();
		for(Entity coffeeEntity : coffeeEntityList){
			Coffer coffer = new Coffer();
			coffer.setInitiator((String) coffeeEntity.getProperty(Entities.Coffer.Property.INITIATOR));
			coffer.setOfferer((String) coffeeEntity.getProperty(Entities.Coffer.Property.OFFERER));
			coffer.setOffereds((String) coffeeEntity.getProperty(Entities.Coffer.Property.OFFEREDS));
			coffer.setExpireTime((long) coffeeEntity.getProperty(Entities.Coffer.Property.EXPIRATION_TIME));
			coffer.setState((long) coffeeEntity.getProperty(Entities.Coffer.Property.STATUS));
			
			coffer.setKey(KeyFactory.keyToString(coffeeEntity.getKey()));
			
			cofferList.add(coffer);
		}
		pd.getData().put("coffers", cofferList);
		pd.getData().put("groupName", groupEntity.getProperty(Entities.Group.Property.NAME));

		pd.getData().put("now", new Date().getTime());
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(pd));
	}
}
