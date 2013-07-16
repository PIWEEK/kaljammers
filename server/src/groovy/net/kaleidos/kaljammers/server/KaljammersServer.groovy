package net.kaleidos.kaljammers.server

import java.net.ServerSocket
import net.kaleidos.kaljammers.game.GameBrain

class KaljammersServer {

    def serverPort = 4444
    //def serverPort2 = 4445
    def numClients = 0
    
    def gameBrain = GameBrain.instance

    public void serverProcess() {
        println "Starting server in port ${serverPort}..."
        def server = new ServerSocket(serverPort)
        //def server2 = new ServerSocket(serverPort2)
     
        while(true) {
            server.accept { socket ->
                println "processing new connection 1..."
                
                socket.withStreams { input, output ->
                
                    def reader = input.newReader()
                    def buffer = reader.readLine()
                    numClients++
                    println "client connected: $numClients, clientToken: $buffer"
                    output << "$numClients\n"
                    
                    while (true) {
                        buffer = reader.readLine()
                        println "1: $buffer"
                        
                        def actionP1 = [direction:(Integer.valueOf(buffer[0])), 
                                        b1:(Integer.valueOf(buffer[1])), 
                                        b2:(Integer.valueOf(buffer[2]))]
                        
                        def move = gameBrain.gameProcess(actionP1, null)
                        
                        println "$move"
                        output << "$move\n"
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
                    
                    println "processing/thread complete."
                }
            }
        }
    }

    static main(args) {
        def kaljammersServer = new KaljammersServer()
        kaljammersServer.serverProcess()
    }
}
