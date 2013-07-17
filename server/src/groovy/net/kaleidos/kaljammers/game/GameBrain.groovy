package net.kaleidos.kaljammers.game

class GameBrain {
    
    Coordinate coordP1 = new Coordinate(x:0, y:0) // x,y
    Coordinate coordP2 = new Coordinate(x:0, y:0) // x,y
    Coordinate coordF = new Coordinate(x:0, y:0) // x,y
    FrisbeeStatus statusF = FrisbeeStatus.AIR // 0,1,2    
    
    def sleepMillis = 1000
    
    Long gameTime = Calendar.instance.time.time
    
    def random = new Random()
    

    private static final INSTANCE = new GameBrain()
    static getInstance(){ return INSTANCE }
    private GameBrain() {}
    
    /**
     * 
     * @param actionP1 [direction:0, b1:true, b2:true]
     * @param actionP2 [direction:0, b1:true, b2:true]
     * return Map with [coordP1:[x,y], coordP2:[x,y], coordF:[x,y], statusF:{0..2}
     */ 
    def gameProcess(actionP1, actionP2) {
        // game logic
        def info = calculateAction(actionP1, actionP2)
        
        // game time
        waitGameTime()
        
        return info
    }
     
     
    def waitGameTime() {        
        // game time
        def nowInMillis = Calendar.instance.time.time
        def waitMillis = sleepMillis - (nowInMillis - gameTime)
        gameTime = nowInMillis

        if (waitMillis > 0) {
            sleep(waitMillis)
        }
    }
    
    def calculateAction(actionP1, actionP2) {
        
        // p1
        coordP1.x = 800 //actionP1.direction
        coordP1.y = 480
        
        // F    
        coordF.x++ 
        coordF.y++ 
        
        statusF = FrisbeeStatus.AIR
        
        def info = [coordP1:coordP1, coordP2:coordP2, coordF:coordF, statusF:statusF]
        println "info: ${info}"
        
        return info
    }
}


enum FrisbeeStatus {
    AIR(0), PLAYER1(1), PLAYER2(2)
    FrisbeeStatus(int value) { this.value = value }
    private final int value
    public int value() { return value }
}

//enum Coin {
    //penny(1), nickel(5), dime(10), quarter(25)
    //Coin(int value) { this.value = value }
    //private final int value
    //public int value() { return value }
//}
