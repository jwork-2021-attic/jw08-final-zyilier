package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import java.io.*;

import com.anish.calabashbros.Bullet;
import com.anish.calabashbros.Calabash;
import com.anish.calabashbros.MinionSorter;
import com.anish.calabashbros.Minions;
import com.anish.calabashbros.World;
import com.anish.calabashbros.Floor;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    public Calabash[] bros;
    private Minions[][] minions;
    String[] sortSteps;

    public Calabash A;
    public Thread[] B;
    public int posx[];
    public int posy[];
    

    public WorldScreen(int a) {
        if(a==0){
            world = new World(this);    
            B = new Thread[4];
            bros = new Calabash[4];
            A = new Calabash(new Color(255,255,0),1,world);
            for(int i=0;i<4;i++){
                B[i] = new Thread(new Minions(Color.red, i, world,6+i,6+i));

                B[i].start();
            }
            bros[0]= new Calabash(Color.blue,1,world);
            bros[1]= new Calabash(Color.yellow,1,world);
            bros[2]= new Calabash(Color.green,1,world);
            bros[3]= new Calabash(Color.orange,1,world);
            world.put(bros[0],0,0);
            world.put(bros[1],29,0);
            world.put(bros[2],0,29);
            world.put(bros[3],29,29);
            posx = new int[4];
            posy = new int[4];
            posx[0]=0;posx[1]=29;posx[2]=0;posx[3]=29;
            posy[0]=0;posy[1]=0;posy[2]=29;posy[3]=29;
        }

    }


    private Minions getMinByRank(Minions[][] bros, int rank) {
        for (int i=0;i<bros.length;i++) {
            for(int j=0;j<bros[i].length;j++)
            if (bros[i][j].getRank() == rank) {
                return bros[i][j];
            }
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
        displayMessages(terminal);     
    }
    
    private void displayMessages(AsciiPanel terminal) {
       
        String s2 = Integer.toString(world.score);

        String s1 = Integer.toString(A.hp);

        terminal.write("Hp:",32, 1);

        terminal.write( s1 , 39 , 1);

        terminal.write("Scores:", 32, 3);

        terminal.write( s2 , 39 , 3);

        terminal.write("press 's'",32,5);

        terminal.write("to save",42,5);
    
    }
    public void movePlayer(int id,int code){
        switch(code){
            case 40:
            bros[id].direction = 4;
                if(judge(posx[id],posy[id]+1)){
                    if(world.get(posx[id],posy[id]+1).getGlyph()==(char)250){
                        bros[id].hp--;
                    }
                    world.put(bros[id],posx[id],posy[id]+1);
                    world.put(new Floor(world), posx[id], posy[id]);
                    posy[id]++;
                }
                break;
            case 38:
            bros[id].direction = 2;
            if(judge(posx[id],posy[id]-1)){
                if(world.get(posx[id],posy[id]-1).getGlyph()==(char)250){
                    bros[id].hp--;
                }
                world.put(bros[id],posx[id],posy[id]-1);
                world.put(new Floor(world), posx[id], posy[id]);
                posy[id]--;
            }
            break;
            case 39:
            bros[id].direction = 3;
            if(judge(posx[id]+1,posy[id])){
                if(world.get(posx[id]+1,posy[id]).getGlyph()==(char)250){
                    bros[id].hp--;
                }
                world.put(bros[id],posx[id]+1,posy[id]);
                world.put(new Floor(world), posx[id], posy[id]);
                posx[id]++;
            }
            break;
            case 37:
            bros[id].direction = 1;
            if(judge(posx[id]-1,posy[id])){
                if(world.get(posx[id]-1,posy[id]).getGlyph()==(char)250){
                    bros[id].hp--;
                }
                world.put(bros[id],posx[id]-1,posy[id]);
                world.put(new Floor(world), posx[id], posy[id]);
                posx[id]--;
            }
            break;
            case 32:
            int tarx = -1,tary = -1;
            if(bros[id].direction == 1){
                if(judge(posx[id]-1, posy[id])){
                    tarx = posx[id] -1;
                    tary = posy[id];
                }
            }
            else if(bros[id].direction == 2){
                if(judge(posx[id], posy[id]-1)){
                    tarx = posx[id];
                    tary = posy[id] - 1;
                }
            }
            else if(bros[id].direction == 3){
                if(judge(posx[id]+1, posy[id])){
                    tarx = posx[id] +1;
                    tary = posy[id];
                }
            }
            else{
                if(judge(posx[id], posy[id] + 1)){
                    tarx = posx[id];
                    tary = posy[id] + 1;
                }
            }
            if(tarx != -1 && tary != -1){
                if(world.get(tarx,tary).getGlyph() == (char)1 || world.get(tarx,tary).getGlyph() == (char)2){
                   world.get(tarx,tary).hp--;
                }
                else{
                    Thread t = new Thread (new Bullet(world, bros[id].direction,tarx,tary));
                    t.start();
                }
            }
            break;
        }
    }


    public World getWorld(){
        return this.world;
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return this;
    }

    public boolean judge(int x,int y){
        if(world.if_parsed == 1)
            return false;
        if(x>=0&&y>=0&&x<world.HEIGHT&&y<world.WIDTH){
            if(world.get(x,y).getGlyph()!=(char)177 && world.get(x,y).getGlyph()!=(char)2){
                return true;
            }
        }
        return false;
    }
    public static boolean existed(int num, int[] array, int index) {
		for(int i=0; i<index; i++) {
			if(num == array[i]) {
				return true;
			}
		}
		return false;
	}


    @Override
    public void load(int[][] m, int[] h) {
        // TODO Auto-generated method stub
        
    }

    // public class hpthread extends Thread{
    //     public void run(){
    //         while(world.if_win == 0){
    //             try{
    //                 Thread.sleep(600);
    //                 for(int i=0;i<4;i++){
    //                     int m = 999;
    //                     for(int j=0;j<4;j++){
    //                         if(Math.abs(bros[j].getX()-B[i].getY))
    //                     }
    //                 }
    //                 if(world.score == 3){
    //                     world.if_win = 1;
    //                 }
    //             }catch (InterruptedException e){
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
        
    // }

}


