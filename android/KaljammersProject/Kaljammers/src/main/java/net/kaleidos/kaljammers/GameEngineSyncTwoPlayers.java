package net.kaleidos.kaljammers;

import android.util.Log;

import java.io.IOException;
import java.util.Random;

/**
 * Created by palba on 17/07/13.
 */
public class GameEngineSyncTwoPlayers extends GameEngine{
    int serverSync = 0;

    ClientSocket clientSocket;

    public GameEngineSyncTwoPlayers(){
        Log.e("-------------------------------KALJAMMERS-------------------", "Before connect");
        clientSocket = new ClientSocket();
        clientSocket.connect();
        clientSocket.sendClientId(1000);
        try {
            //Read numclients
            clientSocket.getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
            case GameOneActivity.STATUS_PLAYER1_LAUNCH:
                newStatus = checkGoals(player1, player2, frisbee, gameField, game);
                if (newStatus==-1) {
                    moveFrisbee(frisbee, gameField, secondsElapsed);
                    movePlayer1(player1, frisbee, gameField, lastMove, secondsElapsed);
                    movePlayer2(player2, frisbee, gameField, secondsElapsed);
                    newStatus = checkFrisbeeCatch(frisbee, player1);
                }
                break;
        }
        Log.e("KALJAMMERS", "serverSync: "+serverSync);
        if (serverSync++ == 10) {
            serverSync = 0;
            //Sync with server
            SendMessageSocket sendMessageSocket = new SendMessageSocket();

            sendMessageSocket.setDirection(lastMove);
            sendMessageSocket.setButton1(true);
            sendMessageSocket.setButton2(false);

            clientSocket.sendMessage(sendMessageSocket);


            GetMessageSocket message = clientSocket.getMessage();

            Log.e("KALJAMMERS", "serverSync: READ");

            if (message != null) {
                Log.e("KALJAMMERS", "serverSync: OK");
                //player1.setPosition(message.getPlayer1X(), message.getPlayer1Y());
                player2.setPosition(message.getPlayer2X(), message.getPlayer2Y());
                //frisbee.setPosition(message.getFrisbeeX(), message.getFrisbeeY());

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



    private byte checkFrisbeeCatch(Frisbee frisbee, Player player1){
        byte status = GameOneActivity.STATUS_PLAYER1_LAUNCH;
        //Frisbee catch player 1
        if (frisbee.collidesWith(player1)){
            status = GameOneActivity.STATUS_PLAYER1_FRISBEE;
            frisbee.setVisible(false);
        }
        return status;
    }

     private void movePlayer2(Player player2, Frisbee frisbee, GameField gameField, float secondsElapsed){
         //Move player2
         float x = player2.getX();
         float y = player2.getY();

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
    }


    private byte checkGoals(Player player1, Player player2, Frisbee frisbee, GameField gameField, GameOneActivity game){
        byte status=(byte) -1;
        final float centerY = GameOneActivity.CAMERA_HEIGHT / 2;

        if(frisbee.getX() <= gameField.getLimitLeft()) {
            //Player 2 goal
            game.setScore2((byte) (game.getScore2()+3));

            status = GameOneActivity.STATUS_PLAYER1_FRISBEE;
            frisbee.setVisible(false);

            player1.setPosition(0, centerY);
            player2.setPosition(700, centerY);

        } else if(frisbee.getX() + frisbee.getWidth() >= gameField.getLimitRight()) {
            //Player 1 goal
            game.setScore1((byte) (game.getScore1()+3));
            status = GameOneActivity.STATUS_PLAYER2_FRISBEE;
            frisbee.setVisible(false);

            player1.setPosition(0, centerY);
            player2.setPosition(700, centerY);
        }
        return status;
    }


}
