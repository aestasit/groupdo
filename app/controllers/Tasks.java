package controllers;

import java.util.List;

import models.InviteForm;
import models.Project;
import models.Task;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.tasks.*;

@Security.Authenticated(Secured.class)
public class Tasks extends Controller {
	
	/**
     * Display the tasks panel for this project.
     */
    public static Result index(Long project) {
        //if(Secured.isMemberOf(project)) {
            return ok(
                index.render(
                    Project.find.byId(project),
                    Task.findByProject(project)
                )
            );
        //} else {
        //    return forbidden();
        //}
    }
  
	
	
}