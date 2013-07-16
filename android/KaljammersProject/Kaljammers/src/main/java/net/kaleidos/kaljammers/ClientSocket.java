package net.kaleidos.kaljammers;
import java.io.ObjectOutputStream;
import 	java.net.Socket;
/**
 * Created by primicachero on 16/07/13.
 */
public class ClientSocket {
    Socket myclient;
    public static final String IP = "10.8.1.1";
    public static final String PORT = "8080";

    public boolean connect(){
        int finalPort = Integer.valueOf(PORT);
        try{
            myclient =  new Socket(IP,finalPort);
            if (myclient.isConnected()){
                return true;
            }else  {
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }

    public boolean disconnect(){
        try {
            myclient.close();
            if (myclient.isClosed()){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }
    public boolean sendMessage(MessageSocket message){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(myclient.getOutputStream());
            if (myclient.isConnected()){
                oos.writeObject(message);
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
                return false;
        }
    }
}
