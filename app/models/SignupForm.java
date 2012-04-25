package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

public class SignupForm {
	
	@Required
	public String username;
	@Required
	public String password;
	@Required
	public String confirmPassword;
	
	 public String validate() {
		 if(!password.equals(confirmPassword)){
			 return "Password field should match";
		 }
		 return null;
	 }
}
