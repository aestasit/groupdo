

import play.*;
import play.libs.*;

import java.util.*;


import models.*;
import java.util.Map;


import play.GlobalSettings;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;

public class Global extends GlobalSettings{
	
	    
	    public void onStart(Application app) {
	        InitialData.insert(app);
	    }
	    
	    static class InitialData {
	        
	        public static void insert(Application app) {
	        	
	        	int projects = Ebean.find(Project.class).findRowCount(); 
	            if(projects>0){
	        	Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yaml");

	                // Insert users first
	                Ebean.save(all.get("users"));
	                Ebean.save(all.get("projects"));
	                for(Object p : all.get("projects")){
	                	
	                	Ebean.saveManyToManyAssociations(p, "members");
	                }
	        }
	       
	                
	            }
	        
	    }

}
