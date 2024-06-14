package src.distributionplafrom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import src.Main;

import java.util.Map;
import java.util.HashMap;

public class CreateUser extends JFrame implements ActionListener {


    JFrame CreateUserFrame = new JFrame("Create New User");
    JPanel panel = new JPanel();
    JLabel userLabel = new JLabel();
    JLabel passwordLabel = new JLabel();
    JLabel passwordAgainLabel = new JLabel();
    JLabel success = new JLabel();
    JTextField userText = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JPasswordField passwordAgainField = new JPasswordField();
    JButton confirmButton = new JButton();
    File file = Main.getFile();
    public static Map<String,String> account = new HashMap<>();
    
@SuppressWarnings("unchecked")
public CreateUser() {
    if (account.containsKey("richie")==false) {
        account.put("richie", "richie");
    }
    SerializableItem();
    try {
        FileInputStream fis = new FileInputStream("resources/account.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        account= (Map<String,String>) ois.readObject();
        ois.close();
        fis.close();
    } catch (ClassNotFoundException c) {
        c.printStackTrace();
    } catch(IOException xx) {
        xx.printStackTrace();
    } 

    CreateUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    CreateUserFrame.setSize(420,420);
    CreateUserFrame.setLocationRelativeTo(null);
    CreateUserFrame.add(panel);
    panel.setLayout(null);
    
    userLabel = new JLabel("User Name");
    userLabel.setBounds(10,20,80,25);
    panel.add(userLabel);

    userText = new JTextField();
    userText.setBounds(100,20,165,25);
    panel.add(userText);

    passwordLabel = new JLabel("Password");
    passwordLabel.setBounds(10,80,80,25);
    panel.add(passwordLabel);

    passwordField = new JPasswordField();
    passwordField.setBounds(100,80,165,25);
    panel.add(passwordField);

    passwordAgainLabel = new JLabel("Password again");
    passwordAgainLabel.setBounds(10,140,120,25);
    panel.add(passwordAgainLabel);

    passwordAgainField = new JPasswordField();
    passwordAgainField.setBounds(120,140,165,25);
    panel.add(passwordAgainField);

    confirmButton = new JButton("Confirm");
    confirmButton.setBounds(10,200,150,25);
    panel.add(confirmButton);
    confirmButton.addActionListener(this);

    success = new JLabel("");
    success.setBounds(10,220,300,25);
    panel.add(success);


    CreateUserFrame.setVisible(true);   
    
}
public void actionPerformed(ActionEvent e) {
    String user = userText.getText();
    String password = passwordField.getText();
    String passwordAgain = passwordAgainField.getText();
    
            if(e.getSource() == confirmButton){
                if (user.equals("") || password.equals("") || passwordAgain.equals("")) {
                    success.setText("Please Enter Words");
                }else {
                        
                        if(account.containsKey(user)) {
                            success.setText("This account is used");
                        }
                    if(password.equals(passwordAgain)== false) {
                        success.setText("please enter password again");
                    } else if (account.containsKey(user)==false) {
                        account.put(user,password);
                        success.setText("Create success !");
                    }

                }
            SerializableItem();
        }}
public void SerializableItem () {
    try {
        FileOutputStream fos = new FileOutputStream("resources/account.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(account);
        
        oos.close();
        fos.close();
    } catch (IOException x) {
        x.printStackTrace();	
    }
}

}
