package uk.ac.cam.dashboard.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.cam.dashboard.models.NotificationUser;
import uk.ac.cam.dashboard.models.User;
import uk.ac.cam.dashboard.queries.NotificationQuery;
import uk.ac.cam.dashboard.util.Util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;

public class GetNotificationForm {

	@QueryParam("offset") String offset;
	@QueryParam("limit") String limit;
	@QueryParam("section") String section;
	
	private Integer intOffset;
	private Integer intLimit;
	
	// Logger
	private static Logger log = LoggerFactory.getLogger(GetNotificationForm.class);
	
	public Map<String, ?> handle(User user, boolean read) {
		
		Map<String, Object> userNotifications = new HashMap<String, Object>();

		NotificationQuery nq = NotificationQuery.all().byUser(user);
		
		// Filter query based on parameters set
		
		if (section != null && !section.isEmpty()) {
			nq.inSection(section);
			userNotifications.put("section", section);
		} else {
			userNotifications.put("section", "none");
		}
		
		nq.isRead(read);
		userNotifications.put("read", read);
		
		// Get number of rows before offset or limit is set
		
		int total = nq.totalRows();
		userNotifications.put("total", total);
		
		// Impose offset and limit
		
		if (intOffset != null) {
			nq.offset(intOffset);
			userNotifications.put("offset", intOffset);
		} else {
			userNotifications.put("offset", 0);
		}
		
		if (intLimit != null) {
			nq.limit(intLimit);
			userNotifications.put("limit", intLimit);
		} else {
			nq.limit(10);
			userNotifications.put("limit", 10);
		}
		
		// Process query result set
		
		List<NotificationUser> results = nq.list();
		
		List<ImmutableMap<String, ?>> notifications = new ArrayList<ImmutableMap<String,?>>();
		for (NotificationUser nu : results) {
			notifications.add(nu.toMap());
		}
		
		userNotifications.put("user", user.toMap());
		userNotifications.put("notifications", notifications);

		log.debug("Returning JSON of user notifications");
		return userNotifications;
	}
	
	public ImmutableMap<String, List<String>> validate() {
		ArrayListMultimap<String, String> errors = ArrayListMultimap.create();
		
		// Offset
		if (offset != null && !offset.equals("")) {
			try {
				intOffset = Integer.parseInt(offset);
			} catch(Exception e) {
				errors.put("offset", "Offset must be an integer");
			}
		}
		
		// Limit
		if (limit != null && !limit.equals("")) {
			try {
				intLimit = Integer.parseInt(limit);
			} catch(Exception e) {
				errors.put("limit", "Limit must be an integer");
			}
		}
		
		// Section
		String[] validSections = {"dashboard", "signups", "questions", "handins"}; // Shared with CreateNotificationForm
		if (section != null && !section.equals("") && !Arrays.asList(validSections).contains(section)) {
			errors.put("section", "Invalid section field.");
		}
		
		return Util.multimapToImmutableMap(errors);
	}
	
	public ImmutableMap<String, ?> toMap() {
		ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>();
		
		String localOffset = (offset == null ? "" : offset);
		builder.put("offset", localOffset);
		
		String localLimit = (limit == null ? "" : limit);
		builder.put("limit", localLimit);
		
		String localSection = (section == null ? "" : section);
		builder.put("section", localSection);
		
		return builder.build();
	}
	
}
