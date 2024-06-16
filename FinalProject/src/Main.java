package src;

import src.distributionplafrom.*;
import java.util.Map;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import src.Minesweeper.User;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class Main implements ActionListener, Serializable {
    public static String user;
    public static User userInformation;
    public static File userfileforSerializable;

    private static JLabel userLabel;
    private static JTextField userText;
    private static JLabel passwordLabel;
    private static JPasswordField passwordText;
    private static JButton ConfirmButton;
    private static JLabel success;
    private static JFrame mFrame;
    private static JPanel panel;
    private static JButton CreateUserButton;
    private static String accountPath = "/resources/account";
    private static File file = new File(accountPath);

    @SuppressWarnings("unchecked")
    public Main() {
        try (InputStream fis = CreateUser.class.getResourceAsStream("/resources/account.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            CreateUser.account = (Map<String, String>) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        // Login
        setupLoginGUI();
    }

    private void setupLoginGUI() {
        mFrame = new JFrame("Login");
        panel = new JPanel();
        mFrame.setSize(350, 200);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setLocationRelativeTo(null);
        mFrame.add(panel);
        panel.setLayout(null);

        userLabel = new JLabel("User");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        ConfirmButton = new JButton("login");
        ConfirmButton.setBounds(10, 80, 80, 25);
        panel.add(ConfirmButton);
        ConfirmButton.addActionListener(this);

        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        panel.add(success);

        CreateUserButton = new JButton("Create New User");
        CreateUserButton.setBounds(100, 80, 200, 25);
        panel.add(CreateUserButton);
        CreateUserButton.addActionListener(this);

        mFrame.setVisible(true);
    }
    
    public static File getFile() {
        return file;
    }

    public void actionPerformed(ActionEvent e) {
        user = userText.getText();
        @SuppressWarnings("deprecation")
        String password = passwordText.getText();

        if (e.getSource() == ConfirmButton) {
            if (CreateUser.account.containsKey(user) && CreateUser.account.get(user).equals(password)) {
                success.setText("Login success");
                mFrame.dispose();
                new Menu(); // Navigate to menu
            } else {
                success.setText("Wrong password or no user");
            }
        } else if (e.getSource() == CreateUserButton) {
            new CreateUser();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
