package it.halfone.coffix.servlet;

import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.constants.Views;
import it.halfone.coffix.dao.Context;
import it.halfone.coffix.dao.PartecipatingGroupUser;

import java.lang.reflect.Type;
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
 * ChooseOfferedsServlet - 21/ott/2013
 * <br>
 * <h1>2StepServlet</h1>
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class ChooseOfferedsServlet extends TwoStepServlet {
	
	private static final long serialVersionUID = -5955485146846342177L;
	
	
	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#firstStep(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    protected Context firstStep(HttpServletRequest req) {
	    Context ctx = new Context();
	    ctx.put("groupId", req.getParameter("groupId"));
	    ctx.put("offerer", req.getParameter("offerer"));
	    return ctx;
    }

	/* (non-Javadoc)
	 * @see it.halfone.coffix.servlet.TwoStepServlet#secondStep(javax.servlet.http.HttpServletRequest, it.halfone.coffix.dao.Context)
	 */
    @Override
    protected String secondStep(HttpServletRequest request, Context ctx) {
    	String forwardPage = Views.chooseOffereds(request);
		String groupId = ctx.get("groupId");
		String offerer = ctx.get("offerer");
		
		request.setAttribute("groupId", groupId);
		request.setAttribute("user", request.getSession().getAttribute(SessionKeys.USER));
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
		Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
		
		Entity groupEntity = datastore.prepare(query).asSingleEntity();
		
		String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
		
		Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
		Collection<PartecipatingGroupUser> partecipatingUserList = new Gson().fromJson(partecipatingUserListStringified, partecipatingUserListType);
		
		Map<String, String> partecipatingUserMap = new HashMap<>();
		for(PartecipatingGroupUser partecipatingUser : partecipatingUserList){
			partecipatingUserMap.put(partecipatingUser.getUsername(), partecipatingUser.getDisplayName());
		}
		
		String offererBasename = partecipatingUserMap.remove(offerer);
		request.setAttribute("offerer", offererBasename);
		request.setAttribute("offererUsername", offerer);
		
		request.setAttribute("partecipatingUserMap", partecipatingUserMap);

		return forwardPage;
    }
}