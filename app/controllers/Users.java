package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Users extends Controller{
	
	public static Result signup(){
		Form<SignupForm> userForm = form(SignupForm.class);
		return ok(views.html.user.signup.render(userForm));
	}
	
	public static Result createUser(){
		Form<SignupForm> userForm = form(SignupForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			return badRequest(views.html.user.signup.render(userForm));
		}
		return ok(views.html.user.signup.render(userForm));
	}
}
