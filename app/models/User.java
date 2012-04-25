package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
@Entity
public class User extends Model{
	
	@Id
	public Long id;
	@Column(unique=true)
	public String username;
	
	public String password;
	

	public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class); 
	
	public static User authenticate(String email, String password) {
	        return find.where()
	            .eq("email", email)
	            .eq("password", password)
	            .findUnique();
	    }

}
