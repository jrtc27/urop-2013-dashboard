package uk.ac.cam.dashboard.controllers;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import uk.ac.cam.dashboard.models.Api;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Path("/dashboard/api")
public class ApiController extends ApplicationController {
	
	@GET @Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ImmutableMap<String, ?> getUserApiKeys() {
		User currentUser = initialiseUser();
		
		return ImmutableMap.of("user", currentUser.getCrsid(), "keys", currentUser.getUserApisMap());
	}
	
	@GET @Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> getNewApiKey() {
		Session s = HibernateUtil.getTransactionSession();

		User currentUser = initialiseUser();
		
		Api api = new Api();
		api.setUser(currentUser);
		
		s.save(api);
		
		return ImmutableMap.of("user", currentUser.getCrsid(), "key", api.getKey());
	}
	
	// Validation
	
	public static boolean validateApiKeyForUser(String key, String userCrsid) {
		User user = User.registerUser(userCrsid);
		
		for (Api a : user.getApis()) {
			if ( key.equals(a.getKey()) ) {
				return true;
			}
		}
		
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	public static boolean validateGlobalApiKey(String key) {
		Session s = HibernateUtil.getTransactionSession();
		
		Criteria criteria = s.createCriteria(Api.class);
		criteria.add(Restrictions.eq("globalPermissions", true));
		List<Api> apis = criteria.list();
		
		for ( Api a : apis ) {
			if ( a.getKey().equals(key) ) {
				return true;
			}
		}
		
		return false;
		
	}
	
}
