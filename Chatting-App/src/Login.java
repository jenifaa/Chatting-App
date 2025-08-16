import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Random;

public class Login implements ActionListener {
    JFrame frame = new JFrame();

    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JButton registerButton = new JButton("Register"); 
    JButton forgotPasswordButton = new JButton("Forgot Password? Reset");

    JTextField emailField = new JTextField(); 
    JPasswordField userPasswordField = new JPasswordField();
    JLabel emailLabel = new JLabel("Email:");
    JLabel userPasswordLabel = new JLabel("Password:");
    JLabel messageLabel = new JLabel();
    JLabel titleLabel = new JLabel("Login to your account", SwingConstants.CENTER);

 
    private String resetEmail;
    private String resetCode;
    private JDialog resetDialog;
    private JTextField codeField;
    private JPasswordField newPasswordField;

  
    private final Color LIGHT_BLUE_BG = new Color(248, 250, 255);
    private final Color RICH_BLUE = new Color(0, 82, 165);
    private final Color DARK_BLUE = new Color(0, 51, 102);

    Login() {
      
        frame.getContentPane().setBackground(LIGHT_BLUE_BG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 700);
        frame.setLocation(200, 50);
        frame.setLayout(null);

 
        titleLabel.setBounds(0, 40, 450, 30);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        titleLabel.setForeground(DARK_BLUE);
        frame.add(titleLabel);

       
        emailLabel.setBounds(50, 100, 75, 25);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(DARK_BLUE);
        
        emailField.setBounds(125, 100, 250, 30);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));


        userPasswordLabel.setBounds(50, 150, 75, 25);
        userPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userPasswordLabel.setForeground(DARK_BLUE);
        
        userPasswordField.setBounds(125, 150, 250, 30);
        userPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

 
        messageLabel.setBounds(100, 250, 250, 35);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        styleButton(loginButton, 50, 200, 100, 30);
        styleButton(resetButton, 160, 200, 100, 30);
        styleButton(registerButton, 270, 200, 100, 30);

        // Forgot password 
        forgotPasswordButton.setBounds(125, 240, 200, 25);
        forgotPasswordButton.setFocusable(false);
        forgotPasswordButton.addActionListener(this);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(RICH_BLUE);
        forgotPasswordButton.setFont(new Font("Arial", Font.BOLD, 12));

        frame.add(emailLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(emailField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(registerButton);
        frame.add(forgotPasswordButton);

        frame.setVisible(true);
    }

    private void styleButton(JButton button, int x, int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setFocusable(false);
        button.setBackground(RICH_BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.addActionListener(this);
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
                messageLabel.setForeground(DARK_BLUE);
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

        if (e.getSource() == forgotPasswordButton) {
            handleForgotPassword();
        }
    }

    private void handleForgotPassword() {
        resetEmail = JOptionPane.showInputDialog(frame, 
            "Enter your email to reset password:", 
            "Forgot Password", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (resetEmail != null && !resetEmail.trim().isEmpty()) {
            resetCode = String.format("%06d", new Random().nextInt(999999));
            System.out.println("Reset code for " + resetEmail + ": " + resetCode);
            createResetDialog();
        }
    }

    private void createResetDialog() {
        resetDialog = new JDialog(frame, "Reset Password", true);
        resetDialog.setLayout(new GridLayout(4, 2, 5, 5));
        resetDialog.setSize(350, 200);
        resetDialog.setLocationRelativeTo(frame);
        resetDialog.getContentPane().setBackground(LIGHT_BLUE_BG);

        JLabel codeLabel = new JLabel("Verification Code:");
        codeLabel.setForeground(DARK_BLUE);
        resetDialog.add(codeLabel);
        
        codeField = new JTextField();
        resetDialog.add(codeField);

        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setForeground(DARK_BLUE);
        resetDialog.add(newPassLabel);
        
        newPasswordField = new JPasswordField();
        resetDialog.add(newPasswordField);

        JButton verifyButton = new JButton("Verify Code");
        styleButton(verifyButton);
        verifyButton.addActionListener(e -> verifyResetCode());
        resetDialog.add(verifyButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(DARK_BLUE);
        cancelButton.addActionListener(e -> resetDialog.dispose());
        resetDialog.add(cancelButton);

        resetDialog.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(RICH_BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void verifyResetCode() {
        String enteredCode = codeField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());

        if (enteredCode.equals(resetCode)) {
            try {
                String connectionString = DatabaseHelper.getConnectionString();
                try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                    MongoDatabase database = mongoClient.getDatabase("Chat");
                    MongoCollection<Document> collection = database.getCollection("users");

                    collection.updateOne(
                        new Document("email", resetEmail),
                        new Document("$set", new Document("password", newPassword))
                    );

                    JOptionPane.showMessageDialog(resetDialog, 
                        "Password reset successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    resetDialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(resetDialog, 
                    "Error resetting password", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(resetDialog, 
                "Invalid verification code", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(frame, "Database connection error!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}