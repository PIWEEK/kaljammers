package net.kaleidos.kaljammers;

import java.util.Random;

/**
 * Created by palba on 17/07/13.
 */
public class GameEngineOnePlayer extends GameEngine{

    float timePlayer2Frisbee = 0;
    boolean atacking = true;
    Random random = new Random();


    @Override
    public byte mainLoop(GameOneActivity game, float secondsElapsed, byte status, int lastMove, boolean buttonPresed){
        byte newStatus = status;

        GameField gameField = game.getGameField();
        Frisbee frisbee = game.getFrisbee();
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        switch (status){
            case GameOneActivity.STATUS_PLAYER1_FRISBEE:
                movePlayer2(player2, frisbee, gameField, secondsElapsed);
                if (buttonPresed) {
                    newStatus = player1PreLaunch(lastMove, player1, frisbee);
                }
                break;
            case GameOneActivity.STATUS_PLAYER2_FRISBEE:
                movePlayer1(player1, frisbee, gameField, lastMove, secondsElapsed);
                newStatus = player2Frisbee(player2, frisbee, secondsElapsed);
                break;
            case GameOneActivity.STATUS_PLAYER1_LAUNCH:
                newStatus = checkGoals(player1, player2, frisbee, gameField, game);
                if (newStatus==-1) {
                    moveFrisbee(frisbee, gameField, secondsElapsed);
                    movePlayer1(player1, frisbee, gameField, lastMove, secondsElapsed);
                    movePlayer2(player2, frisbee, gameField, secondsElapsed);
                    newStatus = player1Launch(frisbee, player2);
                }
                break;
            case GameOneActivity.STATUS_PLAYER2_LAUNCH:
                newStatus = checkGoals(player1, player2, frisbee, gameField, game);
                if (newStatus==-1) {
                    moveFrisbee(frisbee, gameField, secondsElapsed);
                    movePlayer1(player1, frisbee, gameField, lastMove, secondsElapsed);
                    movePlayer2(player2, frisbee, gameField, secondsElapsed);
                    newStatus = player2Launch(frisbee, player1);
                    break;
                }
        }
        return newStatus;
    }



    private void moveFrisbee(Frisbee frisbee, GameField gameField, float timeElapsed){
        float x = frisbee.getX() + frisbee.getVelX()*timeElapsed;
        float y = frisbee.getY() + frisbee.getVelY()*timeElapsed;

        //Check limits frisbee
        if (x+frisbee.getWidth()>=gameField.getLimitRight()){
            x = gameField.getLimitRight()-frisbee.getWidth();
        } else if (x<gameField.getLimitLeft()){
            x = gameField.getLimitLeft();
        }


        if (y<=gameField.getLimitUp()){
            y = gameField.getLimitUp();
            frisbee.setVelY(-frisbee.getVelY());
        } else if (y+frisbee.getHeight()>gameField.getLimitDown()){
            y = gameField.getLimitDown()-frisbee.getHeight();
            frisbee.setVelY(-frisbee.getVelY());
        }

        frisbee.setPosition(x, y);
    }


    private byte player2Frisbee(Player player2, Frisbee frisbee, float secondsElapsed){
        byte status = GameOneActivity.STATUS_PLAYER2_FRISBEE;
        timePlayer2Frisbee += secondsElapsed;

        if (timePlayer2Frisbee>0.6){
            //Launch player 2
            status = GameOneActivity.STATUS_PLAYER2_LAUNCH;

            float[] velY = {player2.getStrength()*1.2f,player2.getStrength()/2,0,-player2.getStrength()/2, player2.getStrength()*1.2f};


            frisbee.setPosition(player2.getX() - 20, player2.getY()+ player2.getHeight()/2 - 16);

            int y = random.nextInt(4);
            frisbee.setVelX(-player2.getStrength());
            frisbee.setVelY(velY[y]);

            frisbee.setVisible(true);
            timePlayer2Frisbee = 0;
        }
        return status;
    }


    private byte player1PreLaunch(int lastMove, Player player1, Frisbee frisbee){

        float velY = 0;

        if (lastMove == GameOneActivity.MOVE_UP_LEFT || lastMove == GameOneActivity.MOVE_UP) {
            velY = -player1.getStrength() * 1.2f;
        }

        if (lastMove == GameOneActivity.MOVE_UP_RIGHT) {
            velY = -player1.getStrength() / 2;
        }

        if (lastMove == GameOneActivity.MOVE_RIGHT || lastMove == GameOneActivity.MOVE_LEFT) {
            velY = 0;
        }

        if (lastMove == GameOneActivity.MOVE_DOWN_RIGHT) {
            velY = player1.getStrength() / 2;
        }

        if (lastMove == GameOneActivity.MOVE_DOWN || lastMove == GameOneActivity.MOVE_DOWN_LEFT) {
            velY = player1.getStrength() * 1.2f;
        }


        frisbee.setPosition(player1.getX()+ player1.getWidth()+5, player1.getY()+ player1.getHeight()/2 - 16);
        frisbee.setVelX(player1.getStrength());
        frisbee.setVelY(velY);

        frisbee.setVisible(true);

        return GameOneActivity.STATUS_PLAYER1_LAUNCH;
    }


    private byte player2Launch(Frisbee frisbee, Player player1){
        byte status = GameOneActivity.STATUS_PLAYER2_LAUNCH;
        //Frisbee catch player 1
        if (frisbee.collidesWith(player1)){
            frisbee.setVisible(false);
            status = GameOneActivity.STATUS_PLAYER1_FRISBEE;
            player1.animate(GameOneActivity.MOVE_NONE);
        }
        return status;
    }



