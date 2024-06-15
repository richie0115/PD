package src.distributionplafrom;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.Minesweeper.Setter;

public class LocalGame {
    private JScrollPane scrollPane;
    private JPanel menuJPanel;
    private JFrame menuFrame;
    private JButton backButton;
    private ImageIcon boomIcon;
    private JLabel gameImageLabel;
    private JLabel bestTimeLabel;

    public LocalGame(JScrollPane scrollPane, JPanel menuJPanel, JFrame menuFrame, JTextArea importedProgramTextArea) {
        this.scrollPane = scrollPane;
        this.menuJPanel = menuJPanel;
        this.menuFrame = menuFrame;

        // Load image
        loadBombImage();
        gameImageLabel = new JLabel(boomIcon);
        initialize();
    }

    private void loadBombImage() {
        try {
            // Load image 
            boomIcon = new ImageIcon(new ImageIcon(getClass().getResource("/resources/bomb.png")).getImage().getScaledInstance(51, 61, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load bomb.png");
        }
    }

    private void initialize() {
        JPanel LocalGamePanel = new JPanel();
        LocalGamePanel.setLayout(null);
        LocalGamePanel.setVisible(true);
        menuJPanel.setVisible(false);
        scrollPane = new JScrollPane(gameImageLabel);
        scrollPane.setBounds(0, 0, 200, 430);
        LocalGamePanel.add(scrollPane);
        Menu.userInformation.getName();
        Integer time = Menu.userInformation.getMinTime();

        bestTimeLabel = new JLabel("Your Best Time : "+time);
        Font boldFont = new Font(bestTimeLabel.getFont().getName(), Font.BOLD, bestTimeLabel.getFont().getSize());
        bestTimeLabel.setFont(boldFont);
        bestTimeLabel.setBounds(200,-20,1000,100);
        LocalGamePanel.add(bestTimeLabel);

        menuFrame.add(LocalGamePanel);

        gameImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Clicked at: (" + x + ", " + y + ")");
                System.out.println("Icon width: " + boomIcon.getIconWidth() + ", height: " + boomIcon.getIconHeight());
                if (x >= 75 && x <= 122 && y >= 184 && y <= 240) {
                    // Run process
                    Setter settter = new Setter();
                    Integer t = Menu.userInformation.getMinTime();
                    bestTimeLabel.setText("Your Best Time : " + t);
                    LocalGamePanel.add(bestTimeLabel);
                    menuFrame.add(LocalGamePanel);
                }
            }
        });

        backButton = new JButton("Back to Menu");
        backButton.setBounds(250, 300, 140, 25);

        LocalGamePanel.add(backButton);
        menuFrame.add(LocalGamePanel);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeUserGame();
                LocalGamePanel.setVisible(false);
            }
        });

        menuFrame.revalidate();
        menuFrame.repaint();
    }

    public void closeUserGame() {
        menuFrame.remove(scrollPane);
        menuFrame.remove(backButton);
        menuJPanel.setVisible(true);
        menuFrame.repaint();
    }
    public void getGameTimeToUserRecord() {
        
    }
}
