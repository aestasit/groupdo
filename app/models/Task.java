package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity 
public class Task extends Model {
    
  public Long id;
  public String label;
  public Date completed;
    
  @ManyToOne
  public Project project;
  
  public static List<Task> all() {
    return new ArrayList<Task>();
  }
  
  public static void create(Task task) {
  }
  
  public static void delete(Long id) {
  }
  
//-- Queries
  
  public static Model.Finder<Long,Task> find = new Model.Finder(Long.class, Task.class);

  
  /**
   * Find tasks related to a project
   */
  public static List<Task> findByProject(Long project) {
      return Task.find.where()
          .eq("project.id", project)
          .findList();
  }
    
}