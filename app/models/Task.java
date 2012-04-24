package models;

import java.util.*;

public class Task {
    
  public Long id;
  public String label;
  public Date completed;
    

  public static List<Task> all() {
    return new ArrayList<Task>();
  }
  
  public static void create(Task task) {
  }
  
  public static void delete(Long id) {
  }
    
}