package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Project extends Model {
	
	@Id
	public Long id;
	
	@Required
	public String name;
	
	public static Finder<Long,Project> find = new Finder<Long,Project>(Long.class, Project.class); 
	
	//public transient List<Task> tasks;
	
	@ManyToOne
	public User creator;
	
	@ManyToMany
	public List<User> members = new ArrayList<User>();
	
	@OneToMany
	public Set<Task> tasks = new HashSet<Task>();
	
	public int getMembers(){
		return 1 + (members!=null?members.size():0);
	}
	
	public static boolean isOwner(Long projectId,String username){
		return find.where().idEq(projectId).eq("creator.username", username).findUnique() !=null;
	}
	
	public static boolean isMember(Long projectId,String username){
		return find.where().idEq(projectId).eq("members.username", username).findList() !=null;
	}
	
	public boolean isCreatorOrMember(String username){
		if(this.creator.username.equals(username)){
			return true;
		}
		for (User u : members) {
			if(u.username.equals(username)){
				return true;
			}
		}
		return false;
	}
}
