

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
import java.util.Set;

import javax.swing.JFrame;


import com.anish.calabashbros.World;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;
import com.anish.screen.StartScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class Server {
    public int numbercount;

    private Selector selector;

    private WorldScreen screen;
    public Server() {
        super();
        screen = new WorldScreen(0);
        numbercount = 1;

    }

    public void startServer() throws IOException{

        selector = Selector.open();
        
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(8000));

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server start successfully!");

        new Thread(new ServerThread(selector,this)).start();

        while(true){
            //获取channel数量
            int readChannels = selector.select();
            if(readChannels == 0){
                continue;
            }

            //获取可用channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();

                iterator.remove();

                if(selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("a connection come in");
                    socketChannel.write(Charset.forName("UTF-8").encode(String.valueOf(numbercount)));
                    numbercount++;
                }
                if(selectionKey.isReadable()){
                    SocketChannel readchannel = (SocketChannel)selectionKey.channel();
                    ByteBuffer bf = ByteBuffer.allocate(1024);

                    int readlength = readchannel.read(bf);
                    String code = "";
                    if(readlength>0){
                        bf.flip();

                        code += Charset.forName("UTF-8").decode(bf);
                    }
                    String []arr = code.split(" ");
                    screen.movePlayer(Integer.valueOf(arr[0])-1, Integer.valueOf(arr[1]));


                    readchannel.register(selector, SelectionKey.OP_READ);
                    //广播给其他客户端
                    if(code.length()>0){
                        System.out.println("receive");
                        System.out.println(code);
                        castOther(selector);
                    }
                }
            }
        }
    }

    public void castOther(Selector selector) throws IOException {
        String code = "";
        if(screen.getWorld().score == 3&&screen.getWorld().if_win == 1){
            int winner = 0;
            for(int i=0;i<4;i++){
                if(screen.bros[i].hp>0){
                    winner = i+1;
                    break;
                }
            }
            code += "9" + " " + String.valueOf(winner);
        }
        else{

        
            for(int i=0;i<4;i++){
                code += String.valueOf(screen.bros[i].hp) + " ";
            }
            for(int i=0;i<30;i++){
                for(int j=0;j<30;j++){
                    if(screen.getWorld().get(i, j).getGlyph()==(char)32){
                        code += "0" + " ";
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)177){
                        code += "1" + ' ';
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)250){
                        code += "2" + ' ';
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)1){
                        code += "3" + ' ';
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)2 && screen.getWorld().get(i, j).getColor() == Color.blue){
                        code += "4" + ' ';
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)2 && screen.getWorld().get(i, j).getColor() == Color.yellow){
                        code += "5" + ' ';
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)2 && screen.getWorld().get(i, j).getColor() == Color.green){
                        code += "6" + ' ';
                    }
                    else if(screen.getWorld().get(i, j).getGlyph()==(char)2 && screen.getWorld().get(i, j).getColor() == Color.orange){
                        code += "7" + ' ';
                    }
                    else {
                        code += "0" + ' ';
                    }
                }
            }
        }
        //获得所有已接入客户端
        Set<SelectionKey> selectionKeySet = selector.keys();
        //循环向所有channel广播消息
        for(SelectionKey k:selectionKeySet){
            Channel target = k.channel();
            if(target instanceof SocketChannel){
                ((SocketChannel)target).write(Charset.forName("UTF-8").encode(code));
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            Server s = new Server();
            s.startServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
