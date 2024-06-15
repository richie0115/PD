package src.Minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.distributionplafrom.Menu;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.Vector;

public class MineSweeper extends JFrame implements java.awt.event.MouseListener{

    private int mapRow, mapCol;  //格數
    private int direct[][]={{0,0},{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,-1},{-1,1},{1,-1}}; //8方位。
    private int timeCount=0; //時間計數。
	private int timeContinue=1; //1:繼續、0:停止。

	private int mode;
	private int timeRecord;
    private int width, height;
    private int bombCount;
    private int[][] map;
    private int[][] bombAround;
    private boolean[][] buttonIsPress;
    private JButton[][] button;
    private JLabel bombRest;
	private JLabel time;


    public MineSweeper(int width, int height, int row, int col, int bombCount,int mode){  //不同模式

		this.mode = mode;
        this.width = width;
        this.height = height;
        this.mapRow = row;
        this.mapCol = col;  
        this.bombCount = bombCount; 
		this.timeRecord = 0;  //刷新紀錄

        button = new JButton[width][height];
        bombRest = new JLabel("目前炸彈:" + bombCount);
        map = new int[mapRow][mapCol]; //地圖。
        buttonIsPress =new boolean[mapRow][mapCol]; //判斷按鈕是否按壓。
        bombAround = new int[mapRow][mapCol]; //周圍有多少炸彈。

        setWindow();
        setTop();
        setButtons();
        setMap();
        setAroundBomb();
        setVisible(true);

    }

    //設置視窗  
    public void setWindow(){
        setSize(width, height); 
		setResizable(false); //設定大小不可調整。
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //設定關閉按鈕。
		setTitle("Minesweeper"); //設定標題。
		setLocationRelativeTo(this); //顯示為置中。
    }

    //top bar  //加圖片
    public void setTop(){
        //地雷數
        JPanel topPanel = new JPanel();
        bombRest.setText("目前炸彈:" + bombCount);
        topPanel.add(bombRest);

        JButton restart = new JButton("新局"); //重新開始按鈕。
		restart.setActionCommand("r"); //設定指令。
		restart.addMouseListener(this); //加入監聽。
		topPanel.add(restart);

        time = new JLabel("已經過時間：0"); //顯示時間。
		TimerTask timertask=new TimerTask(){
			public void run(){
				if(timeContinue==1)time.setText("已經過時間： "+(timeCount++));
			}
		};
        new Timer().scheduleAtFixedRate(timertask,0,1000);
		topPanel.add(time); 
		
		add(topPanel,BorderLayout.NORTH);
    }

    //設置按鈕  //加圖片
    public void setButtons(){
        JPanel centerButtonPanel = new JPanel();
        centerButtonPanel.setLayout(new GridLayout(mapRow,mapCol));
		Font font = new Font(Font.DIALOG, Font.BOLD, 20);
		centerButtonPanel.setFont(font);


        for(int i=0;i<mapRow;i++){
        	for(int j=0;j<mapCol;j++){
        		button[i][j]=new JButton();
        		button[i][j].setBackground(Color.WHITE); //設定按鈕背景顏色。
        		button[i][j].setActionCommand(i+" "+j); //設定按鈕指令。
        		button[i][j].addMouseListener(this); //按鈕加入監聽。
        		centerButtonPanel.add(button[i][j]);
        	}
        }
        add(centerButtonPanel,BorderLayout.CENTER);
    }

    //設置炸彈 有炸彈1 無炸彈0
    public void setMap(){
        int count=0;
		while(count!=bombCount){
			int x=0, y=0;
			if(this.mode == 0){
				x = (int)(Math.random()*9);
				y = (int)(Math.random()*9); 
			}
			else if(this.mode == 1){
				x = (int)(Math.random()*16);
				y = (int)(Math.random()*16);
			}
			else if(this.mode == 2){
				x = (int)(Math.random()*16);
				y = (int)(Math.random()*25);
			}

			if(map[x][y]==0){
				map[x][y]=1;
				count++;
			}
		}
    }

    //設置周圍炸彈數目
    public void setAroundBomb(){
        for(int i=0;i<mapRow;i++){
			for(int j=0;j<mapCol;j++){
				if(map[i][j]==1){
					bombAround[i][j]=-1; //炸彈本身設定為-1。
				}else{
					for(int k=0;k<direct.length;k++){
						int row=i+direct[k][0],col=j+direct[k][1];
						if((row>=0 && row<mapRow && col>=0 && col<mapCol) && map[row][col]==1) bombAround[i][j]++;
					}
				}
			}
		}
    }

    //重新開始  //加圖片
    private void restart(){
		timeCount = 0;
		timeContinue = 0;
		for(int i=0;i<mapRow;i++) Arrays.fill(map[i],0); 
		for(int i=0;i<mapRow;i++) Arrays.fill(buttonIsPress[i],false); 
		for(int i=0;i<mapRow;i++) Arrays.fill(bombAround[i],0); 
		
		for(int i=0;i<mapRow;i++){
        	for(int j=0;j<mapCol;j++){
        		button[i][j].setBackground(Color.WHITE);
        		button[i][j].setText("");
        	}
        }
		setMap();
		setAroundBomb();
        switch (mode) {
			case 0:
				bombCount = 10;
				break;
			case 1:
				bombCount = 40;
				break;
			case 2:
				bombCount = 99;
				break;
			default:
				break;
		}
        bombRest.setText("目前炸彈："+bombCount);
		time.setText("已經過時間： "+(timeCount));

	}

