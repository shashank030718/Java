import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ExamFrame extends JFrame implements ActionListener {
    private JLabel questionLabel;
    private JRadioButton option1, option2, option3, option4;
    private ButtonGroup optionsGroup;
    private JButton nextButton, submitButton;
    private Connection conn;
    private ResultSet rs;
    private int score = 0;
    private String username;

    public ExamFrame(String username) {
        this.username = username;
        setTitle("Online Exam - Welcome " + username);
        setSize(500, 400);
        setLayout(new GridLayout(7, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        questionLabel = new JLabel();
        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();
        optionsGroup = new ButtonGroup();

        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);

        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        add(questionLabel);
        add(option1);
        add(option2);
        add(option3);
        add(option4);
        add(nextButton);
        add(submitButton);

        nextButton.addActionListener(this);
        submitButton.addActionListener(this);

        loadQuestions();
    }

    private void loadQuestions() {
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM questions");
            rs = ps.executeQuery();

            if (rs.next()) showQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showQuestion() throws SQLException {
        questionLabel.setText(rs.getString("question_text"));
        option1.setText("1. " + rs.getString("option1"));
        option2.setText("2. " + rs.getString("option2"));
        option3.setText("3. " + rs.getString("option3"));
        option4.setText("4. " + rs.getString("option4"));
        optionsGroup.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == nextButton) {
                checkAnswer();
                if (rs.next()) showQuestion();
                else JOptionPane.showMessageDialog(this, "No more questions. Click Submit!");
            } else if (e.getSource() == submitButton) {
                checkAnswer();
                saveResult();
                JOptionPane.showMessageDialog(this, "Exam finished! Your score: " + score);
                dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkAnswer() throws SQLException {
        int selected = 0;
        if (option1.isSelected()) selected = 1;
        else if (option2.isSelected()) selected = 2;
        else if (option3.isSelected()) selected = 3;
        else if (option4.isSelected()) selected = 4;

        if (selected == rs.getInt("correct_option")) {
            score++;
        }
    }

    private void saveResult() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO results (username, score) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setInt(2, score);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
