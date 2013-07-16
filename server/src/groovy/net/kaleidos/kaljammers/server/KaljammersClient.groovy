package net.kaleidos.kaljammers.server

class KaljammersClient {

    def serverPort
    def serverHost = "localhost"
    def cliendId
    def clientToken

    static main(args) {
        def kaljammersClient = new KaljammersClient()
        kaljammersClient.serverPort = args[0] as Integer
        kaljammersClient.clientProcess()
    }
    
    def clientProcess() {
        println "Starting client in port ${serverPort}..."
     
        def s = new Socket(serverHost, serverPort);
        s.withStreams { input, output ->
            clientToken = UUID.randomUUID().toString().replaceAll('-', '')
            println "Client token ${clientToken}..."
            output << "$clientToken\n"
            def reader = input.newReader()
            def buffer = reader.readLine()
            println "clientId = $buffer"
        
            while (true) {
                output << "111\n"
                buffer = reader.readLine()
                println "$buffer"
            }
        }    
    }
}
