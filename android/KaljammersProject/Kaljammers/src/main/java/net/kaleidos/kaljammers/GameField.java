package net.kaleidos.kaljammers;

/**
 * Created by palba on 17/07/13.
 */
public class GameField {

    private int limitDown = 430;
    private int limitUp = 0;
    private int limitLeft = 0;
    private int limitRight = 800;
    private int limitMiddle = 400;
    private int imageId = R.drawable.stadium;

    public GameField(){}

    public GameField(int limitDown, int limitUp, int limitLeft, int limitRight, int limitMiddle, int imageId) {
        this.limitDown = limitDown;
        this.limitUp = limitUp;
        this.limitLeft = limitLeft;
        this.limitRight = limitRight;
        this.limitMiddle = limitMiddle;
        this.imageId = imageId;
    }



    public int getLimitDown() {
        return limitDown;
    }

    public void setLimitDown(int limitDown) {
        this.limitDown = limitDown;
    }

    public int getLimitUp() {
        return limitUp;
    }

    public void setLimitUp(int limitUp) {
        this.limitUp = limitUp;
    }

    public int getLimitLeft() {
        return limitLeft;
    }

    public void setLimitLeft(int limitLeft) {
        this.limitLeft = limitLeft;
    }

    public int getLimitRight() {
        return limitRight;
    }

    public void setLimitRight(int limitRight) {
        this.limitRight = limitRight;
    }

    public int getLimitMiddle() {
        return limitMiddle;
    }

    public void setLimitMiddle(int limitMiddle) {
        this.limitMiddle = limitMiddle;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }









}
