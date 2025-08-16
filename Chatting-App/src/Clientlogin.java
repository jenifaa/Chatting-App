import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.bson.Document;
import java.util.Random;

public class Clientlogin implements ActionListener {
    JFrame frame = new JFrame();

    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JButton registerButton = new JButton("Register");
    JButton forgotPasswordButton = new JButton("Forgot Password?");

    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();

    JLabel userIDLabel = new JLabel("Username/Email:");
    JLabel userPasswordLabel = new JLabel("Password:");
    JLabel messageLabel = new JLabel();
    JLabel titleLabel = new JLabel("Login to your account", SwingConstants.CENTER);

  
    private String resetUserID;
    private String resetCode;
    private JDialog resetDialog;
    private JTextField codeField;
    private JPasswordField newPasswordField;


    private final Color LIGHT_BLUE_BG = new Color(248, 250, 255);
    private final Color RICH_BLUE = new Color(0, 82, 165);
    private final Color DARK_BLUE = new Color(0, 51, 102);

    public Clientlogin() {
      
        frame.getContentPane().setBackground(LIGHT_BLUE_BG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 700);
        frame.setLocation(800, 50);
        frame.setLayout(null);

       
        titleLabel.setBounds(0, 40, 450, 30);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(DARK_BLUE);
        frame.add(titleLabel);

        userIDLabel.setBounds(50, 100, 120, 25);
        userIDLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userIDLabel.setForeground(DARK_BLUE);
        
        userIDField.setBounds(180, 100, 200, 25);
        userIDField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        
        userPasswordLabel.setBounds(50, 150, 120, 25);
        userPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userPasswordLabel.setForeground(DARK_BLUE);
        
        userPasswordField.setBounds(180, 150, 200, 25);
        userPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

      
        messageLabel.setBounds(50, 200, 350, 35);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

   
        loginButton.setBounds(110, 250, 100, 30);
        resetButton.setBounds(230, 250, 100, 30);
        styleButton(loginButton);
        styleButton(resetButton);

  
        registerButton.setBounds(150, 300, 150, 30);
        registerButton.setFocusable(false);
        registerButton.setForeground(RICH_BLUE);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(BorderFactory.createLineBorder(RICH_BLUE));
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));

      
        forgotPasswordButton.setBounds(150, 350, 150, 25);
        forgotPasswordButton.setFocusable(false);
        forgotPasswordButton.setForeground(RICH_BLUE);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 12));

     
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        registerButton.addActionListener(this);
        forgotPasswordButton.addActionListener(this);

      
        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(registerButton);
        frame.add(forgotPasswordButton);

        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(RICH_BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText("");
            return;
        }

        if (e.getSource() == loginButton) {
            String userID = userIDField.getText().trim();
            String password = new String(userPasswordField.getPassword());

           
            if (userID.isEmpty() || password.isEmpty()) {
                showMessage("Please fill all fields", Color.RED);
                return;
            }

            try {
                Document user = DatabaseHelperClient.getUserByUserIDAndPassword(userID, password);
                if (user != null) {
                    showMessage("Login Successful!", DARK_BLUE);
                    frame.dispose();
                    
                   
                    SwingUtilities.invokeLater(() -> {
                        Client client = new Client();
                        client.startClient();
                    });
                } else {
                    showMessage("Invalid username/email or password", Color.RED);
                }
            } catch (Exception ex) {
                showMessage("Error connecting to database", Color.RED);
                ex.printStackTrace();
            }
        }

        if (e.getSource() == registerButton) {
            frame.dispose();
            new RegistrationPageClient();
        }

        if (e.getSource() == forgotPasswordButton) {
            handleForgotPassword();
        }
    }

    private void showMessage(String text, Color color) {
        messageLabel.setForeground(color);
        messageLabel.setText(text);
    }

    private void handleForgotPassword() {
        resetUserID = JOptionPane.showInputDialog(frame, 
            "Enter your username/email to reset password:", 
            "Forgot Password", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (resetUserID != null && !resetUserID.trim().isEmpty()) {
            if (DatabaseHelperClient.checkUserExists(resetUserID)) {
                resetCode = String.format("%06d", new Random().nextInt(999999));
                System.out.println("Reset code for " + resetUserID + ": " + resetCode);
                createResetDialog();
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Username/email not found", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
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

    private void verifyResetCode() {
        String enteredCode = codeField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());

        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(resetDialog, 
                "Password must be at least 6 characters", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (enteredCode.equals(resetCode)) {
            try {
                boolean success = DatabaseHelperClient.resetPassword(resetUserID, newPassword);
                if (success) {
                    JOptionPane.showMessageDialog(resetDialog, 
                        "Password reset successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    resetDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(resetDialog, 
                        "Failed to reset password", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Clientlogin::new);
    }
}