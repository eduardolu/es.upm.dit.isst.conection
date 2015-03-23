package es.upm.dit.isst.conection;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

import es.upm.dit.isst.conection.dao.TodoDAO;
import es.upm.dit.isst.conection.dao.TodoDAOImpl;

public class EmailServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	//prueba chat
	private XMPPService xmppService;

	public void init() {
		this.xmppService = XMPPServiceFactory.getXMPPService();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		Message message = new MessageBuilder()
				.withMessageType(MessageType.CHAT)
				.withFromJid(new JID("edu291086@gmail.com"))
				.withRecipientJids(new JID(req.getParameter("to")))
				.withBody(req.getParameter("body")).build();
		processMessage(message, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		processMessage(xmppService.parseMessage(req), res);
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

		for (Map.Entry<JID, SendResponse.Status> entry : response
				.getStatusMap().entrySet()) {
			res.getWriter().println(
					entry.getKey() + "," + entry.getValue() + "<br>");
		}

		res.getWriter().println("processed");
	}
		  //asta aqui la prueba
	/*
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Properties props = new Properties();

		Session email = Session.getDefaultInstance(props, null);

		try {
			MimeMessage message = new MimeMessage(email, req.getInputStream());
			String summary = message.getSubject();
			String description = getText(message);
			Address[] addresses = message.getFrom();
			InternetAddress emailAddress = new InternetAddress(addresses[0].toString());
			User user = new User(emailAddress.getAddress(), "gmail.com");
			TodoDAO dao = TodoDAOImpl.getInstance();
			dao.add(user.getNickname(), summary, description);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
/*
	private boolean textIsHtml = false;

	/**
	 * Return the primary text content of the message.
	 */
/*
	private String getText(Part p) throws
	MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String)p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart)p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}*/


} 