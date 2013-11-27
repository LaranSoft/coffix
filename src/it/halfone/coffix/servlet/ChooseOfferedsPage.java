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
 * ChooseOfferedsPage - 17/nov/2013
 *
 * @author Andrea La Rosa
 */
public class ChooseOfferedsPage extends HttpServlet {
	
	private static final long serialVersionUID = 2431984727577270969L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PageDescription pd = new PageDescription();
		pd.getCssFiles().add(Paths.COMMON_CSS + "/chooseOffereds/chooseOffereds.css");
		pd.getCssFiles().add(Paths.CSS + "/" + req.getAttribute("userAgentMainType") + "/chooseOffereds/chooseOffereds.css");
		
		pd.getJsFiles().add(Paths.JS + "/chooseOffereds/chooseOffereds.js");
		
		Map<String, String> bundles = new HashMap<>();
		bundles.put("selectOfferedLabel", Configuration.getInstance().get("CHOOSE_OFFEREDS.SELECT_OFFERED.LABEL"));
		bundles.put("selectOfferedPlaceholder", Configuration.getInstance().get("CHOOSE_OFFEREDS.SELECT_OFFERED.PLACEHOLDER"));
		bundles.put("summarize", Configuration.getInstance().get("COMMON.SUMMARIZE"));
		bundles.put("change", Configuration.getInstance().get("COMMON.CHANGE"));
		bundles.put("whoIsOfferer", Configuration.getInstance().get("CHOOSE_OFFEREDS.WHOIS_OFFERER.LABEL"));
		bundles.put("createCofferConfirmButtonLabel", Configuration.getInstance().get("CHOOSE_OFFEREDS.CREATE_COFFER.CONFIRM.BUTTON_LABEL"));
		bundles.put("remove", Configuration.getInstance().get("COMMON.REMOVE"));
		pd.getData().put("bundles", bundles);
		
		String groupId = req.getParameter("groupId");
		String offerer = req.getParameter("offerer");
		
		pd.getData().put("groupId", groupId);
		pd.getData().put("user", req.getSession().getAttribute(SessionKeys.USER));
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Key groupKey = KeyFactory.createKey(Keys.Group.KIND, Keys.Group.NAME);
		Query query = new Query(Entities.Group.KIND, groupKey).setFilter(new FilterPredicate(Entities.Group.Property.UUID, FilterOperator.EQUAL, groupId));
		
		Entity groupEntity = datastore.prepare(query).asSingleEntity();
		
		String partecipatingUserListStringified = ((Text) groupEntity.getProperty(Entities.Group.Property.PARTECIPATING_USERS)).getValue();
		
		Type partecipatingUserListType = new TypeToken<Collection<PartecipatingGroupUser>>(){}.getType();
		Collection<PartecipatingGroupUser> partecipatingUserList = new Gson().fromJson(partecipatingUserListStringified, partecipatingUserListType);
		
		String offererBasename = null;
		Map<String, String> partecipatingUserMap = new HashMap<>();
		for(PartecipatingGroupUser partecipatingUser : partecipatingUserList){
			if(offerer.equals(partecipatingUser.getUsername())){
				offererBasename = partecipatingUser.getDisplayName();
			} else {
				partecipatingUserMap.put(partecipatingUser.getDisplayName(), partecipatingUser.getUsername());
			}
		}
		pd.getData().put("offerer", offererBasename);
		pd.getData().put("offererUsername", offerer);
		
		pd.getData().put("partecipatingUserMap", partecipatingUserMap);

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(new Gson().toJson(pd));
    }
}