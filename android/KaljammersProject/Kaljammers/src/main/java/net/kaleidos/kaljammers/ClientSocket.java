package net.kaleidos.kaljammers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * Created by primicachero on 16/07/13.
 */
public class ClientSocket {
    Socket myclient;
    public static final String IP = "192.168.1.13";
    public static final String PORT = "4444";
    private DataOutputStream dos;
    private DataInputStream dis;

    private BufferedReader reader;

    public BufferedReader getReader() {
        return reader;
    }

    public boolean connect(){
        int finalPort = Integer.valueOf(PORT);
        try{
            myclient =  new Socket(IP,finalPort);
            if (myclient.isConnected()){
                this.dos = new DataOutputStream(myclient.getOutputStream());
                this.dis = new DataInputStream(myclient.getInputStream());
                this.reader = new BufferedReader(new InputStreamReader(dis));
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


    public boolean sendClientId(int id){
        try{

            if (myclient.isConnected()){
                dos.writeInt(id);
                dos.writeChar('\n');
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }



    public boolean sendMessage(SendMessageSocket message){
        try{
            if (myclient.isConnected()){
                dos.writeByte((byte) message.getDirection());
                dos.writeBoolean(message.isButton1());
                dos.writeBoolean(message.isButton2());
                dos.writeChar('\n');
                return true;
            }
            else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }


    public GetMessageSocket getMessage() {
        GetMessageSocket message;
        try {
            message = new GetMessageSocket();
            message.setPlayer1X(dis.readShort());
            message.setPlayer1Y(dis.readShort());
            message.setPlayer2X(dis.readShort());
            message.setPlayer2Y(dis.readShort());
            message.setFrisbeeX(dis.readShort());
            message.setFrisbeeY(dis.readShort());

            //Read until eol
            reader.readLine();



            Log.e("KALJAMMERS", "Frisbee: "+message.getFrisbeeX()+", "+message.getFrisbeeY());



        }catch (Exception e){
            return null;
        }
        return message;
    }
}
