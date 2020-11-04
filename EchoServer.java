// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  private ServerConsole console;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  public void setConsole(ServerConsole console) {
	  this.console = console;
  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  console.display("Message received: " + msg.toString() + " from " + client.getInfo("login_id"));

	  if (msg.toString().startsWith("#login ")){
		  if (client.getInfo("login_id") == null) {
			  client.setInfo("login_id", msg.toString().substring(7));
			  this.sendToAllClients(client.getInfo("login_id") + " has logged on.");
			  console.display(client.getInfo("login_id") + " has logged on.");
		  } else {
			  try{
				  client.close();
			  } catch (IOException e) {}
		  }
	  } else {
		    this.sendToAllClients(client.getInfo("login_id") + " : "+ msg.toString());
	  }
    
  }
  
  public void handleMessageFromServer(String msg) {
	 if (msg.substring(12).startsWith("#")) {
		 handleFunction(msg.substring(12));
	 } else {
		 this.sendToAllClients(msg);
		 console.display(msg);
	 }
	 
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    console.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    console.display
      ("Server has stopped listening for connections.");
  }
  
  @Override 
  protected void clientConnected(ConnectionToClient client) {
	  console.display("a new client is attempting to connect to the server");
  }
  
  @Override
  synchronized protected void clientDisconnected( ConnectionToClient client) {
	  sendToAllClients(client.getInfo("login_id") +" has disconnected.");
	  console.display(client.getInfo("login_id") +" has disconnected.");
  }
  
  @Override
  synchronized protected void clientException(
		    ConnectionToClient client, Throwable exception) {
	  sendToAllClients(client.getInfo("login_id") +" has disconnected.");
	  console.display(client.getInfo("login_id") +" has disconnected.");
  }
  
  private void handleFunction(String msg) {
	  
	  if (msg.equals("#quit")) {
		  
		  try{
			  this.close();
			  console.display("closed server");
		  } catch (IOException e) {}

		  System.exit(0);
		  
	  } else if (msg.equals("#stop")) {
		  
		  this.stopListening();
		  
	  } else if (msg.equals("#close")) {
		 
		  try{
			  this.close();
			  console.display("closed server");
		  } catch (IOException e) {}
		  
	  } else if (msg.startsWith("#setport")) {
		  
		  if (!this.isListening()) {
			  if (msg.length() >= 10) {
					this.setPort(Integer.parseInt(msg.substring(9))); 
					console.display("port set to : " + (Integer.parseInt(msg.substring(9))));
			  } else {
				  console.display(" not a valid host ");
				  
			  }
		  }
		  
	  } else if (msg.equals("#start")) {
		  
		  if (!this.isListening()) {
			 
			  try {
				  this.listen();
			  } catch (IOException e) {}
			  
		  } else {
			  console.display(" Server already started.");
		  }
		  
	  } else if (msg.equals("#getport")) {
		  
		  console.display(Integer.toString(this.getPort()));
	  
	  } else {
		  
		  console.display(" Unknown command ");
		  
	  }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }



}
//End of EchoServer class
