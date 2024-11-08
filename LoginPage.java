import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;

    public LoginPage() {
        setTitle("Login Page");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(null);
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 10, 80, 25);
        panel.add(usernameLabel);
        
        usernameField = new JTextField(20);
        usernameField.setBounds(100, 10, 160, 25);
        panel.add(usernameField);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);
        
        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 40, 160, 25);
        panel.add(passwordField);
        
        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
        
        signupButton = new JButton("Signup");
        signupButton.setBounds(180, 80, 80, 25);
        panel.add(signupButton);
        
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignupPage signupPage = new SignupPage();
                signupPage.setVisible(true);
                dispose();
            }
        });
    }
    
    private void login() {
        Connection con = DatabaseConnection.connect();
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, usernameField.getText());
            pst.setString(2, new String(passwordField.getPassword()));
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                HomePage homePage = new HomePage(rs.getString("username"));
                homePage.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
