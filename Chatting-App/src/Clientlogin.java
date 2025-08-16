import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.bson.Document;

public class Clientlogin implements ActionListener {
    JFrame frame = new JFrame();

    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JButton registerButton = new JButton("Register");

    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();

    JLabel userIDLabel = new JLabel("Username/Email:");
    JLabel userPasswordLabel = new JLabel("Password:");
    JLabel messageLabel = new JLabel();

    public Clientlogin() {
    	
        userIDLabel.setBounds(50, 100, 120, 25);
        userPasswordLabel.setBounds(50, 150, 120, 25);

        messageLabel.setBounds(50, 250, 350, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 18));

   
        userIDField.setBounds(180, 100, 200, 25);
        userPasswordField.setBounds(180, 150, 200, 25);

       
        loginButton.setBounds(80, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(200, 200, 100, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        registerButton.setBounds(150, 300, 120, 30);
        registerButton.setFocusable(false);
        registerButton.addActionListener(e -> {
            frame.dispose();
            new RegistrationPageClient(); 
        });

     
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(0, 50, 0));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        frame.add(p1);

        
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(50, 200, 330, 40); 
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); 
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(registerButton);
     
        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(buttonPanel); 

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 700);
        frame.setLocation(800,50);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
        }

        if (e.getSource() == loginButton) {
            String userID = userIDField.getText().trim();
            String password = new String(userPasswordField.getPassword());

            if (userID.isEmpty() || password.isEmpty()) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Please fill all fields");
                return;
            }

        
            Document user = DatabaseHelperClient.getUserByUserIDAndPassword(userID, password);
            if (user != null) {
//                String username = user.getString("username");
//                String email = user.getString("email");

                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login Successful!");

                frame.dispose();
                Client client = new Client();
                client.startClient();
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid username/email or password");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Clientlogin::new);
    }
}
