package net.kaleidos.kaljammers.server

class KaljammersClient {

    def serverPort
    def serverHost = "localhost"
    def cliendId
    def clientToken

    static main(args) {
        def kaljammersClient = new KaljammersClient()
        
        kaljammersClient.serverPort = 4444
        
        if (args[0]) {
            kaljammersClient.serverPort = args[0] as Integer    
        }
        
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
        
            def dis = new DataInputStream(input)

        
            while (true) {
                output << "111\n"
                
                
                def p1x = dis.readShort()
                def p1y = dis.readShort()
                def p2x = dis.readShort()
                def p2y = dis.readShort()
                def pfx = dis.readShort()
                def pfy = dis.readShort()
                def fs = dis.readByte()
                
                buffer = reader.readLine()
                
                println "$p1x,$p1y,$p2x,$p2y,$pfx,$pfy,$fs"
                
                //println "buffer: $buffer, size: ${buffer.size()}"
                //println "p1.x: ${Byte.valueOf(buffer[0])}"
            }
        }    
    }
}
