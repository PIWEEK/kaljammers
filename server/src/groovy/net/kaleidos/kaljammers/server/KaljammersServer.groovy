package net.kaleidos.kaljammers.server

import java.net.ServerSocket

class KaljammersServer {

    def serverPort = 4444
    def numClients = 0

    public void serverProcess() {
        println "Starting server in port ${serverPort}..."
        def server = new ServerSocket(serverPort)
     
        while(true) {
            server.accept { socket ->
                println "processing new connection..."
                
                socket.withStreams { input, output ->
                    def reader = input.newReader()
                    def buffer = reader.readLine()
                    numClients++
                    println "client: $numClients, clientToken: $buffer"
                    output << numClients + "\n"
                }
                println "processing/thread complete."
            }
        }
    }

    static main(args) {
        def kaljammersServer = new KaljammersServer()
        kaljammersServer.serverProcess()
    }

}
