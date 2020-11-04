// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String login_id;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String login_id, String host, int port) {
		// Initialize variables
	  super(host,port);
	  this.login_id = login_id;
		
	}
  
  public ChatClient(String login_id, String host, int port, ChatIF clientUI)  
  {
	  super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.login_id = login_id;
    
  }

  
  //Instance methods ************************************************
   
  public void startConnection() throws IOException{
	  
	  openConnection();
      handleMessageFromClientUI("#login " + login_id);
	  
  }
  
  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	@Override
	protected void connectionClosed() {
  		clientUI.display("Connection to server was terminated.");
  	  
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	@Override
	protected void connectionException(Exception exception) {
  		System.out.println("Could not connect to server.");
  		try{
  			closeConnection();
  		} catch (IOException e) {}
	}
 
  
  
  
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  if (message.startsWith("#") && !message.startsWith("#login ")) {
  		clientUI.display(message);
  		handleFunctions(message);
	  } else {
		  
		  try
		    {
			  sendToServer(message);
		    }
		    catch(IOException e)
		    {
		      clientUI.display
		        ("Could not send message to server.  Terminating client.");
		      quit();
		    }
		  
	  }
	  
    
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  private void handleFunctions(String message) {
	
	  if (message.equals("#quit")) {
		  quit();
	  } else if (message.equals("#logoff")) {
		  
		  try{
			  closeConnection();
		  } catch (IOException e) {}  
		  
	  } else if (message.startsWith("#sethost")) {
		  
		  if(!this.isConnected()) {
			  
			  if (message.length() >= 10) {
					this.setHost(message.substring(9)); 
					clientUI.display("host set to : " + (message.substring(9)));
			  } else {
				  clientUI.display(" not a valid host ");
				  
			  }
		  } else {
			  clientUI.display(" Please disconnect before trying to change host");
		  }
		  
	  } else if (message.startsWith("#setport")) {
		  
		  if(!this.isConnected()) {
			  
			  
			  if (message.length() >= 10) {
				  try {
						this.setPort(Integer.parseInt(message.substring(9)));  
						clientUI.display("port set to : " + Integer.parseInt(message.substring(9)));
					  } catch (NumberFormatException e){
						  clientUI.display(" not a valid port");
					  }
			  } else {
				  clientUI.display(" not a valid port");
			  }
			  
			  
		  } else {
			  clientUI.display(" Please disconnect before trying to change port");
		  }
		
		  
	  } else if (message.equals("#login")) {
		  
		  if(!this.isConnected()) {
			  try {
			  this.openConnection();
			  handleMessageFromClientUI("#login " + login_id);
			  } catch (IOException e) {
				  clientUI.display("Could not connect");
			  }
			  
		  } else {
			  clientUI.display(" Already connected");
		  }
		  
	  } else if (message.equals("#gethost")) {
		  
		  clientUI.display(this.getHost());
		  
	  } else if (message.equals("#getport")) {
		  
		  clientUI.display(Integer.toString(this.getPort()));
		  
      } else {
    	  clientUI.display("Unknown command");
      }
  
  }
  
	  
  
  
  
}
//End of ChatClient class
