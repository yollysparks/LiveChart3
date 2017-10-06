/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import server.Server;

/**
 *
 * @author felesiah
 */
public class ServerGUI extends JFrame implements ActionListener, WindowListener{
    private static final long serialVersionUID = 1L;
	// the stop and start buttons
	private JButton stopStart;
	// JTextArea for the chat room and the events
	private JTextArea chat, event;
	// The port number
	private JTextField tPortNumber;
	// my server
	private Server server;
	
	
	// server constructor that receive the port to listen to for connection as parameter
	ServerGUI(int port) {
		super("Chat Server");
		server = null;
		// in the NorthPanel the PortNumber the Start and Stop buttons
		JPanel north = new JPanel();
		north.add(new JLabel("Port number: "));
		tPortNumber = new JTextField("  " + port);
		north.add(tPortNumber);
		// to stop or start the server, we start with "Start"
		stopStart = new JButton("Start");
                stopStart.addActionListener(this);
		north.add(stopStart);
		add(north, BorderLayout.NORTH);
		
		// the event and chat room
		JPanel center = new JPanel(new GridLayout(2,1));
		chat = new JTextArea(80,80);
		chat.setEditable(false);
		appendRoom("Chat room.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(80,80);
		event.setEditable(false);
		appendEvent("Events log.\n");
		center.add(new JScrollPane(event));	
		add(center);
		
		// need to be informed when the user click the close button on the frame
		addWindowListener(this);
		setSize(400, 600);
		setVisible(true);
	}		
      // append message to the two JTextArea
	// position at the end
	void appendRoom(String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);
	}
	void appendEvent(String str) {
		event.append(str);
		event.setCaretPosition(chat.getText().length() - 1);
		
	}
	
	// start or stop where clicked
    @Override
	public void actionPerformed(ActionEvent e) {
//		// if running we have to stop
//		if(server != null) {
//                    try {
//                        server.stop();
//                    } catch (IOException ex) {
//                        Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//			server = null;
//			tPortNumber.setEditable(true);
//			stopStart.setText("Start");
//			return;
//		}
//      	// OK start the server	
//		int port;
//                try {
//			port = Integer.parseInt(tPortNumber.getText().trim());
//		}
//		catch(Exception er) {
//			appendEvent("Invalid port number");
//			return;
//		}
//		// ceate a new Server
//		server = new Server(port, this);
////		// and start it as a thread
//		new ServerRunning().start();
//		stopStart.setText("Stop");
//		tPortNumber.setEditable(false);
//	}
//	
//	// entry point to start the Server
//	public static void main(String[] arg) {
//        // start server default port 1500
//        new ServerGUI(2222);
//	}
//
//	/*
//	 * If the user click the X button to close the application
//	 * I need to close the connection with the server to free the port
//	 */
//    @Override
//	public void windowClosing(WindowEvent e) {
//		// if my Server exist
//		if(server != null) {
//			try {
//				server.stop();			// ask the server to close the conection
//			}
//			catch(Exception eClose) {
//			}
//			server = null;
//		}
//		// dispose the frame
//		dispose();
//		System.exit(0);
//	}
//	// I can ignore the other WindowListener method
//    @Override
//	public void windowClosed(WindowEvent e) {}
//    @Override
//	public void windowOpened(WindowEvent e) {}
//    @Override
//	public void windowIconified(WindowEvent e) {}
//    @Override
//	public void windowDeiconified(WindowEvent e) {}
//    @Override
//	public void windowActivated(WindowEvent e) {}
//    @Override
//	public void windowDeactivated(WindowEvent e) {}
//
//	/*
//	 * A thread to run the Server
//	 */
//public	class ServerRunning extends Thread {
//                @Override
//		public void run() {
//                    try {
//                        server.start(2222);         // should execute until if fails
//                    } catch (IOException ex) {
//                        Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//			// the server failed
//			stopStart.setText("Start");
//			tPortNumber.setEditable(true);
//			appendEvent("Server crashed\n");
//			server = null;
//		}
	}

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
