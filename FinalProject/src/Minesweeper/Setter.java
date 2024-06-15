package src.Minesweeper;


import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Setter extends JFrame implements MouseListener{

    int mode;    

    public Setter(){
        
        super("choose mode");
        setSize(300, 250); 
		setResizable(false); //設定大小不可調整。
		setDefaultCloseOperation(EXIT_ON_CLOSE); //設定關閉按鈕。
		setLocationRelativeTo(this); //顯示為置中。

        Container contentPane = new Container();
        contentPane.setLayout(new FlowLayout());
        
        JPanel panel = new JPanel();
        panel.setFont(getFont());
        panel.setLayout(new GridLayout(2,3));
        JButton easy = new JButton("easy");
        JButton normal = new JButton("normal");
        JButton hard = new JButton("hard");

        easy.setActionCommand("e");
        normal.setActionCommand("n");
        hard.setActionCommand("h");

        JButton[] buttons = {easy, normal, hard};

        for(int i=0; i<3; i++) {
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setBounds(i, i, 30, 20);
            buttons[i].addMouseListener(this);
            panel.add(buttons[i]);
        }
        JButton confirm = new JButton("確定");
        confirm.setActionCommand("Y");
        confirm.addMouseListener(this);
        panel.add(confirm);
        confirm.setBackground(Color.WHITE);
        add(panel, "Center");
        setVisible(true);
        
    }




    @Override
    public void mouseClicked(MouseEvent e) {
        String[] command = ((JButton)e.getSource()).getActionCommand().split(" ");

        switch (command[0]) {
            case "e":
                this.mode = 0;
                break;
            case "n":
                this.mode = 1;
                break;
            case "h":
                this.mode = 2;
                break;
            default:
                break;
        }



        if(command[0] == "Y")
        switch (this.mode) {
            case 0:
                MineSweeper eGame = new MineSweeper(400, 450, 9, 9, 10, 0);
                this.dispose();
                break;
            case 1:
                MineSweeper nGame = new MineSweeper(700, 750, 16, 16, 40, 1);
                this.dispose();
                break;
            case 2:
                MineSweeper hGame = new MineSweeper(1100, 750, 16, 25, 99, 2);
                this.dispose();
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {}


    @Override
    public void mouseExited(MouseEvent e) {

    }
}
