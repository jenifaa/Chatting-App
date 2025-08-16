import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import org.bson.Document;

public class RegisterPage implements ActionListener {
    JFrame frame;
    JTextField usernameField, emailField, fullNameField;
    JPasswordField passwordField;
    JButton registerBtn, cancelBtn, loginBtn;

    RegisterPage() {
        frame = new JFrame("Register");
        frame.setSize(450, 550);
        frame.setLayout(null);
        frame.setLocation(200, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);

        JLabel header = new JLabel("Create Account");
        header.setBounds(130, 20, 200, 30);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setForeground(new Color(0, 50, 100));
        frame.add(header);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 100, 25);
        frame.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 80, 220, 25);
        frame.add(usernameField);

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setBounds(50, 130, 100, 25);
        frame.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(160, 130, 220, 25);
        frame.add(fullNameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 180, 100, 25);
        frame.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 180, 220, 25);
        frame.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 230, 100, 25);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 230, 220, 25);
        frame.add(passwordField);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(80, 300, 120, 35);
        registerBtn.setBackground(new Color(0, 50, 100));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.addActionListener(this);
        frame.add(registerBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(220, 300, 120, 35);
        cancelBtn.setBackground(Color.LIGHT_GRAY);
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> frame.dispose());
        frame.add(cancelBtn);

        loginBtn = new JButton("Already have an account? Login");
        loginBtn.setBounds(80, 360, 260, 35);
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(e -> {
            frame.dispose();
            new Login();
        });
        frame.add(loginBtn);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!isValidEmail(email)) {
            JOptionPane.showMessageDialog(frame, "Enter a valid email", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(password.length() < 6) {
            JOptionPane.showMessageDialog(frame, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = DatabaseHelper.insertUser(username, fullName, email, password);

        if(success) {
            JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new Server(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Email already exists or DB error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterPage::new);
    }
}