    private byte player1Launch(Frisbee frisbee, Player player2){
        byte status = GameOneActivity.STATUS_PLAYER1_LAUNCH;
        //Frisbee catch player 2
        if (frisbee.collidesWith(player2)){
            status = GameOneActivity.STATUS_PLAYER2_FRISBEE;
            frisbee.setVisible(false);
            player2.animate(GameOneActivity.MOVE_NONE);
        }
        return status;
    }

     private void movePlayer2(Player player2, Frisbee frisbee, GameField gameField, float secondsElapsed){
         //Move player2
         float x = player2.getX();
         if (atacking) {
             x -= player2.getVel() * secondsElapsed;
         } else {
             x += player2.getVel() * secondsElapsed;
         }

         //5% chance of change direction
         if (random.nextInt(100)<5){
             atacking = ! atacking;
         }

         float fy = frisbee.getY()+frisbee.getHeight()/2;
         float p2y = player2.getY()+player2.getHeight()/2;
         float y = player2.getY();


         if (fy<p2y){
             if (p2y-fy>player2.getHeight()/2){
                 y -= player2.getVel() * secondsElapsed;
             }
         }

         if (fy>p2y){
             if (fy-p2y>player2.getHeight()/2){
                 y += player2.getVel() * secondsElapsed;
             }
         }


         //Check limits
         if (x<gameField.getLimitMiddle()){
             x = gameField.getLimitMiddle();
             atacking = !atacking;
         } else if (x+player2.getWidth()>gameField.getLimitRight()){
             x = gameField.getLimitRight() - player2.getWidth();
             atacking = !atacking;
         }

         if (y<gameField.getLimitUp()){
             y = gameField.getLimitUp();
         } else if (y+player2.getHeight()>gameField.getLimitDown()){
             y = gameField.getLimitDown()-player2.getHeight();
         }


        if (player2.getX()>x) {
            player2.animate(GameOneActivity.MOVE_RIGHT);
        } else if (player2.getX()<x) {
            player2.animate(GameOneActivity.MOVE_LEFT);
        }

         player2.setPosition(x, y);

     }
    
    
    private void movePlayer1(Player player1, Frisbee frisbee, GameField gameField, int lastMove, float secondsElapsed){
        //Move player1
        float x = player1.getX();
        float y = player1.getY();

        if ((lastMove == GameOneActivity.MOVE_RIGHT)||(lastMove == GameOneActivity.MOVE_UP_RIGHT)||(lastMove == GameOneActivity.MOVE_DOWN_RIGHT)) {
            x += player1.getVel() * secondsElapsed;


        } else if ((lastMove == GameOneActivity.MOVE_LEFT)||(lastMove == GameOneActivity.MOVE_UP_LEFT)||(lastMove == GameOneActivity.MOVE_DOWN_LEFT)) {
            x -= player1.getVel() * secondsElapsed;

        }


        if ((lastMove == GameOneActivity.MOVE_UP)||(lastMove == GameOneActivity.MOVE_UP_RIGHT)||(lastMove == GameOneActivity.MOVE_UP_LEFT)) {
            y -= player1.getVel() * secondsElapsed;

        } else if ((lastMove == GameOneActivity.MOVE_DOWN)||(lastMove == GameOneActivity.MOVE_DOWN_LEFT)||(lastMove == GameOneActivity.MOVE_DOWN_RIGHT)) {
            y += player1.getVel() * secondsElapsed;

        }

        //Check limits player 1
        if (x+player1.getWidth()>gameField.getLimitMiddle()){
            x = gameField.getLimitMiddle()-player1.getWidth();
        } else if (x<gameField.getLimitLeft()){
            x = gameField.getLimitLeft();
        }

        if (y<gameField.getLimitUp()){
            y = gameField.getLimitUp();
        } else if (y+player1.getHeight()>gameField.getLimitDown()){
            y = gameField.getLimitDown()-player1.getHeight();
        }


        player1.setPosition(x, y);
        player1.animate(lastMove);
    }


    private byte checkGoals(Player player1, Player player2, Frisbee frisbee, GameField gameField, GameOneActivity game){
        byte status=(byte) -1;
        final float centerY = gameField.getLimitDown() / 2 - player1.getHeight();

        if(frisbee.getX() <= gameField.getLimitLeft()) {
            //Player 2 goal
            game.setScore2((byte) (game.getScore2()+3));

            status = GameOneActivity.STATUS_PLAYER1_FRISBEE;
            frisbee.setVisible(false);

            player1.setPosition(0, centerY);
            player2.setPosition(700, centerY);
            player1.animate(GameOneActivity.MOVE_NONE);
            player2.animate(GameOneActivity.MOVE_NONE);

        } else if(frisbee.getX() + frisbee.getWidth() >= gameField.getLimitRight()) {
            //Player 1 goal
            game.setScore1((byte) (game.getScore1()+3));
            status = GameOneActivity.STATUS_PLAYER2_FRISBEE;
            frisbee.setVisible(false);

            player1.setPosition(0, centerY);
            player2.setPosition(700, centerY);
            player1.animate(GameOneActivity.MOVE_NONE);
            player2.animate(GameOneActivity.MOVE_NONE);
        }
        return status;
    }


}
