import java.util.Scanner;

import common.*;

public class ServerConsole implements ChatIF {
	
	Scanner fromConsole;
	EchoServer server;
	
	public ServerConsole(EchoServer server) {
		this.server = server;
		fromConsole = new Scanner(System.in);
		
	}
	
	public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }

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
	
	public static void main (String[] ARGS) {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(ARGS[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = 5555; //Set port to 5555
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
