package it.halfone.coffix.servlet;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Coffer;
import it.halfone.coffix.dao.Context;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
 * GroupOverviewServlet - 21/ott/2013
 * <br>
 * <h1>2StepServlet</h1>
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class GroupOverviewServlet extends TwoStepServlet {
	
	private static final long serialVersionUID = 8750779382456897818L;
	
	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#firstStep(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    protected Context firstStep(HttpServletRequest req) {
    	Context ctx = new Context();
	    ctx.put("groupId", req.getParameter("groupId"));
	    ctx.put("i", req.getParameter("i"));
	    return ctx;
    }

	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#secondStep(javax.servlet.http.HttpServletRequest, it.halfone.coffix.dao.Context)
	 */
    @Override
    protected String secondStep(HttpServletRequest request, Context ctx) {
    	User user = (User) request.getSession().getAttribute(SessionKeys.USER);
    	
		String groupId = ctx.get("groupId");
		String forwardPage = null;
		
		forwardPage = Views.groupOverview(request);
		request.setAttribute("groupId", groupId);
		request.setAttribute("user", user.getBaseName());
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		String indexParam = ctx.get("i");
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
			
			request.setAttribute("nextOfferer", partecipatingUserListToSort.get(index-1).getDisplayName());
			request.setAttribute("index", index+1);
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
		request.setAttribute("coffers", cofferList);
		request.setAttribute("groupName", groupEntity.getProperty(Entities.Group.Property.NAME));

		request.setAttribute("now", new Date().getTime());
		
		return forwardPage;
    }
}
