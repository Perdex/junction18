import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.Dimension;

public class TentacleController2{

	static IO io;
	public static void main(String[] args){
		//io = new IO("localhost", 5005);
		io = new IO("10.42.0.167", 5005);

		JFrame frame = new JFrame("SO MANY TENTACLES!!!");

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(360, 360));

		Listener listener = new Listener();
		panel.addMouseListener(listener);
		panel.addMouseMotionListener(listener);
		panel.setFocusable(true);

		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	static int clamp180(int val){
		val /= 2;
		val = 180 - val;
		return val < 0 ? 0 : val > 180 ? 180 : val;
	}

	static void sendMessage(MouseEvent e){

		int valh = clamp180(e.getX());
		int valv = clamp180(e.getY());

		String valhS = Integer.toString(valh);
		String valvS = Integer.toString(valv);
		//add padding to 3 digits
		valhS = ("000" + valhS).substring(valhS.length());
		valvS = ("000" + valvS).substring(valvS.length());

		int axis = SwingUtilities.isLeftMouseButton(e) ? 0 : 2;
		
		io.sendMessage(axis+valhS);
		io.sendMessage((axis + 1)+valvS);
	}

	private static class Listener extends MouseAdapter implements MouseMotionListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			sendMessage(e);
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			sendMessage(e);
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	private static class IO{
		private Socket clientSocket;
		private PrintWriter out;
		//private BufferedReader in;
	 
		IO(String ip, int port) {
			try{
				clientSocket = new Socket(ip, port);
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				//in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			}catch(Exception e){
				System.err.println(e);
			}
		}
	 
		public void sendMessage(String msg) {
			try{
				System.out.println("Sending: " + msg);
				out.println(msg);
				//String resp = in.readLine();
				//return resp;
			}catch(Exception e){
				System.err.println("At sendmessage: " + e);
			}
		}
	 
		public void stopConnection() {
			try{
				//in.close();
				out.close();
				clientSocket.close();
			}catch(Exception e){
				System.err.println(e);
			}
		}
	}
}


