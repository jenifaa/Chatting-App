import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import java.util.*;
import javax.swing.*;
import java.text.*;
import java.net.*;
import java.io.*;
public class Server  implements ActionListener{
	static JFrame f = new JFrame();
	JTextField text;
	JPanel x;
	static Box vertical = Box.createVerticalBox() ;
	static DataOutputStream dout;
	

	
	Server(String userID){
		f.setLayout(null);
		JPanel p1 = new JPanel();
//		p1.setBackground(new Color(7,94,84));
		p1.setBackground(new Color(0, 50, 0));
		p1.setBounds(0,0,450,70);
		p1.setLayout(null);
		f.add(p1);
		
		
		//arrow image
		
//		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/girl.png"));
		ImageIcon i1 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/3.png");
		Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		
		JLabel back = new JLabel(i3);
		
		back.setBounds(5,20,25,25);
		p1.add(back);
		
		back.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ae) {
				System.exit(0);
			}
		});
		
		//profile img
		ImageIcon i4 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/girl.png");
		Image i5 = i4.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
		ImageIcon i6 = new ImageIcon(i5);
		
		JLabel profile = new JLabel(i6);
		
		profile.setBounds(40,10,40,40);
		p1.add(profile);
		
		ImageIcon i7 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/v.png");
		Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		ImageIcon i9 = new ImageIcon(i8);
		
		JLabel video = new JLabel(i9);
		
		video.setBounds(300,20,30,30);
		p1.add(video);
		
		ImageIcon a = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/ph.png");
		Image b = a.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		ImageIcon c = new ImageIcon(b);
		
		JLabel call = new JLabel(c);
		
		call.setBounds(350,20,25,25);
		p1.add(call);
		
		
		//more icon
		
		ImageIcon d = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/more2.png");
		Image e = d.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
		ImageIcon m = new ImageIcon(e);
		
		JLabel more = new JLabel(m);
		
		more.setBounds(400,20,10,25);
		p1.add(more);
		
		
		
		
		//user name
		
		JLabel name = new JLabel(userID);
		name.setBounds(90,15,100,20);
		name.setForeground(Color.WHITE);
		name.setFont(new Font("SAN_SERIF",Font.BOLD,16));
		p1.add(name);
		
		
		//status
		
		JLabel status = new JLabel("Online");
		status.setBounds(90,35,100,20);
		status.setForeground(Color.WHITE);
		status.setFont(new Font("SAN_SERIF",Font.BOLD,12));
		p1.add(status);
		
		
		//panel for chat
		
		 x = new JPanel();
		x.setBounds(5,75,440,570);
		f.add(x);
		
		//making textField
		
		 text =  new JTextField();
		text.setBounds(5,655,310,40);
		text.setFont(new Font("SAN_SERIF",Font.BOLD,16));
		f.add(text);
		
		// send button
		
		JButton send = new JButton("Send");
		send.setBounds(320,655,123,40);
		send.setBackground(new Color(0, 50, 0));
		send.setForeground(Color.WHITE);
		send.addActionListener(this);
		
		
		
		
		 
	       
	        f.add(send);
		
		
		//send button work function
		
		
		
		
		f.setSize(450,700);
		
		f.setLocation(200,50);
		f.setUndecorated(true);
		f.getContentPane().setBackground(Color.WHITE);
//		getContentPane().setBackground(Color.BLACK);
		f.setVisible(true);
	}
	
	
	
 
	public void startServer() {
		  new Thread(() -> {
	            try {
	                ServerSocket skt = new ServerSocket(6002);
	                while (true) {
	                    Socket s = skt.accept();
	                    DataInputStream din = new DataInputStream(s.getInputStream());
	                    dout = new DataOutputStream(s.getOutputStream());

	                    // Enable send button when client connects
//	                    SwingUtilities.invokeLater(() -> server.send.setEnabled(true));

	                    while (true) {
	                        String msg = din.readUTF();
	                        JPanel panel = formatLabel(msg);
	                        JPanel left = new JPanel(new BorderLayout());
	                        left.add(panel, BorderLayout.LINE_START);
	                        vertical.add(left);
	                        f.validate();
	                    }
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }).start();
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		try {
			
			   
			   
			String out = text.getText();
			JLabel output = new JLabel(out);
			JPanel p2 =  formatLabel(out);
//			p2.add(output);
			x.setLayout(new BorderLayout());
			JPanel right = new JPanel(new BorderLayout());
			
			right.add(p2,BorderLayout.LINE_END);
			
			
			vertical.add(right);
			vertical.add(Box.createVerticalStrut(15));
			x.add(vertical,BorderLayout.PAGE_START);
			dout.writeUTF(out);
			text.setText("");
			f.repaint();
			f.invalidate();
			f.validate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		}
	
	public static JPanel formatLabel(String out) {
		JPanel panel = new JPanel();
		
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		JLabel output = new JLabel("<html><p style=\"width:150px   \">" + out + "</p></html>");
		
		output.setFont(new Font("Tahoma", Font.PLAIN,16));
		output.setBackground(Color.BLACK);
		output.setForeground(Color.WHITE);
		output.setOpaque(true);
		output.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 50)); 
		
		
		panel.add(output);
		
		Calendar cal = Calendar.getInstance() ;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		JLabel time = new JLabel();
		time.setText(sdf.format(cal.getTime()));
		panel.add(time);		
//	    panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		return panel;
		
	}
	
	
//	
//public static void main(String[] args) {
//	new Server();
//	try{
//		ServerSocket skt = new ServerSocket(6001);
//		while(true){
//		Socket s =	skt.accept();
//		DataInputStream din = new DataInputStream(s.getInputStream());
//		 dout = new DataOutputStream(s.getOutputStream());
//		
//		while(true) {
//			String msg = din.readUTF();
//			JPanel panel = formatLabel(msg);
//			JPanel left = new JPanel(new BorderLayout());
//			left.add(panel,BorderLayout.LINE_START);
//			vertical.add(left);
//			f.validate();
//		}
//	}
//	}
//	catch(Exception e){
//		e.printStackTrace();
//	}
//}
	
	
	 public static void main(String[] args) {
	        Server server = new Server(null);

	        server.startServer();
	    }
	
}
