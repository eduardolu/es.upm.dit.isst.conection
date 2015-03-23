package es.upm.dit.isst.conection.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import es.upm.dit.isst.conection.model.Todo;

public class TodoDAOImpl implements TodoDAO {

	private static TodoDAOImpl instance;

	private TodoDAOImpl() {
	}

	public static TodoDAOImpl getInstance(){
		if (instance == null)
			instance = new TodoDAOImpl();
		return instance;
	}


	@Override
	public List<Todo> listTodos() {
		EntityManager em = EMFService.get().createEntityManager();
		// read the existing entries
		Query q = em.createQuery("select m from Todo m");
		List<Todo> todos = q.getResultList();
		return todos;
	}

	@Override
	public void add(String userId, String summary, String description) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Todo todo = new Todo(userId, summary, description);
			em.persist(todo);
			em.close();
		}

	}

	@Override
	public List<Todo> getTodos(String userId) {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select t from Todo t where t.author = :userId");
		q.setParameter("userId", userId);
		List<Todo> todos = q.getResultList();
		return todos;
	}

	@Override
	public void remove(long id) {
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Todo todo = em.find(Todo.class, id);
			em.remove(todo);
		} finally {
			em.close();
		}
	}

	@Override
	public List<String> getUsers() {
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em
				.createQuery("select distinct t.author from Todo t");
		List<String> users = q.getResultList();
		return users;
	}
}
