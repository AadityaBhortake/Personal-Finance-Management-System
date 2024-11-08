import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class SignupPage extends JFrame {
    private JTextField usernameField, nameField, accountNumberField, balanceField, incomeField;
    private JPasswordField passwordField;
    private JButton signupButton;

    public SignupPage() {
        setTitle("Signup Page");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(null);
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 10, 100, 25);
        panel.add(usernameLabel);
        
        usernameField = new JTextField(20);
        usernameField.setBounds(130, 10, 160, 25);
        panel.add(usernameField);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 100, 25);
        panel.add(passwordLabel);
        
        passwordField = new JPasswordField(20);
        passwordField.setBounds(130, 40, 160, 25);
        panel.add(passwordField);
        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 70, 100, 25);
        panel.add(nameLabel);
        
        nameField = new JTextField(20);
        nameField.setBounds(130, 70, 160, 25);
        panel.add(nameField);
        
        JLabel accountNumberLabel = new JLabel("Account No.:");
        accountNumberLabel.setBounds(10, 100, 100, 25);
        panel.add(accountNumberLabel);
        
        accountNumberField = new JTextField(20);
        accountNumberField.setBounds(130, 100, 160, 25);
        panel.add(accountNumberField);
        
        JLabel balanceLabel = new JLabel("Balance:");
        balanceLabel.setBounds(10, 130, 100, 25);
        panel.add(balanceLabel);
        
        balanceField = new JTextField(20);
        balanceField.setBounds(130, 130, 160, 25);
        panel.add(balanceField);
        
        JLabel incomeLabel = new JLabel("Income:");
        incomeLabel.setBounds(10, 160, 100, 25);
        panel.add(incomeLabel);
        
        incomeField = new JTextField(20);
        incomeField.setBounds(130, 160, 160, 25);
        panel.add(incomeField);
        
        signupButton = new JButton("Signup");
        signupButton.setBounds(130, 190, 160, 25);
        panel.add(signupButton);
        
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signup();
            }
        });
    }
    
    private void signup() {
        Connection con = DatabaseConnection.connect();
        String query = "INSERT INTO users (username, password, name, account_number, balance, monthly_income) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, usernameField.getText());
            pst.setString(2, new String(passwordField.getPassword()));
            pst.setString(3, nameField.getText());
            pst.setString(4, accountNumberField.getText());
            pst.setDouble(5, Double.parseDouble(balanceField.getText()));
            pst.setDouble(6, Double.parseDouble(incomeField.getText()));
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "User registered successfully!");
            new LoginPage().setVisible(true);
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
