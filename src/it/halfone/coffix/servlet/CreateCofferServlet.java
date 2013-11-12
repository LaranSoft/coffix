package it.halfone.coffix.servlet;

import it.halfone.coffix.configuration.Configuration;
import it.halfone.coffix.constants.Entities;
import it.halfone.coffix.constants.Keys;
import it.halfone.coffix.constants.SessionKeys;
import it.halfone.coffix.dao.CofferPartecipant;
import it.halfone.coffix.dao.User;
import it.halfone.coffix.exception.CoffixException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.gson.Gson;

/**
 * CreateCofferServlet - 21/ott/2013
 * <br>
 * <h1>WServlet</h1>
 * <br>
 * Le WServlet si occupano di effettuare operazioni di scrittura e restituzione di un codice che rappresenti il risultato dell'operazione.
 * <br>
 * Accettano parametri in ingresso. Per questo motivo il client dovrebbe richiamarle sempre mediante il metodo $POST: non GET per evitare 
 * i parametri in query string e $ per evitare l'apparizione della dialog di conferma reinvio dati se si preme F5
 * <br>
 * <br>
 * @author Andrea La Rosa
 */
public class CreateCofferServlet extends HttpServlet {

	private static final long serialVersionUID = 2066800558202274672L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		String offerer = req.getParameter("offerer");
		String offeredParam = req.getParameter("offereds");
		String groupId = req.getParameter("groupId");
		
		User loggedUser = (User) req.getSession().getAttribute(SessionKeys.USER);
		
		String responseMessage = "";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
		
		try {
			if(StringUtils.isEmpty(offerer)){
				throw new CoffixException("offerer is null", "200");
			} 
			
			if(StringUtils.isEmpty(offeredParam)){
				throw new CoffixException("offered is null", "201");
			} 
			
			String[] offereds = offeredParam.split(";");
			
			if(offereds.length <= 0){
				throw new CoffixException("offereds is empty", "202");
			}
			
			Collection<String> offeredSet = new HashSet<String>();
			for(String offered : offereds){
				offeredSet.add(offered);
			}
			
			if(offereds.length != offeredSet.size()){
				throw new CoffixException("offereds must not contain duplicates", "203");
			} 
			
			if(offeredSet.contains(offerer)){
				throw new CoffixException("offereds must not contain offerer", "204");
			}
			
			Key loggedUserEntityKey = KeyFactory.stringToKey(loggedUser.getEntityKey());
			Entity loggedUserEntity = datastore.get(loggedUserEntityKey);
			
			Collection<String> groupList = (Collection<String>) loggedUserEntity.getProperty(Entities.User.Property.GROUP_LIST);
			
			if(groupList == null || !groupList.contains(groupId)){
				throw new CoffixException("invalid request: user #" + loggedUser.getUsername() + "# can't operate on group #" + groupId + "#", "205");
			}
			
			Key userKey = KeyFactory.createKey(Keys.User.KIND, Keys.User.NAME);
			
			Query query = new Query(Entities.User.KIND, userKey).setFilter(new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.EQUAL, offerer));
			Entity offererEntity = datastore.prepare(txn, query).asSingleEntity();
			if(offererEntity == null){
				throw new CoffixException("unexisting offerer: #" + offerer + "#", "206");
			}
			
			query = new Query(Entities.User.KIND, userKey).setFilter(new FilterPredicate(Entities.User.Property.USERNAME, FilterOperator.IN, offeredSet));
			List<Entity> cofferedUserList = datastore.prepare(txn, query).asList(FetchOptions.Builder.withDefaults());
			
			if(cofferedUserList.size() != offeredSet.size()){
				throw new CoffixException("unexisting offered", "207");
			}
			
			Map<String, String> emailAddressesToAlert = new HashMap<>(); 
			
			CofferPartecipant offererPartecipant = new CofferPartecipant();
			offererPartecipant.setHasNegate(false);
			offererPartecipant.setUsername(offerer);
			offererPartecipant.setDisplayName((String) offererEntity.getProperty(Entities.User.Property.BASENAME));
			
			CofferPartecipant initiator = new CofferPartecipant();
			initiator.setUsername(loggedUser.getUsername());
			initiator.setDisplayName(loggedUser.getBaseName());
			
			Collection<CofferPartecipant> offeredPartecipantSet = new HashSet<CofferPartecipant>();
			for(Entity cofferedUser : cofferedUserList){
				CofferPartecipant cofferPartecipant = new CofferPartecipant();
				cofferPartecipant.setHasNegate(false);
				cofferPartecipant.setUsername((String) cofferedUser.getProperty(Entities.User.Property.USERNAME));
				cofferPartecipant.setDisplayName((String) cofferedUser.getProperty(Entities.User.Property.BASENAME));
				offeredPartecipantSet.add(cofferPartecipant);
			}
			
			Key cofferKey = KeyFactory.createKey(Keys.Coffer.KIND, Keys.Coffer.NAME);
			Entity newCoffer = new Entity(Entities.Coffer.KIND, cofferKey);
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, 12);
//			calendar.add(Calendar.SECOND, 30);
			
			Gson gson = new Gson();
			
			newCoffer.setProperty(Entities.Coffer.Property.INITIATOR, gson.toJson(initiator));
			newCoffer.setProperty(Entities.Coffer.Property.OFFERER, gson.toJson(offererPartecipant));
			newCoffer.setProperty(Entities.Coffer.Property.OFFEREDS, gson.toJson(offeredPartecipantSet));
			newCoffer.setProperty(Entities.Coffer.Property.STATUS, 0);
			newCoffer.setProperty(Entities.Coffer.Property.EXPIRATION_TIME, calendar.getTimeInMillis());
			newCoffer.setProperty(Entities.Coffer.Property.GROUP, groupId);
			
			datastore.put(txn, newCoffer);
			
			txn.commit();
			
			if(!emailAddressesToAlert.isEmpty()){
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);
				
				String msgBody = Configuration.getInstance().get("NEW_COFFER.MAIL_MESSAGE.BODY");
				
				try {
					Message msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress("service@coffiix.appspotmail.com", "Coffiix service"));
					
					for(String key : emailAddressesToAlert.keySet()){
						msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddressesToAlert.get(key), key));
					}
					
					msg.setSubject(Configuration.getInstance().get("NEW_COFFER.MAIL_MESSAGE.SUBJECT"));
					msg.setText(msgBody);
					Transport.send(msg);
					
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			
		} catch(CoffixException ce){
			ce.printStackTrace();
			responseMessage = "error_" + ce.getErrorCode();
		} catch(Throwable t){
			responseMessage = "error_100";
			t.printStackTrace();
		} finally {
			if(txn.isActive()){
				txn.rollback();
			}
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		resp.getWriter().write(responseMessage);
	}
}
