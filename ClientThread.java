import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ClientThread implements Runnable{

    private Selector selector;
    private Client client;

    public ClientThread(Selector s,Client c){
        selector = s;
        client = c;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try{
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
    
                    if(selectionKey.isReadable()){
                        SocketChannel readchannel = (SocketChannel)selectionKey.channel();
                        ByteBuffer bf = ByteBuffer.allocate(2048);
    
                        int readlength = readchannel.read(bf);
                        String code = "";
                        if(readlength>0){
                            bf.flip();
    
                            code += Charset.forName("UTF-8").decode(bf);
                        }
    
                        readchannel.register(selector, SelectionKey.OP_READ);
                        
                        if(code.length()==1){
                            System.out.println("receive number");
                            System.out.println(code);
                            client.number = Integer.valueOf(code);
                        }
                        else if(code.length()==3){
                            String []arr = code.split(" ");

                            client.paintWinner(Integer.valueOf(arr[1]));

                        }
                        else if(code.length()>10){
                            String []arr = code.split(" ");
                            
                            for(int i=0;i<4;i++){
                                client.hp[i]=Integer.valueOf(arr[i]);
                            }
                            for(int i=4;i<904;i++){
                                int j = i - 4;
                                client.map[j/30][j%30] = Integer.valueOf(arr[i]);
                            }
                            client.repaint();
                        }
                    }
                }
            }

        }catch(Exception e){

        }

    }
    
}
