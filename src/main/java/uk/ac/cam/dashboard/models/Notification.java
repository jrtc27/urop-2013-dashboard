package uk.ac.cam.dashboard.models;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="NOTIFICATIONS")
public class Notification {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	
	@OneToMany(mappedBy = "notification")
	private Set<NotificationUser> users = new HashSet<NotificationUser>();
	
	private String message;
	private Calendar timestamp;
	private String section;
	private String link;
	private boolean read;
	
	public Notification() {}
	public Notification(String message, String section, String link) {
		this.message = message;
		this.section = section;
		this.link = link;
		this.timestamp = Calendar.getInstance();
		this.read = false;
	}

	public Map<String,?> toMap() {
		ImmutableMap.Builder<String, Object> map = new ImmutableMap.Builder<String, Object>();
		
		map = map.put("id", this.id);
		map = map.put("message", this.message);
		map = map.put("section", this.section);
		map = map.put("link", this.link);
		map = map.put("timestamp", this.timestamp.getTime().toString());
		map = map.put("read", this.read);
		
		ImmutableMap<String, ?> finalMap = map.build();
		return finalMap; 
	}
	
	public boolean getRead() {return read;}
	public void setRead(boolean read) {this.read = read;}
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public Calendar getTimestamp() { return timestamp; }
	public void setTimestamp(Calendar timestamp) { this.timestamp = timestamp; }
	
	public String getSection() {return section;}
	public void setSection(String section) {this.section = section;}
	
	public String getLink() {return link;}
	public void setLink(String link) {this.link = link;}
		
}
