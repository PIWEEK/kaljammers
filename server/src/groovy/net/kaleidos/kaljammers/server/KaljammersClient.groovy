package net.kaleidos.kaljammers.server

class KaljammersClient {

    def serverPort = 4444
    def serverHost = "localhost"
    def sleepMillis = 1000
    def cliendId
    def clientToken

    static main(args) {
        def kaljammersClient = new KaljammersClient()
        kaljammersClient.clientProcess()
    }
    
    def clientProcess() {
        println "Starting client in port ${serverPort}..."
     
        def s = new Socket(serverHost, serverPort);
        s.withStreams { input, output ->
            clientToken = UUID.randomUUID().toString().replaceAll('-', '')
            println "Client token ${clientToken}..."
            output << "$clientToken\n"

            def clientId = input.newReader().readLine()
            println "clientId = $clientId"
        
            while (true) {
                sleep(sleepMillis)
                output << "$clientToken[0]\n"
            }
        }    
    }
}
