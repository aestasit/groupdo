package controllers;

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
            if(User.authenticate(email, password) == null) {
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
            routes.Application.login()
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
            
			session("username", loginForm.get().username);
            return redirect(
                routes.Projects.index()
            );
        }
    }
	
  public static Result index() {
    return ok(index.render("Your new application is ready."));
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