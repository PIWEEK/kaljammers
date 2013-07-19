package net.kaleidos.kaljammers.server

class KaljammersClient {

    def serverPort
    def serverHost = "localhost"
    Integer clientId = null
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
        
            def reader = input.newReader()
            def dis = new DataInputStream(input)
            def dos = new DataOutputStream(output)
        
            //clientToken = UUID.randomUUID().toString().replaceAll('-', '')
            //println "Client token ${clientToken}..."
            //output << "$clientToken\n"           
            clientId = dis.readByte() as Integer
            def buffer = reader.readLine()
            println "clientId = $clientId"
            
            // if player 1 send game info
            println "sending info"
            if (clientId == 0) {
                dos.writeByte(1) 
                dos.writeByte(3) 
                dos << "\n"
            } 
            // else receive
            else {          
                dos.writeByte(2) 
                dos << "\n"
            }
            
            // receive info
            println "receiving info"
            def player1 = dis.readByte()
            def player2 = dis.readByte()
            def field = dis.readByte()                
            buffer = reader.readLine()
            
            println "selectP1:$player1, selectP2:$player2, field:field"
            
            def p1x = 0
            def p1y = 0
            
            while (true) {
                def move = [px:p1x++, py:p1y++, fx:clientId, fy:clientId, sfx:5, sfy:5, isGoal:true, isPick:true]
                println "mymove: $move.px,$move.py,$move.fx,$move.fy,$move.sfx,$move.sfy,$move.isGoal,$move.isPick"
                
                dos.writeFloat(move.px) //2
                dos.writeFloat(move.py)
                dos.writeFloat(move.fx)
                dos.writeFloat(move.fy)
                dos.writeFloat(move.sfx)
                dos.writeFloat(move.sfy)
                dos.writeBoolean(move.isGoal)
                dos.writeBoolean(move.isPick)
                dos << "\n"
                
                def px = dis.readFloat()
                def py = dis.readFloat()
                def fx = dis.readFloat()
                def fy = dis.readFloat()
                def sfx = dis.readFloat()
                def sfy = dis.readFloat()
                def isGoal = dis.readBoolean()
                def isPick = dis.readBoolean()
                
                buffer = reader.readLine()
                
                println "yourmove: $px,$py,$fx,$fy,$sfx,$sfy,$isGoal,$isPick"
            }
        }    
    }
}
