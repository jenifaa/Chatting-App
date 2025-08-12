//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.net.*;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//public class Phone implements ActionListener {
//
//    private JFrame frame;
//    private JPanel chatPanel;       // panel holding chat messages vertically
//    private JTextField textField;
//    private DataOutputStream dout;
//    private JLabel nameLabel;
//
//    public Phone(String username) {
//        frame = new JFrame("Phone Chat Server");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(450, 700);
//        frame.setLocationRelativeTo(null);
//        frame.setUndecorated(true);
//
//        // Load phone frame background image (phone shell)
//        ImageIcon phoneIcon = new ImageIcon("C:/Users/jefra/git/repository3/Chatting-App/src/icons/mb.jpg");
//        Image phoneImage = phoneIcon.getImage();
//
//        // Custom panel that paints the phone background image
//        JPanel phoneBackgroundPanel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                // scale image to panel size
//                g.drawImage(phoneImage, 0, 0, getWidth(), getHeight(), this);
//            }
//        };
//        phoneBackgroundPanel.setLayout(null);  // absolute layout
//
//        // Header panel (top bar)
//        JPanel headerPanel = new JPanel();
//        headerPanel.setLayout(null);
//        headerPanel.setBackground(new Color(0, 0, 0, 180)); // semi-transparent black
//        headerPanel.setBounds(10, 50, 420, 70);
//
//        // Back arrow icon
//        ImageIcon backIcon = new ImageIcon("C:/Users/jefra/git/repository3/Chatting-App/src/icons/3.png");
//        Image backImg = backIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
//        JLabel backLabel = new JLabel(new ImageIcon(backImg));
//        backLabel.setBounds(5, 20, 25, 25);
//        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        backLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.exit(0);
//            }
//        });
//        headerPanel.add(backLabel);
//
//        // Profile icon
//        ImageIcon profileIcon = new ImageIcon("C:/Users/jefra/git/repository3/Chatting-App/src/icons/girl.png");
//        Image profileImg = profileIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//        JLabel profileLabel = new JLabel(new ImageIcon(profileImg));
//        profileLabel.setBounds(40, 10, 40, 40);
//        headerPanel.add(profileLabel);
//
//        // Video icon
//        ImageIcon videoIcon = new ImageIcon("C:/Users/jefra/git/repository3/Chatting-App/src/icons/v.png");
//        Image videoImg = videoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
//        JLabel videoLabel = new JLabel(new ImageIcon(videoImg));
//        videoLabel.setBounds(300, 20, 30, 30);
//        headerPanel.add(videoLabel);
//
//        // Call icon
//        ImageIcon callIcon = new ImageIcon("C:/Users/jefra/git/repository3/Chatting-App/src/icons/ph.png");
//        Image callImg = callIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
//        JLabel callLabel = new JLabel(new ImageIcon(callImg));
//        callLabel.setBounds(350, 20, 25, 25);
//        headerPanel.add(callLabel);
//
//        // More icon
//        ImageIcon moreIcon = new ImageIcon("C:/Users/jefra/git/repository3/Chatting-App/src/icons/more2.png");
//        Image moreImg = moreIcon.getImage().getScaledInstance(10, 25, Image.SCALE_SMOOTH);
//        JLabel moreLabel = new JLabel(new ImageIcon(moreImg));
//        moreLabel.setBounds(400, 20, 10, 25);
//        headerPanel.add(moreLabel);
//
//        // User name
//        nameLabel = new JLabel(username);
//        nameLabel.setBounds(90, 15, 100, 20);
//        nameLabel.setForeground(Color.WHITE);
//        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
//        headerPanel.add(nameLabel);
//
//        // Status
//        JLabel statusLabel = new JLabel("Online");
//        statusLabel.setBounds(90, 35, 100, 20);
//        statusLabel.setForeground(Color.GREEN.brighter());
//        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
//        headerPanel.add(statusLabel);
//
//        phoneBackgroundPanel.add(headerPanel);
//
//        // Chat message area with scrolling
//        chatPanel = new JPanel();
//        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
//        chatPanel.setOpaque(false); // transparent to show phone bg
//
//        JScrollPane chatScroll = new JScrollPane(chatPanel);
//        chatScroll.setBounds(5, 125, 420, 470);
//        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        chatScroll.setOpaque(false);
//        chatScroll.getViewport().setOpaque(false);
//        chatScroll.setBorder(null);
//
//        phoneBackgroundPanel.add(chatScroll);
//
//        // Text input field
//        textField = new JTextField();
//        textField.setBounds(25, 605, 310, 40);
//        textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
//        phoneBackgroundPanel.add(textField);
//
//        // Send button
//        JButton sendBtn = new JButton("Send");
//        sendBtn.setBounds(340, 600, 100, 40);
//        sendBtn.setBackground(Color.BLACK);
//        sendBtn.setForeground(Color.WHITE);
//        sendBtn.addActionListener(this);
//        phoneBackgroundPanel.add(sendBtn);
//
//        frame.setContentPane(phoneBackgroundPanel);
//        frame.setVisible(true);
//
//        // Start server thread to accept connections and read messages
//        new Thread(this::startServer).start();
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String message = textField.getText().trim();
//        if (!message.isEmpty()) {
//            addMessage(message, true);
//            textField.setText("");
//            if (dout != null) {
//                try {
//                    dout.writeUTF(message);
//                    dout.flush();
//                    System.out.println("Sent: " + message);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            } else {
//                System.out.println("Output stream is null. Client not connected?");
//            }
//        }
//    }
//
//    private void addMessage(String msg, boolean isSent) {
//        JPanel messagePanel = formatMessage(msg, isSent);
//
//        JPanel wrapper = new JPanel(new BorderLayout());
//        wrapper.setOpaque(false);
//        if (isSent) {
//            wrapper.add(messagePanel, BorderLayout.LINE_END);
//        } else {
//            wrapper.add(messagePanel, BorderLayout.LINE_START);
//        }
//
//        chatPanel.add(wrapper);
//        // Add extra vertical spacing only after received messages
//        if (!isSent) {
//            chatPanel.add(Box.createVerticalStrut(20)); // more space below received msg
//        } else {
//            chatPanel.add(Box.createVerticalStrut(10)); // normal space below sent msg
//        }
//        chatPanel.revalidate();
//
//        // Scroll to bottom after adding message
//        SwingUtilities.invokeLater(() -> {
//            JScrollBar vertical = ((JScrollPane) chatPanel.getParent().getParent()).getVerticalScrollBar();
//            vertical.setValue(vertical.getMaximum());
//        });
//    }
//
//
//    private JPanel formatMessage(String msg, boolean isSent) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setOpaque(false);
//
//        JLabel messageLabel = new JLabel("<html><p style=\"width:150px\">" + msg + "</p></html>");
//        messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
//        messageLabel.setOpaque(true);
//        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        if (isSent) {
//            messageLabel.setBackground(new Color(0, 132, 255)); // blue bubble
//            messageLabel.setForeground(Color.WHITE);
//            messageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
//        } else {
//            messageLabel.setBackground(new Color(220, 220, 220)); // light gray bubble
//            messageLabel.setForeground(Color.BLACK);
//            messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        }
//
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        JLabel timeLabel = new JLabel(sdf.format(cal.getTime()));
//        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
//
//        if (isSent) {
//            timeLabel.setForeground(Color.WHITE);
//            timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
//        } else {
//            timeLabel.setForeground(Color.DARK_GRAY);
//            timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        }
//
//        panel.add(messageLabel);
//        panel.add(timeLabel);
//
//        return panel;
//    }
//
//    private void startServer() {
//        try (ServerSocket serverSocket = new ServerSocket(6001)) {
//            System.out.println("Server started, waiting for clients...");
//            Socket socket = serverSocket.accept();
//            System.out.println("Client connected.");
//
//            DataInputStream din = new DataInputStream(socket.getInputStream());
//            dout = new DataOutputStream(socket.getOutputStream());
//
//            while (true) {
//                String msg = din.readUTF();
//                SwingUtilities.invokeLater(() -> addMessage(msg, false));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(Phone::new);
//    }
//}



public class Phone {

	 
	 
 }
