import java.util.Scanner;

import common.*;


/**
 * This class constructs the UI for a chat Server.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * I (Jacob Fodale) wrote a trivial amount of this code.
 * 
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */

public class ServerConsole implements ChatIF {
	
	Scanner fromConsole;
	EchoServer server;
	final public static int DEFAULT_PORT = 5555;
	
	//Constructors ****************************************************
	  
	  /**
	   * Constructs an instance of the ConsoleServer.
	   *
	   * @param server This console's back-end.
	   */
	public ServerConsole(EchoServer server) {
		this.server = server;
		fromConsole = new Scanner(System.in);
		
	}
	
	//Instance methods ************************************************
	  
	/**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	
	/**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	public void accept() 
	  {
		try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServer("SERVER MSG> "+ message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	
	 //Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of the Client UI.
	   *
	   * @param args[0] The port of the server .
	   * 
	   */
	public static void main (String[] ARGS) {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(ARGS[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    EchoServer sv = new EchoServer(port);
	    ServerConsole serverChat = new ServerConsole(sv);
	    sv.setConsole (serverChat);
	    
	    
	    try 
	    {
	      sv.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	    
	    serverChat.accept();
		
		
	}
		
	
}
