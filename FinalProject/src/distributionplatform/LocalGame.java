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
    private JPanel LocalGamePanel;
    private JFrame menuFrame;
    private JButton backButton;
    private ImageIcon boomIcon;
    private JLabel gameImageLabel;
    private JLabel bestTimeLabel0;
    private JLabel bestTimeLabel1;
    private JLabel bestTimeLabel2;

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
        LocalGamePanel = new JPanel();
        LocalGamePanel.setLayout(null);
        LocalGamePanel.setVisible(true);
        menuJPanel.setVisible(false);
        scrollPane = new JScrollPane(gameImageLabel);
        scrollPane.setBounds(0, 0, 200, 430);
        LocalGamePanel.add(scrollPane);

        bestTimeLabel0 = new JLabel("Mode EASY Best Time : "+ Menu.userInformation.getMinTime(0));
        Font boldFont0 = new Font(bestTimeLabel0.getFont().getName(), Font.BOLD, bestTimeLabel0.getFont().getSize());
        bestTimeLabel0.setFont(boldFont0);
        bestTimeLabel0.setBounds(200,-20,1000,100);
        LocalGamePanel.add(bestTimeLabel0);

        bestTimeLabel1 = new JLabel("Mode MEDIUM Best Time : "+Menu.userInformation.getMinTime(1));
        Font boldFont1 = new Font(bestTimeLabel1.getFont().getName(), Font.BOLD, bestTimeLabel1.getFont().getSize());
        bestTimeLabel1.setFont(boldFont1);
        bestTimeLabel1.setBounds(200,0,1000,100);
        LocalGamePanel.add(bestTimeLabel1);

        bestTimeLabel2 = new JLabel("Mode HARD Best Time : "+Menu.userInformation.getMinTime(2));
        Font boldFont2 = new Font(bestTimeLabel2.getFont().getName(), Font.BOLD, bestTimeLabel0.getFont().getSize());
        bestTimeLabel2.setFont(boldFont2);
        bestTimeLabel2.setBounds(200,20,1000,100);
        LocalGamePanel.add(bestTimeLabel2);

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
                    setNewRecord();
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
    public void setNewRecord() {
    //mode 0
    Integer t = Menu.userInformation.getMinTime(0);
    bestTimeLabel0.setText("Mode EASY Best Time : "+t);
    LocalGamePanel.add(bestTimeLabel0);
    //mode 1
    t = Menu.userInformation.getMinTime(1);
    bestTimeLabel1.setText("Mode MEDIUM Best Time : "+t);
    LocalGamePanel.add(bestTimeLabel1);
    //mode 2
    t = Menu.userInformation.getMinTime(2);
    bestTimeLabel2.setText("Mode HARD Best Time : "+t);
    LocalGamePanel.add(bestTimeLabel2);

    menuFrame.add(LocalGamePanel);
        
    }
}
