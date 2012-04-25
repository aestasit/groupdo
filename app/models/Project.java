package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Expression;

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
	public transient List<User> users;
	
	public static boolean isOwner(Long projectId,String username){
		return find.where().idEq(projectId).eq("creator.username", username).findUnique() !=null;
	}
	
}
