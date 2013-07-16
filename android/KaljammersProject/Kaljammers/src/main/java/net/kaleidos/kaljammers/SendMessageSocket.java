package net.kaleidos.kaljammers;

import java.io.Serializable;

/**
 * Created by primicachero on 16/07/13.
 */

/**
 * CLASS SendMessageSocket to send message
 */
public class SendMessageSocket implements Serializable {
    // direction to move
    //
    //  1   2   3
    //  4   x   5
    //  6   7   8

    private int direction;
    // button1 push true or false
    private boolean button1;
    // button2 push true or false
    private boolean button2;

    // getters and setters

    public int getDirection(){
        return direction;
    }
    public void setDirection(int direction){
        this.direction = direction;
    }
    public boolean isButton1(){
        return button1;
    }
    public void setButton1(boolean button1){
        this.button1 = button1;
    }
    public boolean isButton2(){
        return button2;
    }
    public void setButton2(boolean button2){
        this.button2 = button2;
    }

}
