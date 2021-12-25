package com.anish.calabashbros;

import java.awt.Color;

import java.io.*;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import com.anish.maze_generator.MazeGenerator;

public class World {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public int[][] maze;
    public int playerx;
    public int playery;
    public int score;
    public int if_win;
    public int if_parsed;
    public hpthread h;


    private Tile<Thing>[][] tiles;

    public World(int a) {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        MazeGenerator mazeGenerator = new MazeGenerator(30);
        mazeGenerator.generateMaze();
        maze = mazeGenerator.maze;

        if(a==0){

        score = 0;
        if_win = 0;
        if_parsed = 0;
        


        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(i%5==0 || j%5==0){
                    maze[i][j] = 1;
                }
                if(i>=12&&i<=18&&j>=12&&j<=18){
                    maze[i][j]=1;
                }
                tiles[i][j] = new Tile<>(i, j);
                if(maze[i][j]==0){
                    tiles[i][j].setThing(new Wall(this));
                }
                else tiles[i][j].setThing(new Floor(this));
            }
        }
        }
        else if (a==1){
            if_win = 0;
            if_parsed = 0;
            File tem = new File("record\\tem.txt");
            try{
                FileReader f = new FileReader(tem);
                BufferedReader f1 = new BufferedReader(f);
                String str = null;
                String []arr;
                str = f1.readLine();
                arr = str.split(" ");
                score = Integer.valueOf(arr[1]);
                for(int i = 0;i<30;i++){
                    str = f1.readLine();
                    arr = str.split(" ");
                    for(int j=0;j<30;j++){
                        tiles[i][j] = new Tile<>(i, j);
                        System.out.print(arr[j]);
                        if(arr[j].equals("1")){
                            tiles[i][j].setThing(new Wall(this));
                            System.out.println("a wall");
                        }
                        else {
                            tiles[i][j].setThing(new Floor(this));
                        }
                    }
                }
                System.out.println("Success World!");
                f1.close();
                f.close();

            }catch (Exception e){
                System.out.println("Error!");
            }
        }
        h = new hpthread();
    }


    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

    public Floor setFloor(){
        return new Floor(this);
    }

    public class hpthread extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(100);
                    for(int i=0;i<30;i++){
                        for(int j=0;j<30;j++){
                            if(tiles[i][j].getThing().getGlyph()!=(char)32){
                                if(tiles[i][j].getThing().hp<=0){
                                    if(tiles[i][j].getThing().getGlyph()==(char)177){
                                        put(setFloor(),i, j);
                                    }
                                    else if(tiles[i][j].getThing().is_dead == 0){

                                        tiles[i][j].getThing().is_dead = 1;
                                        if(tiles[i][j].getThing().getGlyph() == (char)1){
                                            score++;
                                            put(setFloor(),i, j);
                                        }
                                        else if(tiles[i][j].getThing().getGlyph() == (char)2){
                                            if_win = -1;
                                        }
                                        put(setFloor(),i, j);
                                    }
                                    else{
                                        put(setFloor(),i, j);
                                    }
                                }
                            }
                        }
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        
    }

}
