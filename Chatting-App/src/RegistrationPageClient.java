import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class RegistrationPageClient implements ActionListener {
    private JFrame frame;
    private JTextField usernameField, emailField, fullNameField;
    private JPasswordField passwordField;
    private JButton registerBtn, cancelBtn, loginBtn;

    public RegistrationPageClient() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Register");
        frame.setSize(450, 550);
        frame.setLayout(null);
        frame.setLocation(800, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);

        JLabel header = new JLabel("Create Account");
        header.setBounds(130, 20, 200, 30);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(new Color(0, 50, 100));
        frame.add(header);

    
        addLabelAndField("Username:", 80, usernameField = new JTextField(), 80);

        
        addLabelAndField("Full Name:", 130, fullNameField = new JTextField(), 130);

     
        addLabelAndField("Email:", 180, emailField = new JTextField(), 180);

   
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 230, 100, 25);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 230, 220, 25);
        frame.add(passwordField);

     
        registerBtn = createButton("Register", 80, 300, 120, 35, 
                                 new Color(0, 50, 100), this);
        frame.add(registerBtn);

     
        cancelBtn = createButton("Cancel", 220, 300, 120, 35, 
                               Color.LIGHT_GRAY, e -> frame.dispose());
        frame.add(cancelBtn);

      
        loginBtn = createButton("Already have an account? Login", 80, 360, 260, 35, 
                              new Color(70, 130, 180), e -> {
            frame.dispose();
            new Clientlogin(); 
        });
        frame.add(loginBtn);

        frame.setVisible(true);
    }

    private void addLabelAndField(String labelText, int yPos, JTextField field, int fieldYPos) {
        JLabel label = new JLabel(labelText);
        label.setBounds(50, yPos, 100, 25);
        frame.add(label);

        field.setBounds(160, fieldYPos, 220, 25);
        frame.add(field);
    }

    private JButton createButton(String text, int x, int y, int width, int height, 
                               Color bgColor, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (!validateInputs(username, fullName, email, password)) {
            return;
        }

        boolean success = DatabaseHelperClient.insertUser(username, fullName, email, password);
        if (success) {
            JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", 
                                        JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new Client(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Registration failed. Username or email may already exist.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs(String username, String fullName, String email, String password) {
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields");
            return false;
        }

        if (!isValidEmail(email)) {
            showError("Enter a valid email");
            return false;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationPageClient());
    }
}