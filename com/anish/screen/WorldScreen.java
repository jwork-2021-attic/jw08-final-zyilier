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
    private Calabash[] bros;
    private Minions[][] minions;
    String[] sortSteps;
    int posx;
    int posy;
    public Calabash A;
    public Thread[] B;
    

    public WorldScreen(int a) {
        if(a==0){
            world = new World(0);    
            B = new Thread[5];
            A = new Calabash(new Color(255,255,0),1,world);
            for(int i=0;i<5;i++){
                B[i] = new Thread(new Minions(new Color(i*15,255-i*15,0), i, world));
                B[i].start();
            }

            world.put(A,0,0);
            posx = 0;
            posy = 0;
            hpthread hhh = new hpthread();
            hhh.start();
        }
        if(a==1){
            world = new World(1);
            B = new Thread[5-world.score];
            A = new Calabash(new Color(255,255,0),1,world);
            File tem = new File("record\\tem.txt");
            int count1 = 0;
            try{
                FileReader f = new FileReader(tem);
                BufferedReader f1 = new BufferedReader(f);
                String str = null;
                String []arr;
                str = f1.readLine();
                arr = str.split(" ");
                A.hp = Integer.valueOf(arr[0]);
                for(int i = 0;i<30;i++){
                    str = f1.readLine();
                    arr = str.split(" ");
                    for(int j=0;j<30;j++){
                        if(arr[j].equals("2")){
                            B[count1] = new Thread(new Minions(new Color(count1*15,255-count1*15,0), count1, world,i,j));
                            B[count1].start();
                            count1++;
                        }
                        else if(arr[j].equals("3")){
                            world.put(A,i,j);
                            posx = i;
                            posy = j;
                            System.out.println("A has been put");
                        }
                    }
                }
                f1.close();
                f.close();
                hpthread hhh = new hpthread();world.h.start();
                hhh.start();
                
            }catch (Exception e){
                System.out.println("Error!");
            }
        }

    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Calabash[] bros, String step) {
        String[] couple = step.split("<->");
        getMinByRank(minions, Integer.parseInt(couple[0])).swap(getMinByRank(minions, Integer.parseInt(couple[1])));
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




    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if(world.if_win == -1)
            return new LoseScreen();
        else if(world.if_win == 1){
            return new WinScreen();
        }

        int code = key.getKeyCode();
        System.out.println(code);
        switch(code){
            case 40:
            A.direction = 4;
                if(judge(posx,posy+1)){
                    if(world.get(posx,posy+1).getGlyph()==(char)250){
                        A.hp--;
                    }
                    world.put(A,posx,posy+1);
                    world.put(new Floor(world), posx, posy);
                    posy++;
                }
                break;
            case 38:
            A.direction = 2;
            if(judge(posx,posy-1)){
                if(world.get(posx,posy-1).getGlyph()==(char)250){
                    A.hp--;
                }
                world.put(A,posx,posy-1);
                world.put(new Floor(world), posx, posy);
                posy--;
            }
            break;
            case 39:
            A.direction = 3;
            if(judge(posx+1,posy)){
                if(world.get(posx+1,posy).getGlyph()==(char)250){
                    A.hp--;
                }
                world.put(A,posx+1,posy);
                world.put(new Floor(world), posx, posy);
                posx++;
            }
            break;
            case 37:
            A.direction = 1;
            if(judge(posx-1,posy)){
                if(world.get(posx-1,posy).getGlyph()==(char)250){
                    A.hp--;
                }
                world.put(A,posx-1,posy);
                world.put(new Floor(world), posx, posy);
                posx--;
            }
            break;
            case 32:
            int tarx = -1,tary = -1;
            if(A.direction == 1){
                if(judge(posx-1, posy)){
                    tarx = posx -1;
                    tary = posy;
                }
            }
            else if(A.direction == 2){
                if(judge(posx, posy-1)){
                    tarx = posx;
                    tary = posy - 1;
                }
            }
            else if(A.direction == 3){
                if(judge(posx+1, posy)){
                    tarx = posx +1;
                    tary = posy;
                }
            }
            else{
                if(judge(posx, posy + 1)){
                    tarx = posx;
                    tary = posy + 1;
                }
            }
            if(tarx != -1 && tary != -1){
                System.out.println("successfully attack");
                if(world.get(tarx,tary).getGlyph() == (char)1){
                   world.get(tarx,tary).hp--;
                }
                else{
                    Thread t = new Thread (new Bullet(world, A.direction,tarx,tary));
                    t.start();
                }
            }
            break;
            case 83:
            File tem = new File("record\\tem.txt");
            try{
                if(!tem.exists()){
                    tem.createNewFile();
                }
                FileWriter f = new FileWriter(tem);
                BufferedWriter bf = new BufferedWriter(f);
                bf.write(String.valueOf(A.hp));bf.write(" ");bf.write(String.valueOf(world.score));bf.newLine();
                for(int i=0;i<30;i++){
                    for(int j=0;j<30;j++){
                        if(world.get(i,j).getGlyph()==(char)32||world.get(i,j).getGlyph()==(char)250){
                            bf.write("0");bf.write(" ");
                        }
                        else if(world.get(i,j).getGlyph()==(char)177){
                            bf.write("1");bf.write(" ");
                        }
                        else if(world.get(i,j).getGlyph()==(char)1){
                            bf.write("2");bf.write(" ");
                        }
                        else if(world.get(i,j).getGlyph()==(char)2){
                            bf.write("3");bf.write(" ");
                        }
                    }
                    bf.newLine();
                }
                bf.close();
                f.close();
            }catch (Exception e){
                System.out.println("Error!");
            }
            break;
        }

        return this;
    }

    public boolean judge(int x,int y){
        if(world.if_parsed == 1)
            return false;
        if(x>=0&&y>=0&&x<world.HEIGHT&&y<world.WIDTH){
            if(world.get(x,y).getColor()!=AsciiPanel.cyan){
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

    public class hpthread extends Thread{
        public void run(){
            while(world.if_win == 0){
                try{
                    Thread.sleep(50);
                    world.playerx = A.getX();
                    world.playery = A.getY();
                    if(world.score == 5){
                        world.if_win = 1;
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        
    }

}
