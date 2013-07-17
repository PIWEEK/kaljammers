package net.kaleidos.kaljammers;

import java.io.Serializable;

/**
 * Created by primicachero on 16/07/13.
 */

/**
 * CLASS GetMessageSocket to get message
 */

public class GetMessageSocket implements Serializable {
    // Coordenate X player 1
    private int player1X;
    // Coordenate Y player 1
    private int player1Y;
    // Coordenate X player 2
    private int player2X;
    // Coordenate Y player 2
    private int player2Y;
    // Coordenate X frisbee
    private int frisbeeX;
    // Coordenate Y frisbee
    private int frisbeeY;
    // frisbee status : player 1, player 2 , air
    private byte frisbeeStatus;

    // getters and setters

    public int getPlayer1X(){
        return player1X;
    }
    public int getPlayer1Y(){
        return player1Y;
    }
    public int getPlayer2X(){
        return player2X;
    }
    public int getPlayer2Y(){
        return player2Y;
    }
    public int getFrisbeeX(){
        return frisbeeX;
    }
    public int getFrisbeeY(){
        return frisbeeY;
    }
    public byte getFrisbeeStatus(){
        return frisbeeStatus;
    }
    public void setPlayer1X (int player1X){
        this.player1X = player1X;
    }
    public void setPlayer1Y (int player1Y){
        this.player1Y = player1Y;
    }
    public void setPlayer2X (int player2X){
        this.player2X = player2X;
    }
    public void setPlayer2Y (int player2Y){
        this.player2Y = player2Y;
    }
    public void setFrisbeeX (int frisbeeX){
        this.frisbeeX = frisbeeX;
    }
    public void setFrisbeeY (int frisbeeY){
        this.frisbeeY = frisbeeY;
    }

    public void setFrisbeeStatus(byte frisbeeStatus){
        this.frisbeeStatus = frisbeeStatus;
    }

}
