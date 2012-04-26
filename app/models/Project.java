package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Project extends Model{
	
	@Id
	public Long id;
	@Required
	public String name;
	
	
	public static Finder<Long,Project> find = new Finder<Long,Project>(Long.class, Project.class); 
	public transient List<Task> tasks;
	@ManyToOne
	public User creator;
	
	@ManyToMany
	public List<User> members = new ArrayList<User>();
	public int getMembers(){
		return 1 + (members!=null?members.size():0);
	}
	public static boolean isOwner(Long projectId,String username){
		return find.where().idEq(projectId).eq("creator.username", username).findUnique() !=null;
	}
	
}
