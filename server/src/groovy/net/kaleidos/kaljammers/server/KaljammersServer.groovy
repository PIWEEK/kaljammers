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

import java.net.ServerSocket
import net.kaleidos.kaljammers.game.GameBrain

class KaljammersServer {

    def serverPort = 4444

    def gameBrain = GameBrain.instance

    public void serverProcess() throws IOException {
        
        ServerSocket serverSocket = null
        boolean listening = true

        
        println "Starting server in port ${serverPort}..."


        try {
            serverSocket = new ServerSocket(serverPort)
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.")
            System.exit(-1)
        }
        
        while (listening) {
            new KaljammersServerThread(serverSocket.accept()).start()
        }


        // REFACTOR

        while(true) {
            server.accept { socket ->
                println "processing new connection 1..."

                socket.withStreams { input, output ->

                    def reader = input.newReader()
                    def buffer = reader.readLine()
                    numClients++
                    println "client connected: $numClients, clientToken: $buffer"
                    output << "$numClients\n"

                    def dis = new DataInputStream(input)

                    while (true) {

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

                        def dos = new DataOutputStream(output)
                        dos.writeShort(move.coordP1.x) //2
                        dos.writeShort(move.coordP1.y)
                        dos.writeShort(move.coordP2.x)
                        dos.writeShort(move.coordP2.y)
                        dos.writeShort(move.coordF.x)
                        dos.writeShort(move.coordF.y)
                        dos.writeByte((byte)move.statusF)
                        dos << "\n"
                    }


                    //server2.accept { socket2 ->



                        //println "processing new connection 2..."

                        //socket2.withStreams { input2, output2 ->

                            //def reader2 = input2.newReader()
                            //def buffer2 = reader2.readLine()
                            //numClients++
                            //println "client connected: $numClients, clientToken: $buffer"
                            //output2 << "$numClients\n"

                            //while (true) {
                                //println "while true"
                                ////buffer = reader.readLine()
                                ////println "1: $buffer"
                                //buffer2 = reader2.readLine()
                                //println "2: $buffer2"
                            //}

                        //}
                    //}

                    //println "processing/thread complete."
                }
            }
        }
    }

    static main(args) {
        def kaljammersServer = new KaljammersServer()
        kaljammersServer.serverProcess()
    }
}
