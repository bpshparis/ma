package com.bpshparis.wsvc.app0;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class SessionAttributeListener
 *
 */
@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {

    /**
     * Default constructor. 
     */
    public SessionAttributeListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    	HttpSession s = arg0.getSession();
    	
		System.out.println(arg0.getName() + " saved in sessionId " + s.getId() + ".");
    	
//    	if(arg0.getName() == "client"){
//	    	InitialContext ic = (InitialContext) s.getServletContext().getAttribute("ic");
//	    	String jndiName = "jdbc/" + s.getAttribute("client");
//	    	try {
//				DataSource ds = (DataSource) ic.lookup(jndiName);
//				Connection con = ds.getConnection();
//				s.setAttribute("con", con);
//				s.setAttribute("jndiName", jndiName);
//				System.out.println("SessionId " + s.getId() + " is now connected to " + jndiName);
//				
//			} catch (NamingException | SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
    	
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
