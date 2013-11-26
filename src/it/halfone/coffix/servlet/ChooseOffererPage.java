package it.halfone.coffix.servlet;

import it.halfone.coffix.configuration.Configuration;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.Paths;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.PageDescription;
import it.halfone.coffix.dao.PartecipatingGroupUser;

import java.io.IOException;
import java.lang.reflect.Type;
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
 * ChooseOffererPage - 17/nov/2013
 *
 * @author Andrea La Rosa
 */
public class ChooseOffererPage extends HttpServlet {
	
	private static final long serialVersionUID = -1294340341696466413L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PageDescription pd = new PageDescription();
		pd.getCssFiles().add(Paths.COMMON_CSS + "/chooseOfferer/chooseOfferer.css");
		pd.getCssFiles().add(Paths.CSS + "/" + req.getAttribute("userAgentMainType") + "/chooseOfferer/chooseOfferer.css");
		
		pd.getJsFiles().add(Paths.JS + "/chooseOfferer/chooseOfferer.js");
		
		Map<String, String> bundles = new HashMap<>();
		bundles.put("selectOffererLabel", Configuration.getInstance().get("CHOOSE_OFFERER.SELECT_OFFERER.LABEL"));
		bundles.put("selectOffererPlaceholder", Configuration.getInstance().get("CHOOSE_OFFERER.SELECT_OFFERER.PLACEHOLDER"));
		pd.getData().put("bundles", bundles);
		
    	String groupId = req.getParameter("groupId");
		
    	pd.getData().put("groupId", groupId);
    	pd.getData().put("user", req.getSession().getAttribute(SessionKeys.USER));
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
		Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
		
		Entity groupEntity = datastore.prepare(query).asSingleEntity();
		
		String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
		Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
		
		Collection<PartecipatingGroupUser> partecipatingUserList = new Gson().fromJson(partecipatingUserListStringified, partecipatingUserListType);
		
		Map<String, String> partecipatingUserMap = new HashMap<>();
		for(PartecipatingGroupUser partecipatingUser : partecipatingUserList){
			partecipatingUserMap.put(partecipatingUser.getDisplayName(), partecipatingUser.getUsername());
		}
		
		pd.getData().put("partecipatingUserMap", partecipatingUserMap);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(pd));
    }
}
