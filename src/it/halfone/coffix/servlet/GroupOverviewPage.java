package it.halfone.coffix.servlet;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Coffer;
import it.halfone.coffix.dao.PartecipatingGroupUser;
import it.halfone.coffix.dao.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
		String forwardPage = Views.groupOverview(request);
		request.setAttribute("groupId", groupId);
		request.setAttribute("user", user.getBaseName());
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		String indexParam = request.getParameter("i");
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
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardPage);
		dispatcher.forward(request, response);
	}
}
