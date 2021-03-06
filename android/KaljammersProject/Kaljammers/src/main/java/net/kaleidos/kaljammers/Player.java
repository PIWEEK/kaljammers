package net.kaleidos.kaljammers;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends AnimatedSprite {


    private static final float STANDARD_VEL = 350f;
    private static final float STANDARD_STRENGTH = 600f;

    public final static String SPRITE_PABLO = "gpx/pablo.png";
    public final static String SPRITE_PRIMI = "gpx/primi.png";
    public final static String SPRITE_TONYO = "gpx/tonyo.png";
    public final static String SPRITE_NINJA = "gpx/player4.png";
    public final static String SPRITE_OPPONENT = "gpx/player4.png";
    public final static String SPRITE_SPACE = "gpx/space.png";

    private float vel = STANDARD_VEL;
    private float strength = STANDARD_STRENGTH;
    private boolean isPlayer1 = false;

    private int lastMove = GameOneActivity.MOVE_RIGHT;
    private int numMoves = 0;

    public Player(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, (TiledTextureRegion) pTextureRegion, pVertexBufferObjectManager);
        this.setCurrentTileIndex(8);
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public void setPlayer1(boolean player1) {
        isPlayer1 = player1;
    }

    public float getVel() {
        return vel;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void applyFactorVel(float f) {
        this.vel = this.vel * f;
    }

    public void applyFactorStrength(float f) {
        this.strength = this.strength * f;
    }


    public void animate(int direction){
        boolean change=false;

        if (direction == lastMove){
            numMoves++;
            if (numMoves==5) {
                change = true;
                numMoves = 0;
            }
        } else {
            change = true;
            numMoves = 0;
        }

        if (change) {

            if (direction == GameOneActivity.MOVE_DOWN) {
                this.nextAnimation(0, 3);
            }

            if (direction == GameOneActivity.MOVE_UP) {
                this.nextAnimation(12, 15);
            }


            if ((direction== GameOneActivity.MOVE_LEFT)||(direction == GameOneActivity.MOVE_DOWN_LEFT)||(direction == GameOneActivity.MOVE_UP_LEFT)){
                if (this.isPlayer1()) {
                    this.nextAnimation(4, 7);
                } else {
                    this.nextAnimation(8, 11);
                }
            }

            if ((direction== GameOneActivity.MOVE_RIGHT)||(direction == GameOneActivity.MOVE_DOWN_RIGHT)||(direction == GameOneActivity.MOVE_UP_RIGHT)){
                if (this.isPlayer1()) {
                    this.nextAnimation(8, 11);
                } else {
                    this.nextAnimation(4, 7);
                }
            }

            if (direction == GameOneActivity.MOVE_NONE){
                if (this.isPlayer1()) {
                    this.setCurrentTileIndex(8);
                } else {
                    this.setCurrentTileIndex(4);
                }

            }
        }

        lastMove = direction;
    }


    private void nextAnimation(int min, int max){
        int current = this.getCurrentTileIndex();
        if ((current >= min) && (current <= max)){
            current++;
            if (current > max){
                current = min;
            }
        } else {
            current = min;
        }
        this.setCurrentTileIndex(current);
    }
}

