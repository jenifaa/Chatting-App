import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Login implements ActionListener {
    JFrame frame = new JFrame();

    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JButton registerButton = new JButton("Register"); 

    JTextField emailField = new JTextField(); 
    JPasswordField userPasswordField = new JPasswordField();
    JLabel emailLabel = new JLabel("Email:");
    JLabel userPasswordLabel = new JLabel("Password:");
    JLabel messageLabel = new JLabel();

    Login() {
        emailLabel.setBounds(50, 100, 75, 25);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        messageLabel.setBounds(100, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 20));

        emailField.setBounds(125, 100, 200, 25);
        userPasswordField.setBounds(125, 150, 200, 25);

        loginButton.setBounds(50, 200, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(160, 200, 100, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        registerButton.setBounds(270, 200, 100, 25); 
        registerButton.setFocusable(false);
        registerButton.addActionListener(this);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(0, 50, 0));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        frame.add(p1);

        frame.add(emailLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(emailField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(registerButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 700);
        frame.setLocation(200, 50);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            emailField.setText("");
            userPasswordField.setText("");
        }

        if (e.getSource() == loginButton) {
            String email = emailField.getText().trim();
            String password = new String(userPasswordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Please fill all fields");
                return;
            }

           
            String username = getUserNameByEmail(email, password);
            if (username != null) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login Successful");
                frame.dispose();
              
                Server server = new Server();
                server.startServer();  

              
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Email or Password incorrect");
            }
        }

        if (e.getSource() == registerButton) {
            frame.dispose(); 
            new RegisterPage();  
        }
    }

    private String getUserNameByEmail(String email, String password) {
        try {
            String connectionString = DatabaseHelper.getConnectionString();
            try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                MongoDatabase database = mongoClient.getDatabase("Chat");
                MongoCollection<Document> collection = database.getCollection("users"); 

                Document user = collection.find(new Document("email", email)
                        .append("password", password)).first();

                if (user != null) {
                    return user.getString("username"); 
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database connection error!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
