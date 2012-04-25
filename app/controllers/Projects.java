package controllers;

import models.Project;
import play.data.*;
import play.*;
import play.mvc.*;
import views.html.*;
public class Projects extends Controller{
	
	
	public static Result newProject(){
		Form<Project> projectForm = form(Project.class);
		return ok(views.html.project.create.render(projectForm));
	}
  
  public static Result saveProject(){
	    Form<Project> projectForm = form(Project.class).bindFromRequest();
	    if(projectForm.hasErrors()) {
            return badRequest(views.html.project.create.render(projectForm));
        }
	    Project p = new Project();
	    p.name = projectForm.get().name;
	    p.save();
	    
		return redirect(routes.Projects.show(p.id));
	}
  
  public static Result show(Long  id){
	  Project p = Project.find.setId(id).findUnique();
	  Logger.error("id:" +p.name);
	  if(p==null){
		  return notFound();
	  }
	  return ok(views.html.project.view.render(p));
  }
}
