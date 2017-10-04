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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author felesiah
 */
public class Server {
       private static String IP = "localhost";
       private int port;
	private ServerGUI sg;
        private SimpleDateFormat sdf;
       private boolean keepGoing;
   
    private ServerSocket serverSocket;
    private final ExecutorService es = Executors.newCachedThreadPool();
    private static List<EchoClientHandler> clients = new ArrayList();
  
    public Server() {
        this.port = 0;
	}
    public Server(int port, ServerGUI sg) {
		// GUI or not
		this.sg = sg;
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
	}
    
    
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
//       serverSocket.bind(new InetSocketAddress(IP, port));Important blocking call until it gets the connection.
        while (true) {
            System.out.println("Waiting for clients");
            EchoClientHandler client = new EchoClientHandler(serverSocket.accept());
            clients.add(client);
            es.execute(client);
            System.out.println("New client connected");
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
    private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		if(sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}
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
		for(int i = clients.size(); --i >= 0;) {
			EchoClientHandler ech = clients.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ech.writeMsg(messageLf)) {
				clients.remove(i);
				display("Disconnected Client " + ech.username + " removed from list.");
			}
		}
	}

    public static void main(String[] args) throws IOException {
        Server ems = new Server();
        ems.start(2222);
    }

   

    private static class EchoClientHandler implements Runnable {

        private Socket clientSocket;
        private PrintWriter output;
        private Scanner input;
        private String username;
        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        private boolean writeMsg(String msg) {
			// if Client is still connected send the message to it
			if(!clientSocket.isConnected()) {
                            return false;
			}
            // write the message to the stream
            output.print(msg); // if an error occurs, do not abort just inform the user
			return true;
		}

       
        
        @Override
        public void run() {      
            try {
                output = new PrintWriter(clientSocket.getOutputStream(), true);
                input = new Scanner(clientSocket.getInputStream());

                String inputLine;
                String messageToClients = "";
                while ((inputLine = input.nextLine()) != null) {
                    if (inputLine.equals("STOP")) {
                        break;
                    }
                    String[] parts = inputLine.split(":");
                    switch (parts[0]) {
                        case "LOGIN":
                            messageToClients = parts[1].toUpperCase();
                            String message="";
                            System.out.println( username + ": " + message);
                            break;
                        case "MSG":
                            messageToClients = parts[1].toLowerCase();
                            break;
                        case "LOGOUT":
                           
                            
                            break;
                        default: {
                            messageToClients = inputLine;
                            break;
                        }
                    }

                    for (EchoClientHandler client : clients) {
                        if (client != this) {
                            if (!messageToClients.equals("")) {
                                client.output.println(messageToClients);
                            }
                        }
                    }
                }

                input.close();
                output.close();
                clientSocket.close();
                System.out.println("SHUTTING DOWN A CLIENT");
            } catch (IOException e) {

            }
        }

    }  
}





