package es.upm.dit.isst.conection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.isst.conection.dao.TodoDAO;
import es.upm.dit.isst.conection.dao.TodoDAOImpl;
import es.upm.dit.isst.conection.model.Todo;


public class ReminderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		TodoDAO dao = TodoDAOImpl.getInstance();
		List<String> users = dao.getUsers();
		for (String user : users) {
			List<Todo> todos = dao.getTodos(user);
			todos = filterPending(todos);
			if (todos.size() > 0) {
				try {
					Properties props = new Properties();
					Session session = Session.getDefaultInstance(props, null);
					Message msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress("noreply@alnaba-isst-2015.appspotmail.com", "My Todo App 2015"));
					msg.addRecipient(Message.RecipientType.TO,
							new InternetAddress(user + "@gmail.com", user));
					msg.setSubject("You still have pending TODO's ");
					String msgBody = "You still have " + todos.size() + " pending TODO's: " + System.getProperty("line.separator");
					for (Todo todo : todos) {
						msgBody += "\t" + todo.getLongDescription() + System.getProperty("line.separator");
					}
					msgBody += System.getProperty("line.separator") + "Kind Regards," + System.getProperty("line.separator") + "The TODO team!";
					msg.setText(msgBody);
					Transport.send(msg);

				} catch(Exception e) {e.printStackTrace();}
			}
		}
	}

	private List<Todo> filterPending(List<Todo> todos) {
		List<Todo> pending = new ArrayList<Todo>();
		for (Todo todo : todos) {
			if (!todo.isFinished()) pending.add(todo);
		}
		return pending;
	}
}