package src.distributionplafrom;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserGame {
    private JScrollPane scrollPane;
    private JPanel menuJPanel;
    private JTextArea importedProgramTextArea;
    private JFrame menuFrame;
    private JButton backButton;
    private JLabel linetextLabel;
    public UserGame(JScrollPane scrollPane, JPanel menuJPanel, JFrame menuFrame, JTextArea importedProgramTextArea) {
        this.scrollPane = scrollPane;
        this.menuJPanel = menuJPanel;
        this.menuFrame = menuFrame;
        this.importedProgramTextArea = importedProgramTextArea;

        initialize();
    }

    private void initialize() {
        JPanel backJPanel = new JPanel();

        linetextLabel = new JLabel("選擇：");
        linetextLabel.setBounds(200, 10, 240, 25);
        backJPanel.add(linetextLabel);

        backJPanel.setLayout(null);
        backJPanel.setVisible(true);
        menuJPanel.setVisible(false);
        scrollPane = new JScrollPane(importedProgramTextArea);
        scrollPane.setBounds(0,0,200,430);
        backJPanel.add(scrollPane);
        menuFrame.add(backJPanel);

        importedProgramTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int offset = importedProgramTextArea.viewToModel2D(e.getPoint());
                    int line = importedProgramTextArea.getLineOfOffset(offset);
                    int start = importedProgramTextArea.getLineStartOffset(line);
                    int end = importedProgramTextArea.getLineEndOffset(line);
                    Menu.lineText = importedProgramTextArea.getText(start, end - start).replaceAll("\\s+", "").trim();
                    linetextLabel.setText("選擇："+Menu.lineText);
                    JOptionPane.showMessageDialog(menuFrame, "Line clicked: " + Menu.lineText, "Event Triggered", JOptionPane.INFORMATION_MESSAGE);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        backButton = new JButton("Back to Menu");
        backButton.setBounds(250, 300, 140, 25);
        
        backJPanel.add(backButton);
        menuFrame.add(backJPanel);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeUserGame();
                backJPanel.setVisible(false);
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
}
