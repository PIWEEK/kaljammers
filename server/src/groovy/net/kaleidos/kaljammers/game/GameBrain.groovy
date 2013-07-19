package net.kaleidos.kaljammers.game

class GameBrain {

    final static int sleepMillis = 0

    public static final int MOVE_NONE = 0
    public static final int MOVE_UP = 10
    public static final int MOVE_UP_RIGHT = 11
    public static final int MOVE_RIGHT = 1
    public static final int MOVE_DOWN_RIGHT = 21
    public static final int MOVE_DOWN = 20
    public static final int MOVE_DOWN_LEFT = 22
    public static final int MOVE_LEFT = 2
    public static final int MOVE_UP_LEFT = 12


    public static final int STATUS_PLAYER1_FRISBEE = 1;
    public static final int STATUS_PLAYER1_LAUNCH = 2;
    public static final int STATUS_PLAYER2_FRISBEE = 3;
    public static final int STATUS_PLAYER2_LAUNCH = 4;


    Integer numClients = 0

    Boolean atacking = true
    Float timePlayer2Frisbee = 0;

    Coordinate coordP1 = new Coordinate(x:0, y:0) // x,y
    Coordinate coordP2 = new Coordinate(x:0, y:0) // x,y
    Coordinate coordF = new Coordinate(x:0, y:0) // x,y
    FrisbeeStatus statusF = FrisbeeStatus.AIR // 0,1,2


    Long gameTime = Calendar.instance.time.time

    def random = new Random()

    def frisbee = [x:100, y:100, velX:500, velY:-300, width:32, height:32]
    def player1 = [x:100, y:100, vel:300, strenght:500, width:100, height:100]
    def player2 = [x:700, y:300, vel:300, strenght:500, width:100, height:100]
    def gameField = [limitUp:0, limitRight:800, limitDown:430, limitLeft:0, limitMiddle:400]
    def game = [score1:0, score2:0]
    int status = STATUS_PLAYER1_FRISBEE;


    // singleton
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

        def nowInMillis = Calendar.instance.time.time
        def timeElapsed = (nowInMillis - gameTime)/1000


        status = mainLoop(timeElapsed, actionP1.direction, actionP1.b1)

        coordP1.x = player1.x
        coordP1.y = player1.y

        coordP2.x = player2.x
        coordP2.y = player2.y

        coordF.x = frisbee.x
        coordF.y = frisbee.y


        def info = [coordP1:coordP1, coordP2:coordP2, coordF:coordF, statusF:status]
       // println "info: ${info}"

