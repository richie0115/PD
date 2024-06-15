package src.distributionplafrom;

import javax.swing.*;

import src.Main;
import src.Minesweeper.User;

import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Menu extends JFrame implements ActionListener {
    static JOptionPane importAlert = new JOptionPane();
    static JFrame menuFrame = new JFrame("main menu");
    static JLabel menuLabel = new JLabel();
    JLabel welcomJLabel = new JLabel();
    static JPanel menuJPanel = new JPanel();
    static JScrollPane scrollPane = new JScrollPane();

    static JTextArea importedProgramTextArea = new JTextArea();

    static String lineText;

    JMenuBar menuBar;
    JMenu FileMenu;
    JMenu EditMenu;
    JMenu HelpMenu;
    JMenu ActionMenu;
    JMenuItem ImportiItem = new JMenuItem("Import");
    JMenuItem Run = new JMenuItem("Run");
    JMenuItem Back = new JMenuItem("Back");
    JButton logoutButton = new JButton("Logout");
    JButton userGameButton = new JButton("User's game");
    JButton LocalGameButton = new JButton("Local game");


    File userfileforSerializable = new File("resources/" + Main.user + ".ser");
    public static User userInformation;

    private UserGame userGame;
    private LocalGame localGame;

    public Menu() {
        if (!userfileforSerializable.exists()) {
            userInformation = new User(Main.user);
            userInformation.getName();
        } else {
            try {
                FileInputStream fis = new FileInputStream("resources/" + Main.user + ".ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                userInformation = (User) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException c) {
                c.printStackTrace();
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
        }
        userInformation.getName();
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(420, 420);
        menuJPanel.setLayout(null);

        menuFrame.add(menuJPanel);
        welcomJLabel = new JLabel("welcome ! " + Main.user);
        welcomJLabel.setBounds(10, 10, 120, 25);
        menuJPanel.add(welcomJLabel);

        menuBar = new JMenuBar();
        FileMenu = new JMenu("File");
        ActionMenu = new JMenu("Action");
        EditMenu = new JMenu("Edit");
        HelpMenu = new JMenu("Help");

        ImportiItem = new JMenuItem("Import");
        Run = new JMenuItem("Run");
        Back = new JMenuItem("Back");

        ActionMenu.add(Back);
        FileMenu.add(Run);
        Run.addActionListener(this);
        FileMenu.add(ImportiItem);
        ImportiItem.addActionListener(this);

        logoutButton.setBounds(320, 300, 80, 25);
        menuJPanel.add(logoutButton);
        logoutButton.addActionListener(this);

        LocalGameButton.setBounds(10, 60, 140, 25);
        menuJPanel.add(LocalGameButton);
        LocalGameButton.addActionListener(this);

        userGameButton.setBounds(10, 30, 140, 25);
        menuJPanel.add(userGameButton);
        userGameButton.addActionListener(this);

        menuBar.add(FileMenu);
        menuBar.add(EditMenu);
        menuBar.add(HelpMenu);
        menuBar.add(ActionMenu);

        menuFrame.setJMenuBar(menuBar);
        menuFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ImportiItem) {
            JFileChooser FileChooser = new JFileChooser();
            int ChosenFile = FileChooser.showOpenDialog(null);
            if (ChosenFile == JFileChooser.APPROVE_OPTION) {
                File file = new File(FileChooser.getSelectedFile().getAbsolutePath());
                String fileName = file.getName();
                String filepath = file.getAbsolutePath();
                importAlert(fileName);
                importedProgramTextArea.append(filepath + "\n");
            }
        }
        if (e.getSource() == Run) {
                // run
                try {
                    System.out.println(lineText+"123");
                    File fileToOpen = new File(lineText);
                    
    
                    if (!Desktop.isDesktopSupported()) {
                        System.out.println("Desktop is not supported");
                        return;
                    }
                    Desktop desktop = Desktop.getDesktop();
                    if (!fileToOpen.exists()) {
                        JOptionPane.showMessageDialog(menuFrame, "File does not exist", "Warning", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    desktop.open(fileToOpen);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            
        }
        if (e.getSource() == Back) {
            menuJPanel.setVisible(true);
            menuFrame.add(menuJPanel);
            menuFrame.repaint();
        }
        if (e.getSource() == logoutButton) {
            // store game
            SerializableItem("resources/" + Main.user + ".ser");
            menuFrame.dispatchEvent(new WindowEvent(menuFrame, WindowEvent.WINDOW_CLOSING));
        }
        if (e.getSource() == userGameButton) {
            if (userGame != null) {
                userGame.closeUserGame();
                userGame = null;
            }
            userGame = new UserGame(scrollPane, menuJPanel, menuFrame, importedProgramTextArea);
        }
        if (e.getSource() == LocalGameButton) {
            if (localGame != null) {
                localGame.closeUserGame();
                localGame = null;
            }
            localGame = new LocalGame(scrollPane, menuJPanel, menuFrame, importedProgramTextArea);
        }
    }

    public void importAlert(String fileName) {
        JOptionPane.showMessageDialog(menuFrame, "Import successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void SerializableItem(String name) {
        try {
            FileOutputStream fos = new FileOutputStream(name);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            userInformation.getName();
            oos.writeObject(userInformation);

            oos.close();
            fos.close();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }
}
