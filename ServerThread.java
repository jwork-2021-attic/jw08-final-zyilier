
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ServerThread implements Runnable{
    private Selector selector;
    private Server server;

    public ServerThread(Selector s,Server se){
        selector = s;
        server = se;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
            while(true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try{
                    server.castOther(selector);
                }catch(Exception e){

                }
                
            }
        
    }
    
}
