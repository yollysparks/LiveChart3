/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca2;

import java.io.Serializable;

/**
 *
 * @author felesiah
 */
public class MessageChat implements Serializable {
    protected static final long serialVersionUID = 1112122200L;

   
    // The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
        public  static String WHOISIN = null,MESSAGE = " ", LOGOUT = " ";
	private String type;
	private String message;
	
	 public MessageChat(String type, String message) {
		this.type = type;
		this.message = message;
	}


	public String getType() {
		return type;
	}
	public String getMessage() {
		return message;
	}
}
