package net.kaleidos.kaljammers;

/**
 * Created by palba on 17/07/13.
 */
public class GameEngineTwoPlayers extends GameEngine{

    ClientSocket clientSocket;


    public GameEngineTwoPlayers(){
        clientSocket = new ClientSocket();
        clientSocket.connect();
    }

    @Override
    public byte mainLoop(GameOneActivity game, float secondsElapsed, byte status, int lastMove, boolean buttonPresed){
        byte newStatus = status;

        GameField gameField = game.getGameField();
        Frisbee frisbee = game.getFrisbee();
        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        SendMessageSocket sendMessageSocket = new SendMessageSocket();

        sendMessageSocket.setDirection(lastMove);
        sendMessageSocket.setButton1(true);
        sendMessageSocket.setButton2(false);

        clientSocket.sendMessage(sendMessageSocket);


        GetMessageSocket message = clientSocket.getMessage();

        player1.setPosition(message.getPlayer1X(), message.getPlayer1Y());
        player2.setPosition(message.getPlayer2X(), message.getPlayer2Y());
        frisbee.setPosition(message.getFrisbeeX(), message.getFrisbeeY());


        return message.getFrisbeeStatus();
    }




}
