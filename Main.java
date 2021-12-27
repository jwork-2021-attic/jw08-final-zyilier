

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import javax.swing.JFrame;
import javax.swing.text.html.HTMLDocument.Iterator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

import com.anish.calabashbros.World;
import com.anish.screen.ClientScreen;
import com.anish.screen.Screen;
import com.anish.screen.WorldScreen;
import com.anish.screen.StartScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class Main extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    public String msg;
    public int number;
    public int [][]map;
    public int []hp;

    public Main() {
        super();
        terminal = new AsciiPanel(World.WIDTH + 20, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        for(int i=0;i<30;i++){
            for(int j=0;j<30;j++){
                map[i][j] = 1;
            }
        }
        for(int i=0;i<4;i++)
            hp[i]=4;
        screen = new ClientScreen();
        addKeyListener(this);
        repaint();
        // paintThread t = new paintThread();
        // t.start();
        msg = "";
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public class paintThread extends Thread{
        public void run(){
            while(true){
                try{
                    Thread.sleep(100);
                    repaint();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public void startClient() throws IOException{

        //连接服务器
 //       SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("112.20.78.72",8000));
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8000));
        System.out.println("Connect!");

        //接受数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);

  //      new Thread(new ClientThread(selector,this)).start();
        //向服务器发送消息
        while(true){
            if(msg.length()>1){
                socketChannel.write(Charset.forName("UTF-8").encode(msg));
                System.out.println("successfully write!");
                msg = "";
            }
        }
    }

    
    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        
    }

}
