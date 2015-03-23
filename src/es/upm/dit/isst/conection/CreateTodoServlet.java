package es.upm.dit.isst.conection;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.SendResponse;

import es.upm.dit.isst.conection.dao.TodoDAO;
import es.upm.dit.isst.conection.dao.TodoDAOImpl;

public class CreateTodoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	//prueba enviar el post
	private XMPPService xmppService;

	public void init() {
	    this.xmppService = XMPPServiceFactory.getXMPPService();
	  }
	  
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		User user = (User) req.getAttribute("user");
		if (user == null) {
			UserService userService = UserServiceFactory.getUserService();
			user = userService.getCurrentUser();
		}
		//XMPPService xmppService = XMPPServiceFactory.getXMPPService();
		Message message = new MessageBuilder()
				.withMessageType(MessageType.CHAT)
				.withFromJid(new JID("edu291086@gmail.com"))
				.withRecipientJids(new JID(req.getParameter("summary")))
				.withBody(req.getParameter("description")).build();
		processMessage(message, res);
		//xmppService.parseMessage(req);
		res.sendRedirect("/");
	}

	public void processMessage(Message message, HttpServletResponse res)
			throws IOException {
		JID fromId = message.getFromJid();
		Presence presence = xmppService.getPresence(fromId);
		String presenceString = presence.isAvailable() ? "" : "not ";
		SendResponse response = xmppService.sendMessage(new MessageBuilder()
				.withBody(
						message.getBody() + " (you are " + presenceString
								+ "available)").withRecipientJids(fromId)
				.build());

		for (Map.Entry<JID, SendResponse.Status> entry : response.getStatusMap().entrySet()) {
			res.getWriter().println(
					entry.getKey() + "," + entry.getValue() + "<br>");
		}

		res.getWriter().println("processed");
	}
/*
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		System.out.println("Creating new todo ");
		User user = (User) req.getAttribute("user");
		if (user == null) {
			UserService userService = UserServiceFactory.getUserService();
			user = userService.getCurrentUser();
		}

		String summary = checkNull(req.getParameter("summary"));
		String longDescription = checkNull(req.getParameter("description"));

		TodoDAO dao = TodoDAOImpl.getInstance();
		dao.add(user.getNickname(), summary, longDescription);

		resp.sendRedirect("/");
	}
*/
	private String checkNull(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}
} 
