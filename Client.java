
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;


import com.anish.calabashbros.World;
import com.anish.screen.ClientScreen;
import com.anish.screen.Screen;
import com.anish.screen.StartScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class Client extends JFrame implements KeyListener{

    public int number;
    public int hp[];
    public int map[][];
    public String msg;
    private AsciiPanel terminal;

    public SocketChannel socketChannel;
    public Client() {
        super();
        terminal = new AsciiPanel(World.WIDTH + 20, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        addKeyListener(this);
        map = new int[30][30];
        hp = new int[4];
        msg = "";
        for(int i=0;i<30;i++){
            for(int j=0;j<30;j++){
                map[i][j] = 1;
            }
        }
        for(int i=0;i<4;i++)
            hp[i]=4;

        repaint();
    }

    @Override
    public void repaint() {       
        terminal.clear();        
        displayOutput();
        super.repaint();
    }

    public void paintWinner(int i){
        terminal.clear();
        terminal.write("Winner is:",0,0);
        if(i==1){
            terminal.write("Blueman!",10,0);
        }
        else if(i==2){
            terminal.write("Yellowman!",10,0);
        }
        else if(i==3){
            terminal.write("Greenman!",10,0);
        }
        else if(i==4){
            terminal.write("Orangeman!",10,0);
        }
        super.repaint();
    }

    public void displayOutput(){

        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 30; y++) {
                if(map[x][y]==0){//空地
                    terminal.write((char)32, x, y, Color.gray);
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
        displayMessages(terminal);     
    }
    
    private void displayMessages(AsciiPanel terminal) {

        String s1 = Integer.toString(hp[0]);
        String s2 = Integer.toString(hp[1]);
        String s3 = Integer.toString(hp[2]);
        String s4 = Integer.toString(hp[3]);

        terminal.write("Blueman Hp:",32, 1);

        terminal.write( s1 , 45 , 1);

        terminal.write("Yellowman Hp:",32, 3);

        terminal.write( s2 , 45 , 3);

        terminal.write("Greenman Hp:",32, 5);

        terminal.write( s3 , 45 , 5);

        terminal.write("Orangeman Hp:",32, 7);

        terminal.write( s4 , 45 , 7);

        if(number == 1){
            terminal.write("You are Blueman",32, 9);
        }
        else if(number == 2){
            terminal.write("You are Yellowman",32, 9);
        }
        else if(number == 3){
            terminal.write("You are Greenman",32, 9);
        }
        else if(number == 4){
            terminal.write("You are Orangeman",32, 9);
        }
    
    }

    public void startClient() throws IOException{

        //连接服务器
 //       SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("112.20.78.72",8000));
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8000));
        System.out.println("Connect!");

        //接受数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);

        new Thread(new ClientThread(selector,this)).start();
        //向服务器发送消息
        // while(true){
        //     if(msg.length()>1){
        //         socketChannel.write(Charset.forName("UTF-8").encode(msg));
        //         System.out.println("successfully write!");
        //         msg = "";
        //     }
        // }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void castPosition() throws IOException{
        if(msg.length()>1){
            socketChannel.write(Charset.forName("UTF-8").encode(msg));
            System.out.println("successfully write!");
            msg = "";
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(hp[number-1]>0){
        msg = String.valueOf(number)+" "+e.getKeyCode();
        try {
            castPosition();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println("pressed a key");
        repaint();
    }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static void main(String[] args) {
        try {
            Client app = new Client();
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.setVisible(true);
            app.startClient();
        } catch (IOException e) { 
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
