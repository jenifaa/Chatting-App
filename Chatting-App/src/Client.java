import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import java.io.*;

public class Client implements ActionListener {
    JTextField text;
    JPanel x;
    static Box vertical = Box.createVerticalBox();
    JFrame f = new JFrame();
    static DataOutputStream dout;
    static Socket cl;
    static DataInputStream din;
    private boolean connecting = false;
    private boolean showConnectionError = true;

    public Client() {
        f.setLayout(null);

        // Header Panel
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(8, 37, 103));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        // Back button
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

        // Profile image
        ImageIcon i4 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/man.png");
        Image i5 = i4.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 40, 40);
        p1.add(profile);

        // Video icon
        ImageIcon i7 = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/vd.png");
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(i8));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        // Call icon
        ImageIcon a = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/ph.png");
        Image b = a.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel call = new JLabel(new ImageIcon(b));
        call.setBounds(350, 20, 25, 25);
        p1.add(call);

        // More options icon
        ImageIcon d = new ImageIcon("C:\\Users\\jefra\\git\\repository3\\Chatting-App\\src\\icons/mr.png");
        Image e = d.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel more = new JLabel(new ImageIcon(e));
        more.setBounds(400, 20, 25, 25);
        p1.add(more);

        // Name label
        JLabel name = new JLabel("Ashish");
        name.setBounds(90, 15, 100, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        p1.add(name);

        // Status label
        JLabel status = new JLabel("Online");
        status.setBounds(90, 35, 100, 20);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 12));
        p1.add(status);

        // Message area
        x = new JPanel();
        x.setBounds(5, 75, 440, 570);
        x.setLayout(new BorderLayout());
        vertical = Box.createVerticalBox();
        vertical.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        x.add(vertical, BorderLayout.PAGE_START);
        f.add(x);

        // Text input field
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        f.add(text);

        // Send button
        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(8, 37, 103));
        send.setForeground(Color.WHITE);
        send.setFocusPainted(false);
        send.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        send.addActionListener(this);
        f.add(send);

        // Frame settings
        f.setSize(450, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);

        startClient();
    }

   public void startClient() {
        if (connecting) return;
        
        connecting = true;
        new Thread(() -> {
            while (true) {
                try {
                    cl = new Socket();
                    cl.connect(new InetSocketAddress("127.0.0.1", 1000), 3000);
                    din = new DataInputStream(cl.getInputStream());
                    dout = new DataOutputStream(cl.getOutputStream());
                    
                    SwingUtilities.invokeLater(() -> {
                        if (showConnectionError) {
                            JOptionPane.showMessageDialog(f, "Connected to server!");
                            showConnectionError = true;
                        }
                    });

                    
                    while (!cl.isClosed()) {
                        String msg = din.readUTF();
                        SwingUtilities.invokeLater(() -> {
                            JPanel panel = formatLabel(msg, false);
                            JPanel messageContainer = new JPanel(new BorderLayout());
                            messageContainer.add(panel, BorderLayout.LINE_START);
                            messageContainer.setMaximumSize(new Dimension(400, 60));
                            vertical.add(messageContainer);
                            vertical.add(Box.createVerticalStrut(3));
                            f.revalidate();
                            f.repaint();
                        });
                    }
                } catch (SocketTimeoutException e) {
                    SwingUtilities.invokeLater(() -> {
                        if (showConnectionError) {
                            JOptionPane.showMessageDialog(f, "Connection timeout. Please check if server is running.");
                            showConnectionError = false;
                        }
                    });
                } catch (ConnectException e) {
                    SwingUtilities.invokeLater(() -> {
                        if (showConnectionError) {
                            JOptionPane.showMessageDialog(f, "Server not available. Please start the server first.");
                            showConnectionError = false;
                        }
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        if (showConnectionError) {
                            JOptionPane.showMessageDialog(f, "Connection error: " + e.getMessage());
                            showConnectionError = false;
                        }
                    });
                } finally {
                    closeConnection();
                    connecting = false;
                }

               
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText().trim();
            if (out.isEmpty()) return;

            if (dout != null) {
                try {
                    dout.writeUTF(out);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(f, "Server disconnected!");
                    dout = null;
                    return;
                }

                JPanel p2 = formatLabel(out, true);
                JPanel right = new JPanel(new BorderLayout());
                right.add(p2, BorderLayout.LINE_END);
                right.setMaximumSize(new Dimension(400, 60));
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(3));

                x.revalidate();
                x.repaint();
                text.setText("");
            } else {
                JOptionPane.showMessageDialog(f, "Server not connected yet!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out, boolean isSent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel output = new JLabel("<html><p style=\"width:150px; margin:2px;\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 14));
        output.setBackground(isSent ? new Color(0, 120, 215) : new Color(220, 220, 220));
        output.setForeground(isSent ? Color.WHITE : Color.BLACK);
        output.setOpaque(true);
        output.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panel.add(output, BorderLayout.CENTER);
        
    
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(Calendar.getInstance().getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 10));
        time.setBorder(BorderFactory.createEmptyBorder(0, 5, 2, 5));
        panel.add(time, BorderLayout.SOUTH);
        
        return panel;
    }

    private void closeConnection() {
        try {
            if (din != null) din.close();
            if (dout != null) dout.close();
            if (cl != null && !cl.isClosed()) cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            din = null;
            dout = null;
            cl = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}