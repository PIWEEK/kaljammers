/**
 * KaljammersServer
 * 
 * a PIWEEK project by Kaleidos
 * 
 * Some code based in: http://docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
 * has Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * 
 */ 
package net.kaleidos.kaljammers.server

import net.kaleidos.kaljammers.game.GameComm

public class KaljammersServerThread extends Thread {
    
    private Socket socket = null;
    def clientId
    
    def gameComm = GameComm.instance

    public KaljammersServerThread(Socket socket) {
        super("KaljammersServerThread");
        this.socket = socket;
    }

    public void run() {
        try {
            
            socket.withStreams { input, output ->
            
                def reader = input.newReader()

                // check client id
                if (gameComm.numClients + 1 > GameComm.MAX_PLAYERS) {
                    output << "Eres el $gameComm+1 ya somos muchos no puedes jugar!\n"
                    return
                }
                
                this.clientId = gameComm.numClients++
                
                // handshaking
                // 1.- read token
                // 2.- send client id
                def buffer = reader.readLine()
                println "client connected: $clientId, clientToken: $buffer"
                output << "$clientId\n"
                    
                def dis = new DataInputStream(input)
                def dos = new DataOutputStream(output)
                        
                while (true) {
                    
                        println "clientId: $clientId"
                    
                        //if (buffer.equals("Bye"))
                            //break;
                    
                        def px = dis.readFloat()
                        def py = dis.readFloat()
                        def fx = dis.readFloat()
                        def fy = dis.readFloat()
                        def sfx = dis.readFloat()
                        def sfy = dis.readFloat()
                        def isGoal = dis.readBoolean()
                        def isPick = dis.readBoolean()
                        
                        buffer = reader.readLine()
                        
                        println "player$clientId move: $px,$py,$fx,$fy,$sfx,$sfy,$isGoal,$isPick"

                        def action = [px:px, py:py, fx:fx, fy:fy, sfx:sfx, sfy:sfy, isGoal:isGoal, isPick:isPick]

                        def move = gameComm.saveInfo(clientId, action)
                        
                        println "$move"

                        dos.writeFloat(move.px) //2
                        dos.writeFloat(move.py)
                        dos.writeFloat(move.fx)
                        dos.writeFloat(move.fy)
                        dos.writeFloat(move.sfx)
                        dos.writeFloat(move.sfy)
                        dos.writeBoolean(move.isGoal)
                        dos.writeBoolean(move.isPick)
                        dos << "\n"
                }
                
                output.close();
                input.close();
            }
        
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
