package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.api.libs.Crypto;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
@Entity
public class User extends Model{
	
	@Id
	public Long id;
	@Column(unique=true)
	public String username;
	
	@Required
	public String password;
	
	
	@OneToMany(mappedBy="creator")
	public List<Project> createdProjects;
	
	@ManyToMany(mappedBy="members")
	public List<Project> joinedProjects;
	
	public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class); 
	
	public static User authenticate(String username, String password) {
	        return find.where()
	            .eq("username", username)
	            .eq("password", Crypto.sign(password))
	            .findUnique();
	    }
	
	public static List<User> findNotPartecipating(Long projectId){
		return find.where().ne("createdProjects.id", projectId).ne("joinedProjects.id",projectId).findList();
	}

}
