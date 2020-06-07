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
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String loginID) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;

    openConnection(); 
    sendToServer("#login "+loginID);
    System.out.println(loginID + " has logged on.");
  }

  
  //Instance methods ************************************************
    
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
    try
    {
      if (message.startsWith("#quit")){
          sendToServer("disconnected");
          quit();

      } else if (message.startsWith("#logoff")){
          sendToServer("disconnected");
          closeConnection();

      } else if (message.startsWith("#sethost")){

          if (!isConnected()){
            String [] s = message.split(" ");
            setHost(s[1].trim());
          } 
          else
            clientUI.display("You are still connected to the host, please disconnect before setting up a new host");

      } else if (message.startsWith("#setport")){
        if (!isConnected()){
            String [] s = message.split(" ");
            setPort(Integer.parseInt(s[1].trim()));
          } 
          else
            clientUI.display("You are still connected to the host, please disconnect before setting up a new port");
        
      } else if (message.equals("#login")){
          if (!isConnected()){
              openConnection();
          }
          else
            clientUI.display("You are still connected to the host, please disconnect before setting up new connection");

      } else if (message.startsWith("#gethost")){
          clientUI.display("The current host name is: " + getHost());
        
      } else if (message.startsWith("#getport")){
          clientUI.display("The current port number is: " + getPort());
      } else{// default
        sendToServer(message);
      }


    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
    // override abstract class method
    protected void connectionClosed(){
        System.out.println("The connection is closed");
    }
    
    // override abstract class method
    protected void connectionException(Exception exception){
        // this block of code will execute once there is an exception thrown (due to not receiving message from server)
        // By calling the quiz method below, it stimulates the connectionClosed method above
        quit();
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
}
//End of ChatClient class
