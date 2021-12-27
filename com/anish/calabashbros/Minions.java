package com.anish.calabashbros;

import java.awt.Color;
import java.security.spec.DSAGenParameterSpec;


public class Minions extends Creature implements Comparable<Minions> , Runnable {
    private int rank;
    public int tx;
    public int ty;
    public int [][] maze;
    public int stopDFS;
    public int depth;
    public int finaldepth = 999;
    public int dir;
    public int finaldir;

    public Minions(Color color, int rank, World world) {
        super(color, (char) 1, world);
        this.rank = rank;
        this.is_dead = 0;
        maze = new int [30][30];
        world.put(this,15,rank*6);
        hp = 1;
    }
    public Minions(Color color, int rank, World world,int x,int y) {
        super(color, (char) 1, world);
        this.rank = rank;
        this.is_dead = 0;
        maze = new int [30][30];
        world.put(this,x,y);
        hp = 1;
        tx=ty=0;
    }


    public int getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return String.valueOf(this.rank);
    }

    @Override
    public int compareTo(Minions o) {
        return Integer.valueOf(this.rank).compareTo(Integer.valueOf(o.rank));
    }

    public void swap(Minions another) {
        int x = this.getX();
        int y = this.getY();
        this.moveTo(another.getX(), another.getY());
        another.moveTo(x, y);
    }

    public void DFS(int x,int y){
        if(x==tx&&y==ty){
            if(depth < finaldepth){
                finaldepth = depth;
                finaldir = dir;
                stopDFS = 1;
                
            }
        }
        if(stopDFS == 0){
            depth++;
            maze[x][y]=1;
            int tarx,tary;
            for(int i=0;i<4;i++){
                if(i==0){
                    tarx = x-1;
                    tary = y;
                }
                else if(i==1){
                    tarx = x;
                    tary = y - 1;
                }
                else if(i == 2){
                    tarx = x+1;
                    tary = y;
                }
                else {
                    tarx = x;
                    tary = y+1;
                }
                if(Judge(tarx, tary)){
                    if(maze[tarx][tary]==0)
                        DFS(tarx, tary);
                }
            }

        }
    }
    public boolean Judge(int tarx,int tary){
        if(tarx>=0&&tary>=0&&tarx<30&&tary<30){
            if(maze[tarx][tary]==0&&world.get(tarx, tary).getGlyph()==(char)32){
                return true;
            }
        }
        return false;
    }
    public void run(){
        while(is_dead==0){
            try{
                Thread.sleep(600);
                int m = 999;
                int tar = 0;
                
                for(int i=0;i<4;i++){
                    int t = Math.abs(world.screen.bros[i].getX()-this.getX())+Math.abs(world.screen.bros[i].getY()-this.getY());
                    if(t < m && world.screen.bros[i].hp > 0){
                        tar = i;
                    }
                }
                tx = world.screen.bros[tar].getX();
                ty = world.screen.bros[tar].getY();
                int readyattack = 0;
                if(tx == this.getX()||ty == this.getY()){
                    readyattack = 1;
                    if(tx == this.getX()){
                        if(ty>this.getY()){
                            for(int i=this.getY()+1;i!=ty;i++){
                                if(world.get(tx, i).getGlyph()!=(char)32 && world.get(tx, i).getGlyph()!=(char)250){
                                    readyattack = 0;
                                    break;
                                }
                            }
                            if(readyattack == 1){
                                if (ty==this.getY()+1){
                                    world.screen.bros[tar].hp--;
                                }
                                else{
                                Thread t = new Thread (new Bullet(world,4,this.getX(),this.getY()+1));
                                t.start();
                                }
                            }
                        }
                        else{
                            for(int i=this.getY()-1;i!=ty;i--){
                                if(world.get(tx, i).getGlyph()!=(char)32 && world.get(tx, i).getGlyph()!=(char)250){
                                    readyattack = 0;
                                    break;
                                }
                            }
                            if(readyattack == 1){
                                if (ty==this.getY()-1){
                                    world.screen.bros[tar].hp--;
                                }
                                else{
                                Thread t = new Thread (new Bullet(world,2,this.getX(),this.getY()-1));
                                t.start();
                                }
                            }
                        }
                    }
                    else{
                        if(tx>this.getX()){
                            for(int i=this.getX()+1;i!=tx;i++){
                                if(world.get(i, ty).getGlyph()!=(char)32 && world.get(i, ty).getGlyph()!=(char)250){
                                    readyattack = 0;
                                    break;
                                }
                            }
                            if(readyattack == 1){
                                if (tx==this.getX()+1){
                                    world.screen.bros[tar].hp--;
                                }
                                else{
                                Thread t = new Thread (new Bullet(world,3,this.getX()+1,this.getY()));
                                t.start();
                                }
                            }
                        }
                        else{
                            for(int i=this.getX()-1;i!=tx;i--){
                                if(world.get(i, ty).getGlyph()!=(char)32  && world.get(i, ty).getGlyph()!=(char)250){
                                    readyattack = 0;
                                    break;
                                }
                            }
                            if(readyattack == 1){
                                if (tx==this.getX()-1){
                                    world.screen.bros[tar].hp--;
                                }
                                else{
                                Thread t = new Thread (new Bullet(world,1,this.getX()-1,this.getY()));
                                t.start();
                                }
                            }
                        }
                    }
                    
                }
                if(readyattack == 0){
                    int tarx,tary;
                    tarx = Math.abs(this.getX()-tx);
                    tary = Math.abs(this.getY()-ty);
                    finaldir = -1;
                    if(tarx>=tary){
                        if(this.getX()>tx&&Judge(this.getX()-1, this.getY())){
                            finaldir = 0;
                        }
                        else if(this.getX()<tx&&Judge(this.getX()+1, this.getY())){
                            finaldir = 2;
                        }
                        else if(this.getY()>ty&&Judge(this.getX(), this.getY()-1)){
                            finaldir = 1;
                        }
                        else if(this.getY()<ty&&Judge(this.getX(), this.getY()+1)){
                            finaldir = 3;
                        }
                        else if(this.getX()>tx){
                            finaldir = 4;
                        }
                        else if(this.getX()<tx){
                            finaldir = 6;
                        }
                        else if(this.getY()>ty){
                            finaldir = 5;
                        }
                        else if(this.getY()<ty){
                            finaldir = 7;
                        }
                    }
                    else{
                        if(this.getY()>ty&&Judge(this.getX(), this.getY()-1)){
                            finaldir = 1;
                        }
                        else if(this.getY()<ty&&Judge(this.getX(), this.getY()+1)){
                            finaldir = 3;
                        }
                        else if(this.getX()>tx&&Judge(this.getX()-1, this.getY())){
                            finaldir = 0;
                        }
                        else if(this.getX()<tx&&Judge(this.getX()+1, this.getY())){
                            finaldir = 2;
                        }
                        else if(this.getY()>ty){
                            finaldir = 5;
                        }
                        else if(this.getY()<ty){
                            finaldir = 7;
                        }
                        else if(this.getX()>tx){
                            finaldir = 4;
                        }
                        else if(this.getX()<tx){
                            finaldir = 6;
                        }
                    }
                    switch (finaldir){
                        case 0:
                        
                            if(this.getX()>0){
                            world.put(new Floor(world), this.getX(), this.getY());
                            moveTo(this.getX()-1, this.getY());}
                            break;
                        case 1:
                        
                            if(this.getY()>0){
                            world.put(new Floor(world), this.getX(), this.getY());
                            moveTo(this.getX(), this.getY()-1);}
                            break;
                        case 2:
                        
                            if(this.getX()<29){
                            world.put(new Floor(world), this.getX(), this.getY());
                            moveTo(this.getX()+1, this.getY());}
                            break;
                        case 3:
                        
                            if(this.getY()<29){
                            world.put(new Floor(world), this.getX(), this.getY());
                            moveTo(this.getX(), this.getY()+1);}
                            break;

                        case 4:
                                if(this.getX()>0){
                                    if(world.get(this.getX()-1,this.getY()).getGlyph()==(char)177){
                                        world.get(this.getX()-1,this.getY()).hp--;
                                    }
                                }
                                break;
                        case 5:
                            if(this.getY()>0){
                                if(world.get(this.getX(),this.getY()-1).getGlyph()==(char)177){
                                    world.get(this.getX(),this.getY()-1).hp--;
                                }
                            }
                        break;
                        case 6:
                        if(this.getX()<29){
                            if(world.get(this.getX()+1,this.getY()).getGlyph()==(char)177){
                                world.get(this.getX()+1,this.getY()).hp--;
                            }
                        }
                        break;
                        case 7:
                        if(this.getY()<29){
                            if(world.get(this.getX(),this.getY()+1).getGlyph()==(char)177){
                                world.get(this.getX(),this.getY()+1).hp--;
                            }
                        }
                        break;



                    }

                }

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
