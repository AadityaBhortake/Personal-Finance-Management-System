import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;

public class ProfilePage extends JFrame {
    private JTextField balanceField, incomeField;
    private JLabel nameLabel, accountNumberLabel;
    private JButton saveButton;
    private String username;

    public ProfilePage(String username) {
        this.username = username;
        setTitle("Profile Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(204,204,255));
        add(panel);
        panel.setLayout(null);

        // Name Label
        JLabel nameTitleLabel = new JLabel("Name:");
        nameTitleLabel.setBounds(10, 10, 100, 25);
        panel.add(nameTitleLabel);

        nameLabel = new JLabel();
        nameLabel.setBounds(150, 10, 200, 25);
        panel.add(nameLabel);

        // Account Number Label
        JLabel accountNumberTitleLabel = new JLabel("Account Number:");
        accountNumberTitleLabel.setBounds(10, 40, 150, 25);
        panel.add(accountNumberTitleLabel);

        accountNumberLabel = new JLabel();
        accountNumberLabel.setBounds(150, 40, 200, 25);
        panel.add(accountNumberLabel);

        // Balance Field
        JLabel balanceTitleLabel = new JLabel("Balance:");
        balanceTitleLabel.setBounds(10, 70, 100, 25);
        panel.add(balanceTitleLabel);

        balanceField = new JTextField(20);
        balanceField.setBounds(150, 70, 200, 25);
        panel.add(balanceField);

        // Monthly Income Field
        JLabel incomeTitleLabel = new JLabel("Monthly Income:");
        incomeTitleLabel.setBounds(10, 100, 100, 25);
        panel.add(incomeTitleLabel);

        incomeField = new JTextField(20);
        incomeField.setBounds(150, 100, 200, 25);
        panel.add(incomeField);

        // Save Button
        saveButton = new JButton("Save");
        saveButton.setBounds(150, 150, 100, 25);
        panel.add(saveButton);

        // Fetch user details and display them in the fields
        loadUserDetails();

        // Save Button ActionListener to update Balance and Income
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUserDetails();
            }
        });
    }

    // Method to load user details from the database
    private void loadUserDetails() {
        Connection con = DatabaseConnection.connect();
        String query = "SELECT * FROM users WHERE username=?";
        
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                accountNumberLabel.setText(rs.getString("account_number"));
                balanceField.setText(String.valueOf(rs.getDouble("balance")));
                incomeField.setText(String.valueOf(rs.getDouble("monthly_income")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update the Balance and Income in the database
    private void updateUserDetails() {
        double newBalance = Double.parseDouble(balanceField.getText());
        double newIncome = Double.parseDouble(incomeField.getText());

        Connection con = DatabaseConnection.connect();
        String query = "UPDATE users SET balance=?, monthly_income=? WHERE username=?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDouble(1, newBalance);
            pst.setDouble(2, newIncome);
            pst.setString(3, username);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ProfilePage("testUser").setVisible(true);
    }
}
