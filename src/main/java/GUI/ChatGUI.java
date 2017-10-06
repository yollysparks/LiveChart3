/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author marcofrydshou1
 */
public class ChatGUI implements Runnable{
    
    private String name;
    private Socket socket;
    private PrintWriter out;

    public ChatGUI(String ip, int port, String name) throws IOException {
        
        this.name = name;
        this.socket = new Socket (ip, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
     logIn();
         
    }
    
    public void logIn(){
        out.println("LOGIN:" + name);
    }
    
           
    public void sendMessage(String message){
        out.println("MSG:" + message);
        
    }
    
    
    public void logOut(){
        out.println("LOGOUT:");
    }
    
}
