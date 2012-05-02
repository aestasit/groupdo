package controllers;

import java.util.HashMap;
import java.util.Map;

import models.Project;
import models.Task;
import models.TaskSocket;
import models.User;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;

@Security.Authenticated(Secured.class)
public class Tasks extends Controller {
	
	/**
     * Display the tasks panel for this project.
     */
    public static Result index(Long project) {
        if(Secured.isMemberOf(project) || Secured.isOwnerOf(project)) {
            return ok(
                views.html.tasks.index.render(
                    Project.find.byId(project)
                )
            );
        } else {
            return forbidden("Not allowed");
        }
    }
    
    public static Result newTask(Long project) {
        if(Secured.isMemberOf(project) || Secured.isOwnerOf(project)) {
        	try{
        		String task = request().body().asFormUrlEncoded().get("label")[0];
        		User user = User.find.where().eq("username", session("currentUser")).findUnique();
        		Task t = new Task();
        		t.label = task;
        		Project p = Project.find.byId(project);
        		t.project = p;
        		t.opened = user;
        		p.tasks.add(t);
        		p.save();
        		t.save();
        		Map<String,String> result = new HashMap<String, String>();
        		result.put("task",task);
        		result.put("id", Long.toString(t.id));
        		result.put("by", session("currentUser"));
        		result.put("projectId", Long.toString(p.id));
        		return ok(Json.toJson(result));
        	}catch (Exception e) {
				return badRequest();
			}
        } else {
            return forbidden("Not allowed");
        }
    }
  
    public static WebSocket<JsonNode> ws(final Long projectId,final String username) {
        return new WebSocket<JsonNode>() {
            
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
                try { 
                    TaskSocket.join(projectId,username, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
	
	
}