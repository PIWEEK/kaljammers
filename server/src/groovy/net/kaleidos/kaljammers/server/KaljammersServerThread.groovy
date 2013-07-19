/**
 * KaljammersServer
 * 
 * a PIWEEK project by Kaleidos
 * 
 * Some code based in: http://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 * has Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * 
 */ 

public class KaljammersServerThread extends Thread {
    
    private Socket socket = null;
    def clientId
    
    def gameBrain = GameBrain.instance

    public KaljammersServerThread(Socket socket) {
        super("KaljammersServerThread");
        this.socket = socket;
    }

    public void run() {
        try {
            
            socket.withStreams { in, out ->
            
                def reader = in.newReader()

                // client id
                gameBrain.numClients++
                this.clientId = gameBrain.numClients
                
                // handshaking
                def buffer = reader.readLine()
                println "client connected: $clientId, clientToken: $buffer"
                out << "$clientId\n"
                    
                def dis = new DataInputStream(in)
                    
                while (true) {
                    
                        //if (buffer.equals("Bye"))
                            //break;
                    
                        def direction = dis.readByte()
                        def b1 = dis.readBoolean()
                        def b2 = dis.readBoolean()
                        buffer = reader.readLine()
                        //println "direction: $direction b1: $b1 b2: $b2"

                        def actionP1 = [direction:direction, b1:b1, b2:b2]

                        def move = gameBrain.gameProcess(actionP1, null)

                        //[info.coordP1.x, info.coordP1.y, info.coordP2.x, info.coordP2.y,
                        // info.coordF.x, info.coordF.y, info.statusF.ordinal()] as byte[]

                        //println "$move"

                        def dos = new DataOutputStream(out)
                        dos.writeShort(move.coordP1.x) //2
                        dos.writeShort(move.coordP1.y)
                        dos.writeShort(move.coordP2.x)
                        dos.writeShort(move.coordP2.y)
                        dos.writeShort(move.coordF.x)
                        dos.writeShort(move.coordF.y)
                        dos.writeByte((byte)move.statusF)
                        dos << "\n"
                        
                }
                
                out.close();
                in.close();
            }
        
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
