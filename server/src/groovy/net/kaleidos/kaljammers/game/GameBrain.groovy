package net.kaleidos.kaljammers.game

class GameBrain {
    
    def coordP1 = [x:0, y:0] // x,y
    def coordP2 = [x:0, y:0]
    def coordF  = [x:0, y:0]
    def statusF = 0 // 0,1,2
    
    
    def sleepMillis = 1000
    

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
        
        // p1
        coordP1.x += actionP1.direction
        coordP1.y += actionP1.direction
        
        // F
        coordF.x++ 
        coordF.y++ 
        
        statusF = 0
        
        
        // game time
        sleep(sleepMillis)
        
        return [coordP1, coordP2, coordF, statusF]
    }
    
}
