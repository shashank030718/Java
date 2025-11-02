import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    // UI Components
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn, clearBtn;

    public LoginFrame() {
        setTitle("Online Exam Portal - Login");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window
        setLayout(null); // manual layout for simplicity

        // --- Labels ---
        JLabel title = new JLabel("Login to Exam Portal");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(80, 20, 200, 30);
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 70, 100, 25);
        add(userLabel);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 25);
        add(passLabel);

        // --- Text Fields ---
        userField = new JTextField();
        userField.setBounds(150, 70, 130, 25);
        add(userField);

        passField = new JPasswordField();
        passField.setBounds(150, 110, 130, 25);
        add(passField);

        // --- Buttons ---
        loginBtn = new JButton("Login");
        loginBtn.setBounds(70, 160, 90, 30);
        add(loginBtn);

        clearBtn = new JButton("Clear");
        clearBtn.setBounds(180, 160, 90, 30);
        add(clearBtn);

        // --- Button Actions ---
        loginBtn.addActionListener(e -> login());
        clearBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
        });

        setVisible(true);
    }

    // Login verification method
    private void login() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "✅ Login Successful!");
                dispose(); // close login window
                new ExamFrame(username).setVisible(true); // open exam window
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Username or Password!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main for testing this frame
    public static void main(String[] args) {
        new LoginFrame();
    }
}
