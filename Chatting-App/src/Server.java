import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class Server implements ActionListener {
    static JFrame f = new JFrame();
    JTextField text;
    JPanel x;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout = null;
    static Socket s = null;
    static ServerSocket serverSocket = null;
    static DataInputStream din = null;
    private boolean serverRunning = true;


    public Server() {
        f.setLayout(null);
        
       
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(8, 37, 103));
        
//        p1.setBackground(new Color(25, 25, 112));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

   
        ImageIcon i1 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/3.png");
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });
        p1.add(back);

       
        ImageIcon i4 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/girl.png");
        Image i5 = i4.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 40, 40);
        p1.add(profile);

   
        ImageIcon i7 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/v.png");
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(i8));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

 
        ImageIcon a = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/ph.png");
        Image b = a.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel call = new JLabel(new ImageIcon(b));
        call.setBounds(350, 20, 25, 25);
        p1.add(call);

        ImageIcon d = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/mr.png");
        Image e = d.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel more = new JLabel(new ImageIcon(e));
        more.setBounds(400, 20, 25, 25);
        p1.add(more);

   
        JLabel name = new JLabel("Jenifa");
        name.setBounds(90, 15, 100, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        p1.add(name);

        JLabel status = new JLabel("Online");
        status.setBounds(90, 35, 100, 20);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 12));
        p1.add(status);

       
        x = new JPanel();
        x.setBounds(5, 75, 440, 570);
        x.setLayout(new BorderLayout());
        vertical = Box.createVerticalBox();
        vertical.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        x.add(vertical, BorderLayout.PAGE_START);
        f.add(x);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        f.add(text);

    
        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(8, 37, 103));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);

      
        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
        startServer();
    }

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(1000);
                System.out.println("Server started on port 7000");

                while (serverRunning) {
                    try {
                        s = serverSocket.accept();
                        System.out.println("Client connected: " + s.getInetAddress());
                        
                        din = new DataInputStream(s.getInputStream());
                        dout = new DataOutputStream(s.getOutputStream());


                        while (true) {
                            try {
                                String msg = din.readUTF();
                                SwingUtilities.invokeLater(() -> {
                                    JPanel panel = formatLabel(msg);
                                    JPanel left = new JPanel(new BorderLayout());
                                    left.add(panel, BorderLayout.LINE_START);
                                    left.setMaximumSize(new Dimension(400, 60));
                                    vertical.add(left);
                                    vertical.add(Box.createVerticalStrut(3));
                                    f.revalidate();
                                    f.repaint();
                                });
                            } catch (IOException e) {
                                System.out.println("Client disconnected");
                                break;
                            }
                        }
                    } catch (SocketException e) {
                        if (serverRunning) {
                            System.out.println("Server socket closed");
                        }
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } finally {
                        closeConnection();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();
            if (out.trim().isEmpty()) return;

            if (dout != null) {
                try {
                    dout.writeUTF(out);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(f, "Client disconnected!");
                    dout = null;
                    return;
                }

                JPanel p2 = formatSentLabel(out); 
                JPanel right = new JPanel(new BorderLayout());
                right.add(p2, BorderLayout.LINE_END);
                right.setMaximumSize(new Dimension(400, 60));
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(3));

                text.setText("");
                f.revalidate();
                f.repaint();
            } else {
                JOptionPane.showMessageDialog(f, "No client connected yet!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    public static JPanel formatSentLabel(String out) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
    
        JLabel output = new JLabel("<html><p style=\"width:150px; margin:2px;\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 14));
        output.setBackground(new Color(0, 120, 215));
        output.setForeground(Color.WHITE); 
        output.setOpaque(true);
        output.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panel.add(output, BorderLayout.CENTER);
        
        // Time label
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(Calendar.getInstance().getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 10));
        time.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
        panel.add(time, BorderLayout.SOUTH);
        
        return panel;
    }

	public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
       
        JLabel output = new JLabel("<html><p style=\"width:150px; margin:2px;\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 14));
        output.setBackground(new Color(220, 220, 220)); 
        output.setForeground(Color.BLACK);
        output.setOpaque(true);
        output.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panel.add(output, BorderLayout.CENTER);
        
        // Time label
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(Calendar.getInstance().getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 10));
        time.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
        panel.add(time, BorderLayout.SOUTH);
        
        return panel;
    }
	 private static void closeConnection() {
	        try {
	            if (din != null) din.close();
	            if (dout != null) dout.close();
	            if (s != null && !s.isClosed()) s.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            din = null;
	            dout = null;
	            s = null;
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            Server server = new Server();
	            server.startServer();
	        });
	    }
	}