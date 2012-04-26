package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;

import play.Logger;
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
		Expression owner = Expr.eq("createdProjects.id",projectId);
		Expression member = Expr.eq("joinedProjects.id",projectId);
		List<User> invitables = find.select("id").where().add(owner).findList();
		invitables.addAll(find.select("id").where().add(member).findList());
		List<Long> ids = new ArrayList<Long>();
		for (User u : invitables) {
			ids.add(u.id);
		}
		return find.where().not(Expr.in("id", ids)).findList(); 
	}

}