        return info
    }



    public byte mainLoop(def secondsElapsed, int lastMove, boolean buttonPresed){
        byte newStatus = status

        switch (status){
            case STATUS_PLAYER1_FRISBEE:
                movePlayer2(secondsElapsed)
                if (buttonPresed) {
                    newStatus = player1PreLaunch(lastMove)
                }
                break
            case STATUS_PLAYER2_FRISBEE:
                movePlayer1(lastMove, secondsElapsed)
                newStatus = player2Frisbee(secondsElapsed)
                break
            case STATUS_PLAYER1_LAUNCH:
                newStatus = checkGoals()
                if (newStatus==-1) {
                    moveFrisbee(secondsElapsed)
                    movePlayer1(lastMove, secondsElapsed)
                    movePlayer2(secondsElapsed)
                    newStatus = player1Launch()
                }
                break
            case STATUS_PLAYER2_LAUNCH:
                newStatus = checkGoals()
                if (newStatus==-1) {
                    moveFrisbee(secondsElapsed)
                    movePlayer1(lastMove, secondsElapsed)
                    movePlayer2(secondsElapsed)
                    newStatus = player2Launch()
                    break
                }
        }
        return newStatus
    }







    private void moveFrisbee(float timeElapsed){
        println (frisbee)
        println (timeElapsed )


        float x = frisbee.x + frisbee.velX * timeElapsed
        float y = frisbee.y + frisbee.velY * timeElapsed

        //Check limits frisbee
        if (x + frisbee.width >= gameField.limitRight){
            x = gameField.limitRight - frisbee.width
        } else if (x < gameField.limitLeft){
            x = gameField.limitLeft
        }


        if (y <= gameField.limitUp){
            y = gameField.limitUp
            frisbee.velY = -frisbee.velY
        } else if (y+frisbee.height>gameField.limitDown){
            y = gameField.limitDown-frisbee.height
            frisbee.velY = -frisbee.velY
        }

        frisbee.x = x
        frisbee.y = y
    }

    private byte player2Frisbee(float secondsElapsed){
        byte status = STATUS_PLAYER2_FRISBEE
        timePlayer2Frisbee += secondsElapsed

        if (timePlayer2Frisbee>0.6){
            //Launch player 2
            status = STATUS_PLAYER2_LAUNCH

            def velY = [player2.strenght*1.2f,player2.strenght/2,0,-player2.strenght/2, player2.strenght*1.2f]


            frisbee.x = player2.x - 20
            frisbee.y = player2.y + player2.height/2 - 16

            int y = random.nextInt(4)
            frisbee.velX = -player2.strenght
            frisbee.velY = velY[y]
            timePlayer2Frisbee = 0
        }
        return status
    }


    private byte player1PreLaunch(int lastMove){

        float velY = 0

        if (lastMove == MOVE_UP_LEFT || lastMove == MOVE_UP) {
            velY = -player1.strenght * 1.2f
        }

        if (lastMove == MOVE_UP_RIGHT) {
            velY = -player1.strenght / 2
        }

        if (lastMove == MOVE_RIGHT || lastMove == MOVE_LEFT) {
            velY = 0
        }

        if (lastMove == MOVE_DOWN_RIGHT) {
            velY = player1.strenght / 2
        }

        if (lastMove == MOVE_DOWN || lastMove == MOVE_DOWN_LEFT) {
            velY = player1.strenght * 1.2f
        }


        frisbee.x = player1.x + player1.width+5
        frisbee.y = player1.y + player1.height/2 - 16
        frisbee.velX = player1.strenght
        frisbee.velY = velY

        return STATUS_PLAYER1_LAUNCH
    }


    /////////////////////////

    private byte player2Launch(){
        byte status = STATUS_PLAYER2_LAUNCH
        //Frisbee catch player 1
        if (collision(frisbee,player1)){
            status = STATUS_PLAYER1_FRISBEE
        }
        return status
    }



    private byte player1Launch() {
        byte status = STATUS_PLAYER1_LAUNCH
        //Frisbee catch player 2
        if (collision(frisbee,player2)){
            status = STATUS_PLAYER2_FRISBEE
        }
        return status
    }

     private void movePlayer2(float secondsElapsed){
         //Move player2
         float x = player2.x
         if (atacking) {
             x -= player2.vel * secondsElapsed
         } else {
             x += player2.vel * secondsElapsed
         }

         //5% chance of change direction
         if (random.nextInt(100)<5){
             atacking = ! atacking
         }

         float fy = frisbee.y+frisbee.height/2
         float p2y = player2.y+player2.height/2
         float y = player2.y


         if (fy<p2y){
             if (p2y-fy>player2.height/2){
                 y -= player2.vel * secondsElapsed
             }
         }

         if (fy>p2y){
             if (fy-p2y>player2.height/2){
                 y += player2.vel * secondsElapsed
             }
         }


         //Check limits
         if (x<gameField.limitMiddle){
             x = gameField.limitMiddle
         } else if (x+player2.width>gameField.limitRight){
             x = gameField.limitRight - player2.width
         }

         if (y<gameField.limitUp){
             y = gameField.limitUp
         } else if (y+player2.height>gameField.limitDown){
             y = gameField.limitDown-player2.height
         }

         player2.x = x
         player2.y = y

     }


    private void movePlayer1(int lastMove, float secondsElapsed){
        //Move player1
        float x = player1.x
        float y = player1.y

        if ((lastMove == MOVE_RIGHT)||(lastMove == MOVE_UP_RIGHT)||(lastMove == MOVE_DOWN_RIGHT)) {
            x += player1.vel * secondsElapsed


        } else if ((lastMove == MOVE_LEFT)||(lastMove == MOVE_UP_LEFT)||(lastMove == MOVE_DOWN_LEFT)) {
            x -= player1.vel * secondsElapsed

        }


        if ((lastMove == MOVE_UP)||(lastMove == MOVE_UP_RIGHT)||(lastMove == MOVE_UP_LEFT)) {
            y -= player1.vel * secondsElapsed

        } else if ((lastMove == MOVE_DOWN)||(lastMove == MOVE_DOWN_LEFT)||(lastMove == MOVE_DOWN_RIGHT)) {
            y += player1.vel * secondsElapsed

        }

        //Check limits player 1
        if (x+player1.width>gameField.limitMiddle){
            x = gameField.limitMiddle-player1.width
        } else if (x<gameField.limitLeft){
            x = gameField.limitLeft
        }

        if (y<gameField.limitUp){
            y = gameField.limitUp
        } else if (y+player1.height>gameField.limitDown){
            y = gameField.limitDown-player1.height
        }


        player1.x = x
        player1.y = y
    }


    private byte checkGoals(){
        byte status=(byte) -1
        final float centerY = 800 / 2

        if(frisbee.x <= gameField.limitLeft) {
            //Player 2 goal
            game.score2 = (byte) (game.score2+3)

            status = STATUS_PLAYER1_FRISBEE

            player1.x = 0
            player1.y = centerY

            player2.x = 700
            player2.y = centerY


        } else if(frisbee.x + frisbee.width >= gameField.limitRight) {
            //Player 1 goal
            game.score1 = (byte) (game.score1+3)

            status = STATUS_PLAYER2_FRISBEE

            player1.x = 0
            player1.y = centerY

            player2.x = 700
            player2.y = centerY

        }
        return status
    }



    private collision(rect1, rect2) {
        return !((rect1.y > rect2.y+rect2.height) || (rect1.y+rect1.height < rect2.y) ||
             (rect1.x > rect2.x+rect2.width) || (rect1.x+rect1.width < rect2.x))
    }


///////////////////////




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
