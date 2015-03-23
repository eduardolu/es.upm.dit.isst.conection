package es.upm.dit.isst.conection;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.isst.conection.dao.TodoDAO;
import es.upm.dit.isst.conection.dao.TodoDAOImpl;

public class RemoveTodoServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public void doGet(HttpServletRequest req, HttpServletResponse resp)
  throws IOException {
    String id = req.getParameter("id");
    TodoDAO dao = TodoDAOImpl.getInstance();
    dao.remove(Long.parseLong(id));
    resp.sendRedirect("/");
  }
} 
