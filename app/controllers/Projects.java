package controllers;

import java.util.List;

import models.InviteForm;
import models.Project;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
@Security.Authenticated(Secured.class)
public class Projects extends Controller{
	
	
	public static Result newProject(){
		Form<Project> projectForm = form(Project.class);
		return ok(views.html.project.create.render(projectForm));
	}
  
	
	
	
	public static Result list(){
		return ok(views.html.project.list.render(Project.find.all()));
	}
	
	public static Result join(Long projectId){
		String u = session("currentUser");
		Project p = Project.find.byId(projectId);
		User user = User.find.where().eq("username", u).findUnique();
		if(u!=null && p!=null && !p.isCreatorOrMember(user.username)){
			p.members.add(user);
			p.saveManyToManyAssociations("members");
			flash("You succesfully joined the project "+p.name);
		}
		return redirect(routes.Users.view(u));
	}
	
  public static Result saveProject(){
	    User user = User.find.where().eq("username", session("currentUser")).findUnique();
	    Form<Project> projectForm = form(Project.class).bindFromRequest();
	    if(projectForm.hasErrors()) {
            return badRequest(views.html.project.create.render(projectForm));
        }
	    Project p = new Project();
	    p.name = projectForm.get().name;
	    p.creator = user;
	    p.save();
	    
		return redirect(routes.Users.view(user.username));
	}
  
  
  
  public static Result show(Long  id){
	  Project p = Project.find.setId(id).findUnique();
	  	if(p==null){
		  return notFound();
	  }
	  return ok(views.html.project.view.render(p));
  }
}
