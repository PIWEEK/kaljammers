package net.kaleidos.kaljammers.game

/**
 * Game communication for two players
 */ 
class GameComm {

    public static final Integer MAX_PLAYERS = 2

    Integer numClients = 0
    List playersInfo = []
    def gameInfo = [player1:0, player2:0, field:0]
    
    // singleton
    private static final INSTANCE = new GameComm()
    static getInstance(){ return INSTANCE }
    private GameComm() {
            playersInfo[0] = [px:0, py:200, fx:100, fy:200, sfx:600, sfy:600, isGoal:false, isPick:true]
            playersInfo[1] = [px:0, py:200, fx:700, fy:200, sfx:-600, sfy:600, isGoal:false, isPick:false]
        }

    /**
     *
     * @param id client id
     * @param action [px:px, py:py, fx:fx, fy:fy, sfx:sfx, sfy:sfy: isGoal:isGoal, isPick:isPick]
     * return Map with [coordP1:[x,y], coordP2:[x,y], coordF:[x,y], statusF:{0..2}
     */
    def saveMoveInfo(id, action) {        
        // save action player
        playersInfo[id] = action
        
        println "playersInfo: $playersInfo"
        
        // return other player info
        def opponent = (id + 1) % MAX_PLAYERS
        return playersInfo[opponent]
    }
    
    def void saveGameInfo(id, info) {
        println "id: $id, info:$info"
        if (id == 0) {
            this.gameInfo.player1 = info.player
            this.gameInfo.field = info.field
        } else {
            this.gameInfo.player2 = info.player
        }       
        println "gameinfo: $gameInfo" 
    }
}
