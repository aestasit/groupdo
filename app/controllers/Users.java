package controllers;

import java.util.List;

import models.*;
import play.Logger;
import play.api.libs.Crypto;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
@Security.Authenticated(Secured.class)
public class Users extends Controller {
	
	public static Result signup(){
		Form<SignupForm> userForm = form(SignupForm.class);
		return ok(views.html.user.signup.render(userForm));
	}
	
	public static Result createUser(){
		Form<SignupForm> userForm = form(SignupForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			return badRequest(views.html.user.signup.render(userForm));
		}
		User u = new User();
		u.username = userForm.get().username;
		u.password = Crypto.sign(userForm.get().password);
		u.save();
		session("currentUser",u.username);
		return redirect(routes.Users.view(u.username));
	}
	
	public static Result view(String username){
		
		User user = User.find.where().eq("username",username).findUnique();
		if(user==null){
			return notFound();
		}
					
		List<Project> createdByYou = Project.find.where().eq("creator", user).findList();
		List<Project> joined = Project.find.where().eq("members.id", user.id).findList();
		return ok(views.html.user.show.render(createdByYou,joined));
		
	}
}
