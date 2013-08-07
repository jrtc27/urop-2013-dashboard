package uk.ac.cam.dashboard.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;

import uk.ac.cam.dashboard.helpers.LDAPQueryHelper;
import uk.ac.cam.dashboard.util.HibernateUtil;

import com.google.common.collect.ImmutableMap;

@Entity
@Table(name="DEADLINES")
public class Deadline implements Mappable {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;

	private String title;
	private String message;
	private String url;
	
	//@Temporal(TemporalType.TIMESTAMP)
	private Calendar datetime;

	@OneToMany(mappedBy = "deadline", cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<DeadlineUser> users = new HashSet<DeadlineUser>();
	
	@ManyToOne
	@JoinColumn(name="USER_CRSID")
	private User owner;
	
	public Deadline() {}
	public Deadline(int id, 
									String title, 
									String message, 
									Set<DeadlineUser> users, 
									User owner) {
		this.id = id;
		this.title = title;
		this.message = message;
		this.users = users;
		this.owner = owner;
	}
	
	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }

	public String getMessage() { return this.message; }
	public void setMessage(String message) { this.message= message; }
	
	public String getURL() { return this.url; }
	public void setURL(String url) { this.url= url; }
	
	public Calendar getDatetime() { return this.datetime; }
	public void setDatetime(Calendar datetime) { this.datetime= datetime; }
	public String getFormattedDate() { 
		SimpleDateFormat df = new SimpleDateFormat("EEEEE, dd MMMMM yyyy");
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
		return df.format(datetime.getTime()) + " at " + tf.format(datetime.getTime());
	}
	public Map<String, ?> getDateMap() { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
		SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
		String dateString = dateFormat.format(datetime.getTime());
		String hourString = hourFormat.format(datetime.getTime());
		String minuteString = minuteFormat.format(datetime.getTime());	
		return ImmutableMap.of("date", dateString, "hour", hourString, "minute", minuteString);
	}
	
	public User getOwner() { return this.owner; }
	public void setOwner(User owner) { this.owner= owner; }
	
	public Set<DeadlineUser> getUsers() { return this.users; }
	public void clearUsers() { users.clear(); }
	public void setUsers(Set<DeadlineUser> users) { this.users.addAll(users); }
	
	public boolean getImminence() {
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_YEAR,1);
		return (this.datetime.before(tomorrow));
	}
	
	// Queries
	public static Deadline getDeadline(int id){
		
		Session session = HibernateUtil.getTransactionSession();
		
		Query getDeadline = session.createQuery("from Deadline where id = :id").setParameter("id", id);
	  	Deadline deadline = (Deadline) getDeadline.uniqueResult();	
	  	return deadline;
	}
	
	// Map builder
	@Override
	public Map<String, ?> toMap() {
		
		ImmutableMap.Builder<String, Object> builder; 
		
		try {
			builder = new ImmutableMap.Builder<String, Object>();
			builder =builder
				.put("id", id)
				.put("name", title)
				.put("message", message)
				.put("url", url)
				.put("owner", owner.toMap());
			
			HashSet<ImmutableMap<String,?>> deadlineUsers = new HashSet<ImmutableMap<String,?>>();
			String crsid;
			for(DeadlineUser du : users){
				// Get users crsid
				crsid = du.getUser().getCrsid();
				// Get users display name from LDAP
				String name = LDAPQueryHelper.getRegisteredName(crsid);
				deadlineUsers.add(ImmutableMap.of("crsid",crsid, "name", name));
			}		
			
			builder = builder
					.put("datetime", getFormattedDate())
					.put("date", getDateMap())
					.put("users", deadlineUsers);
			
		} catch(NullPointerException e){
			builder  = new ImmutableMap.Builder<String, Object>();
			builder =builder
					.put("id", id)
					.put("name", "Error getting deadline")
					.put("message", "")
					.put("url", "")
					.put("owner", "")
					.put("datetime", getFormattedDate())
					.put("date", getDateMap())
					.put("users", "");
			return builder.build();
		}
		return builder.build();
	}
	
	public List<ImmutableMap<String, ?>> usersToMap(){
		
		List<ImmutableMap<String, ?>> deadlineUsers =new ArrayList<ImmutableMap<String, ?>>();
		
		for(DeadlineUser u : users){
			String crsid = u.getUser().getCrsid();
			deadlineUsers.add(ImmutableMap.of("crsid", crsid, "name", LDAPQueryHelper.getRegisteredName(crsid)));
		}
		
		return deadlineUsers;
	}
	
}
