package controllers;

import play.Logger;
import play.mvc.*;
import views.html.*;
import models.*;

import play.data.*;
public class Application extends Controller {
  
  	// -- Authentication
    
    public static class Login {
        
        public String username;
        public String password;
        
        public String validate() {
            User u = null;
        	if((u = User.authenticate(username, password)) == null) {
                return "Invalid user or password";
            }
            return null;
        }
        
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(
            index.render(form(Login.class))
        );
    }
	
	/**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.index()
        );
    }
	
	/**
     * Handle login form submission.
     */
    public static Result authenticate() {
        
		Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(index.render(loginForm));
        } else {
            
			session("currentUser", loginForm.get().username);
            return redirect(
                routes.Users.view(loginForm.get().username)
            );
        }
    }
	
  public static Result index() {
	String currentUser = session("currentUser");
	if(currentUser==null){
		return redirect(routes.Application.login());
	}else{
		return redirect(routes.Users.view(currentUser));
	}
    //return redirect(routes.Application.tasks());
  }

  public static Result tasks() {
    return TODO;
  }

  public static Result newTask() {
    return TODO;
  }

  public static Result deleteTask(Long id) {
    return TODO;
  }
  
}