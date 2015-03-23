package es.upm.dit.isst.conection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.SendResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import es.upm.dit.isst.conection.dao.TodoDAO;
import es.upm.dit.isst.conection.dao.TodoDAOImpl;
import es.upm.dit.isst.conection.model.Todo;






import java.util.Map;

public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/*
	/**
	 * HelloXmpp is an echo bot that sends back the message it receives.
	 */
	//public class HelloXmpp extends HttpServlet {
	/*
	  private XMPPService xmppService;

	  @Override
	  public void init() {
	    this.xmppService = XMPPServiceFactory.getXMPPService();
	  }

	  // For testing. Real requests are POST
	  public void doGet(HttpServletRequest req, HttpServletResponse res)
	      throws IOException {
	    Message message =new MessageBuilder()
	        .withMessageType(MessageType.CHAT)
	        .withFromJid(new JID(req.getParameter("from")))
	        .withRecipientJids(new JID(req.getParameter("to")))
	        .withBody(req.getParameter("body"))
	        .build();
	    processMessage(message, res);
	  }

	  public void doPost(HttpServletRequest req, HttpServletResponse res)
	      throws IOException {
	    processMessage(xmppService.parseMessage(req), res);
	  }

	  public void processMessage(Message message, HttpServletResponse res) throws IOException {
	    JID fromId = message.getFromJid();
	    Presence presence = xmppService.getPresence(fromId);
	    String presenceString = presence.isAvailable() ? "" : "not ";
	    SendResponse response = xmppService.sendMessage(
	        new MessageBuilder().
	        withBody(message.getBody() + " (you are " + presenceString + "available)").
	        withRecipientJids(fromId).
	        build());

	    for (Map.Entry<JID, SendResponse.Status> entry :
	        response.getStatusMap().entrySet()) {
	      res.getWriter().println(entry.getKey() + "," + entry.getValue() + "<br>");
	    }

	    res.getWriter().println("processed");
	  }
	}*/
	  

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		TodoDAO dao = TodoDAOImpl.getInstance();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String url = userService.createLoginURL(req.getRequestURI());
		String urlLinktext = "Login";
		List<Todo> todos = new ArrayList<Todo>();

		if (user != null) {
			url = userService.createLogoutURL(req.getRequestURI());
			urlLinktext = "Logout";
			todos = dao.getTodos(user.getNickname());
		}
		req.getSession().setAttribute("user", user);
		req.getSession().setAttribute("todos", new ArrayList<Todo>(todos));
		req.getSession().setAttribute("url", url);
		req.getSession().setAttribute("urlLinktext", urlLinktext);
		RequestDispatcher view = req
				.getRequestDispatcher("TodoApplication.jsp");
		view.forward(req, resp);
	}
}
