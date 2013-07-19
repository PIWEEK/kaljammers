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

        serverSocket.close();
    }

    static main(args) {
        def kaljammersServer = new KaljammersServer()
        kaljammersServer.serverProcess()
    }
}
