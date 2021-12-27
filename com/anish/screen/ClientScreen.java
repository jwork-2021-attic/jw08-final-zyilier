package com.anish.screen;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import java.awt.Color;



public class ClientScreen implements Screen{

    private int [][]map;
    private int []hp;
    
    public void load(int [][]m,int []h){
        map = m;
        hp = h;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // TODO Auto-generated method stub
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                if(map[x][y]==0){//空地
                    terminal.write("A", x, y, Color.gray);
                }
                else if(map[x][y]==1){//墙
                    terminal.write((char)177, x, y, AsciiPanel.cyan);
                }
                else if(map[x][y]==2){//子弹
                    terminal.write((char)250,x,y,Color.gray);
                }
                else if(map[x][y]==3){//敌人
                    terminal.write((char)1,x,y,Color.red);
                }
                else if(map[x][y]==4){//四个玩家
                    terminal.write((char)2,x,y,Color.blue);
                }
                else if(map[x][y]==5){
                    terminal.write((char)2,x,y,Color.yellow);
                }
                else if(map[x][y]==6){
                    terminal.write((char)2,x,y,Color.green);
                }
                else if(map[x][y]==7){
                    terminal.write((char)2,x,y,Color.orange);
                }

            }
        }
 //       displayMessages(terminal); 
    }

    private void displayMessages(AsciiPanel terminal) {

        String s1 = Integer.toString(hp[0]);
        String s2 = Integer.toString(hp[1]);
        String s3 = Integer.toString(hp[2]);
        String s4 = Integer.toString(hp[3]);

        terminal.write("Player1 Hp:",32, 1);

        terminal.write( s1 , 39 , 1);

        terminal.write("Player2 Hp:",32, 3);

        terminal.write( s2 , 39 , 3);

        terminal.write("Player3 Hp:",32, 5);

        terminal.write( s3 , 39 , 5);

        terminal.write("Player4 Hp:",32, 7);

        terminal.write( s4 , 39 , 7);
    
    }

    

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
