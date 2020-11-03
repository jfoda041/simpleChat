import java.util.Scanner;

import common.*;

public class ServerConsole implements ChatIF {
	
	Scanner fromConsole;
	EchoServer server;
	
	public ServerConsole(EchoServer server) {
		this.server = server;
		accept();
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
		
	
}
