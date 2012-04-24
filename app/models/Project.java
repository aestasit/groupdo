package models;

import java.util.List;

public class Project {
	
	public Long id;
	public List<Task> tasks;
	public User creator;
	public List<User> users;
}
