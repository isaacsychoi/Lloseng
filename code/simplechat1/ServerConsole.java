import java.io.*;
import common.*;

public class ServerConsole implements ChatIF{


	public final static int DEFAULT_PORT = 5555;

	EchoServer server;


	public ServerConsole (int port){

		this.server = new EchoServer(port, this);


		try{
			this.server.listen();
		} catch (Exception ex){
			System.out.println("ERROR - Could not listen for clients!");
		}

		accept();

	}

	public void accept(){
    	try{
	      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
	      String message;

	      while (true){
	        message = fromConsole.readLine();
		    this.server.handleMessageFromServerUI(message);
		    
	      }// while-loop
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	 }


	public void display(String message) {
    	System.out.println("SERVER MSG> " + message);
  	}


  	public static void main(String[] args){
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    ServerConsole sc = new ServerConsole(port);
	    
  }
}