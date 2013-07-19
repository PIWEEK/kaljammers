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
                def dis = new DataInputStream(input)
                def dos = new DataOutputStream(output)

                // check client id
                if (gameComm.numClients + 1 > GameComm.MAX_PLAYERS) {
                    output << "Eres el $gameComm+1 ya somos muchos no puedes jugar!\n"
                    return
                }
                
                this.clientId = gameComm.numClients++
                
                // handshaking
                // 1.- send client id
                dos.writeByte(clientId) 
                dos << "\n"
                
                
                // wait until players connected
                println "waiting conection"                         
                while (gameComm.numClients < GameComm.MAX_PLAYERS) {    
                    print "."
                    sleep(500)
                }
                
                
                
                // wait player to receive players selection and field                
                def player = dis.readByte()
                def field = dis.readByte()                
                reader.readLine()
                gameComm.saveGameInfo(clientId, [player:player, field:field])

                // wait until info complete
                def completed = false
                println "waiting info completed"            
                while (!completed) {
                    print "."
                    completed = (gameComm.gameInfo.player1 != 0
                                    && gameComm.gameInfo.player2 != 0)                                    
                    sleep(500)
                }

                // send info to 
                dos.writeByte(gameComm.gameInfo.player1)
                dos.writeByte(gameComm.gameInfo.player2) 
                dos.writeByte(gameComm.gameInfo.field) 
                dos << "\n"
                        
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
                        
                        reader.readLine()
                        
                        println "player$clientId move: $px,$py,$fx,$fy,$sfx,$sfy,$isGoal,$isPick"

                        def action = [px:px, py:py, fx:fx, fy:fy, sfx:sfx, sfy:sfy, isGoal:isGoal, isPick:isPick]

                        def move = gameComm.saveMoveInfo(clientId, action)
                        
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