	//讀取紀錄
	public int getTime(){
		return this.timeRecord;
	}

	//讀取結束時間
	public void setTime(){
		this.timeRecord = timeCount;
	}

    //滑鼠事件  //加圖片
	@Override
	public void mouseClicked(MouseEvent e){
		String command[]=((JButton)e.getSource()).getActionCommand().split(" ");
		if(command[0].equals("r")) restart();//重新開始:r

		else{
			int row=Integer.parseInt(command[0]),col=Integer.parseInt(command[1]);

			//左鍵
			if(e.getButton()==MouseEvent.BUTTON1){

				//踩到地雷
				if(map[row][col]==1 && !buttonIsPress[row][col]){
					
					button[row][col].setBackground(Color.RED); //設定按鈕背景為紅色。
					for(int i=0;i<mapRow;i++)for(int j=0;j<mapCol;j++) if(map[i][j]==1) button[i][j].setText("*"); //印出所有炸彈。
					timeContinue=0; //時間停止計時。
					JOptionPane.showMessageDialog(null, "你踩到地雷了"); //顯示失敗訊息。
					Menu.userInformation.addTimeToRecord(0,getTime());
					restart(); //重新開始。
				}

				//判斷開始
				else if(timeContinue == 0 && !buttonIsPress[row][col])
				timeContinue = 1;

				//踩到空格
				else if(bombAround[row][col]==0 && !buttonIsPress[row][col]){
						
					Vector<postion> vector=new Vector<postion>(); //紀錄需要擴散的點。
					vector.add(new postion(row,col));
					//判斷點是否符合擴散的需求，直到vector的資料都處理完。
					for(int i=0;i<vector.size();i++){
						for(int j=0;j<direct.length;j++){
							int tempRow=direct[j][0]+vector.get(i).getRow(),tempCol=direct[j][1]+vector.get(i).getCol();
							if((tempRow>=0 && tempRow<mapRow) && (tempCol>=0 && tempCol<mapCol) && bombAround[tempRow][tempCol]==0){
								boolean flag=false;
								//檢查是否已經儲存此筆資料。
								for(int k=0;k<vector.size();k++){
									if(tempRow==vector.get(k).getRow() && tempCol==vector.get(k).getCol()){
										flag=true;
										break;
									}
								}
								if(!flag) vector.add(new postion(tempRow,tempCol)); //此擴散點不存在則儲存起來。
							}
						}
					}
					//開始擴散。
					for(int i=0;i<vector.size();i++){
						for(int j=0;j<direct.length;j++){
							int tempRow=direct[j][0]+vector.get(i).getRow(),tempCol=direct[j][1]+vector.get(i).getCol();
							if((tempRow>=0 && tempRow<mapRow) && (tempCol>=0 && tempCol<mapCol)){
								//非0數字才印出來。
								if(bombAround[tempRow][tempCol]!=0) 
									button[tempRow][tempCol].setText(Integer.toString(bombAround[tempRow][tempCol]));
								button[tempRow][tempCol].setBackground(Color.GRAY); 
								buttonIsPress[tempRow][tempCol]=true; 
							}
						}
					}
				}

				//踩到數字
				else if(!buttonIsPress[row][col]){		
					button[row][col].setText(Integer.toString(bombAround[row][col])); //印出數字。
					button[row][col].setBackground(Color.GRAY); //設定按鈕背景顏色。
					buttonIsPress[row][col]=true; //設定按鈕為按過。
				}
			}

			//右鍵
			if(e.getButton() == MouseEvent.BUTTON3){

				//判斷開始
				if(timeContinue == 0 && !buttonIsPress[row][col])
				timeContinue = 1;

				//取消旗子
				if(buttonIsPress[row][col] && button[row][col].getBackground() == Color.GREEN){
					buttonIsPress[row][col] = false;
					button[row][col].setBackground(Color.WHITE);
					bombCount++;
					bombRest.setText("目前炸彈:"+bombCount);
				}
				else{
					button[row][col].setBackground(Color.GREEN);
					buttonIsPress[row][col] = true;
					bombCount--;
					bombRest.setText("目前炸彈:"+bombCount);
			
					//判斷是否勝利
					if(bombCount == 0){
						boolean win = true;
						for(int i=0; i<mapRow; i++)
						for(int j=0; j<mapCol; j++)
						if(map[i][j] == 1 && !buttonIsPress[i][j])
						win = false;
						if(win){
							setTime();
							timeCount = 0;
							JOptionPane.showMessageDialog(null, "You win!");
							Menu.userInformation.addTimeToRecord(0,getTime());
							restart();
						}
					}
				}
			}
		}
	}


    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}
    public void mouseReleased(java.awt.event.MouseEvent e) {}
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    public void mouseExited(java.awt.event.MouseEvent e) {}

}

class postion{
	private int row,col;
	postion(int row,int col){
		this.row=row;
		this.col=col;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
}
