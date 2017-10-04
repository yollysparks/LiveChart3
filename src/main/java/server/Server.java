/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import ca2.MessageChat;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author felesiah
 */
public class Server {
    // a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	private List<ClientThread> al;
	// if I am in a GUI
	private ServerGUI sg;
	// to display time
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the Server
	private boolean startingUp;
	private final ExecutorService es = Executors.newCachedThreadPool();

	/*
	 *  Server constructor that receive the port to listen to for connection as parameter
	 *  in console
	 */
	public Server(int port) {
		this(port, null);
	}

       public Server(int port, ServerGUI sg) {
		// GUI or not
		this.sg = sg;
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		al = new ArrayList<ClientThread>() {};
	}
       
       public void start() {
		startingUp = true;
		/* create socket Server and wait for connection requests */
		try 
		{
			// the socket used by the Server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(startingUp) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				
				ClientThread t = new ClientThread(serverSocket.accept());  	// accept connection
			        al.add(t);
                                es.execute(t);
                                System.out.println("New client connected");
// if I was asked to stop
				if(!startingUp)
					break;
//				ClientThread t = new ClientThread(socket);  // make a thread of it
				al.add(t);									// save it in the ArrayList
				t.start();
                               
			}
                        
                        // I was asked to stop
			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
                catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}		
    /*
     * For the GUI to stop the Server
     */
	protected void stop() {
		startingUp = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
                    Socket socket = new Socket("localhost", port);//i added Socket socket
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
        private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		if(sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}
	/*
	 *  to broadcast a message to all Clients
	 */
	private synchronized void broadcast(String message) {
		// add HH:mm:ss and \n to the message
		String time = sdf.format(new Date());
		String messageLf = time + " " + message + "\n";
		// display message on console or GUI
		if(sg == null)
			System.out.print(messageLf);
		else
			sg.appendRoom(messageLf);     // append in the room window
		
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
               for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(messageLf)) {
				al.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			// found it
			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
            }
	
	/*
	 *  To run as a console application just open a console window and: 
	 * > java Server
	 * > java Server portNumber
	 * If the port number is not specified 1500 is used
	 */
        public static void main(String[] args) {
		// start Server on port 1500 unless a PortNumber is specified 
		int portNumber = 2222;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
                // create a Server object and start it
		Server sv = new Server(portNumber);
		sv.start();
	}

	/** One instance of this thread will run for each client */
public	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		 private Scanner sInput;
		 private PrintWriter sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
		// the only type of message a will receive
		MessageChat cm;
		// the date I connect
		String date;

		
	public	ClientThread(Socket socket) {
			// a unique id
			id = ++uniqueId;
                        id = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// create output first
				sOutput = new PrintWriter(socket.getOutputStream());
				sInput  = new Scanner(socket.getInputStream());
				// read the username
				username = sInput.nextLine();
				display(username + " just connected.");
			}catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
                    // have to catch ClassNotFoundException
                    // but I read a String, I am sure it will work
            date = new Date().toString() + "\n";
		}

		// what will run forever
                @Override
		public void run() {
                    try {
                sOutput = new PrintWriter(socket.getOutputStream(), true);
                sInput = new Scanner(socket.getInputStream());

                String inputLine;
                String messageToClients ="";
                while ((inputLine = sInput.nextLine()) != null) {// to loop until LOGOUT
                    if (inputLine.equals("STOP")) {
                        break;
                    }
   // Switch on the type of message receive
                    String[] type = cm.getType().split(":");
				switch(type[0]) {

                                    case "MESSAGE":
                                        messageToClients = type[1];
                                        String message= MessageChat.MESSAGE; 
					broadcast(username + ": " + message);
					break;
				    case "LOGOUT":
                                        messageToClients = type[1];
					display(username + " disconnected with a LOGOUT message.");
			                startingUp = false;
					break;
				    case "WHOISIN":
                                        messageToClients = type[1];
					writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
					// scan al the users connected
					for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
						writeMsg((i+1) + ") " + ct.username + " since " + ct.date);
					}
					break;
				}
			}
			// remove myself from the arrayList containing the list of the
			// connected Clients
			remove(id);
			close();
		}catch (IOException e) {         
           }
     }
		
		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		/*
		 * Write a String to the Client output stream
		 */
		private boolean writeMsg(String msg) {
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
                    // write the message to the stream
                    sOutput.print(msg); // if an error occurs, do not abort just inform the user
			return true;
		}
	}
}





