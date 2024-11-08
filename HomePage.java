import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class HomePage extends JFrame {
    private JLabel balanceLabel, savingsLabel, expensesLabel;
    private JButton addExpenseButton, profileButton;
    private String username;
    private double income, expenses, balance, savings;

    public HomePage(String username) {
        this.username = username;
        setTitle("Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(204,204,255));
        add(panel);
        panel.setLayout(null);
        balanceLabel = new JLabel("Balance: ");
        balanceLabel.setBounds(10, 10, 200, 25);
        panel.add(balanceLabel);
        savingsLabel = new JLabel("Monthly Savings: ");
        savingsLabel.setBounds(10, 40, 200, 25);
        panel.add(savingsLabel);
        expensesLabel = new JLabel("Monthly Expenses: ");
        expensesLabel.setBounds(10, 70, 200, 25);
        panel.add(expensesLabel);
        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.setBounds(10, 100, 120, 25);
        panel.add(addExpenseButton);
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        profileButton = new JButton("Profile");
        profileButton.setBounds(150, 100, 120, 25);
        panel.add(profileButton);

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProfilePage profilePage = new ProfilePage(username);
                profilePage.setVisible(true);
            }
        });
        loadUserDetails();
    }

    private void loadUserDetails() {
        Connection con = DatabaseConnection.connect();
        String query = "SELECT * FROM users WHERE username=?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                income = rs.getDouble("monthly_income");
                expenses = rs.getDouble("monthly_expenses");
                balance = rs.getDouble("balance");
                savings = income - expenses; 
                balanceLabel.setText("Balance: " + balance);
                savingsLabel.setText("Monthly Savings: " + savings);
                expensesLabel.setText("Monthly Expenses: " + expenses);
                updateMonthlySavings(savings);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addExpense() {
        String expenseInput = JOptionPane.showInputDialog(this, "Enter new expense:");

        if (expenseInput != null) {
            try {
                double newExpense = Double.parseDouble(expenseInput);
                expenses += newExpense;
                balance -= newExpense;
                savings = income - expenses; 

                Connection con = DatabaseConnection.connect();
                String query = "UPDATE users SET monthly_expenses = ?, balance = ?, monthly_savings = ? WHERE username=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setDouble(1, expenses);
                pst.setDouble(2, balance);
                pst.setDouble(3, savings);
                pst.setString(4, username);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Expense added successfully!");
                loadUserDetails();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a numeric value.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMonthlySavings(double newSavings) {
        Connection con = DatabaseConnection.connect();
        String query = "UPDATE users SET monthly_savings=? WHERE username=?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDouble(1, newSavings);
            pst.setString(2, username);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HomePage("testUser").setVisible(true);
    }
}

/*import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;

public class HomePage extends JFrame {
    private JLabel balanceLabel, savingsLabel, expensesLabel, incomeLabel;
    private JButton addExpenseButton, profileButton;
    private String username;

    public HomePage(String username) {
        this.username = username;
        setTitle("Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(204,204,255));
        add(panel);
        

        // Balance Label
        balanceLabel = new JLabel("Balance: ");
        balanceLabel.setBounds(10, 10, 200, 25);
        panel.add(balanceLabel);

        // Monthly Income Label
        incomeLabel = new JLabel("Monthly Income: ");
        incomeLabel.setBounds(10, 40, 200, 25);
        panel.add(incomeLabel);

        // Expenses Label
        expensesLabel = new JLabel("Monthly Expenses: ");
        expensesLabel.setBounds(10, 70, 200, 25);
        panel.add(expensesLabel);

        // Savings Label
        savingsLabel = new JLabel("Monthly Savings: ");
        savingsLabel.setBounds(10, 100, 200, 25);
        panel.add(savingsLabel);

        // Add Expense Button
        addExpenseButton = new JButton("Add Expense");
        addExpenseButton.setBounds(10, 130, 120, 25);
        panel.add(addExpenseButton);

        // Add ActionListener for the Add Expense Button
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });

        // Profile Button to open Profile Page
        profileButton = new JButton("Profile");
        profileButton.setBounds(150, 130, 120, 25); // Set position and size
        panel.add(profileButton);

        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProfilePage profilePage = new ProfilePage(username);
                profilePage.setVisible(true);
            }
        });

        // Fetch user details from the database and display them
        loadUserDetails();
    }

    private void loadUserDetails() {
        Connection con = DatabaseConnection.connect();
        String query = "SELECT * FROM users WHERE username=?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                double monthlyIncome = rs.getDouble("monthly_income");
                double monthlyExpenses = rs.getDouble("monthly_expenses");

                // Calculate savings as Income - Expenses
                double monthlySavings = monthlyIncome - monthlyExpenses;

                // Update the labels
                balanceLabel.setText("Balance: " + balance);
                incomeLabel.setText("Monthly Income: " + monthlyIncome);
                expensesLabel.setText("Monthly Expenses: " + monthlyExpenses);
                savingsLabel.setText("Monthly Savings: " + monthlySavings);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addExpense() {
        String expenseInput = JOptionPane.showInputDialog(this, "Enter new expense:");

        if (expenseInput != null) {
            try {
                double newExpense = Double.parseDouble(expenseInput);

                // Update user's expenses and balance in the database
                Connection con = DatabaseConnection.connect();
                String query = "UPDATE users SET monthly_expenses = monthly_expenses + ?, balance = balance - ? WHERE username=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setDouble(1, newExpense);
                pst.setDouble(2, newExpense);
                pst.setString(3, username);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Expense added successfully!");

                // Reload user details to update labels
                loadUserDetails();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a numeric value.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new HomePage("testUser").setVisible(true);
    }
}*/
