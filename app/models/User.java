package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.api.libs.Crypto;
import play.db.ebean.Model;
@Entity
public class User extends Model{
	
	@Id
	public Long id;
	@Column(unique=true)
	public String username;
	
	public String password;
	

	public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class); 
	
	public static User authenticate(String username, String password) {
	        return find.where()
	            .eq("username", username)
	            .eq("password", Crypto.sign(password))
	            .findUnique();
	    }

}
