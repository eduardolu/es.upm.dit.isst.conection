package es.upm.dit.isst.conection.dao;

import java.util.List;

import es.upm.dit.isst.conection.model.Todo;

public interface TodoDAO {

	public List<Todo> listTodos();
	public void add (String userId, String summary, String description);
	public List<Todo> getTodos(String userId);
	public void remove (long id);
	public List<String> getUsers();
	
}