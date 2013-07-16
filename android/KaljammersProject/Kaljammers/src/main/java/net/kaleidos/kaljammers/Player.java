package net.kaleidos.kaljammers;

import android.util.Log;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends Sprite {

    private static final int FIELD_LIMIT_UP = 50;
    private static final int FIELD_LIMIT_DOWN = 480;
    private static final int FIELD_LIMIT_RIGHT = 395;
    private static final int FIELD_LIMIT_LEFT = 0;

    private static final int CAMERA_RIGHT = 800;

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public void setPlayer1(boolean player1) {
        isPlayer1 = player1;
    }

    private boolean isPlayer1 = false;


    public float getVel() {
        return vel;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    private float vel = 50f;

    public float getStrength() {
        return strength;
    }

    private float strength = 600f;

    public Player(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        Log.v("[PLAYER position]","------------------>  (x,y):("+this.mX+","+this.mY+")");
        if (isPlayer1) {
            if(this.mX < FIELD_LIMIT_LEFT) {
                this.mX = FIELD_LIMIT_LEFT;
            } else if(this.mX + this.getWidth() > FIELD_LIMIT_RIGHT) {
                this.mX = FIELD_LIMIT_RIGHT - this.getWidth();
            }
        } else {
            if(this.mX < FIELD_LIMIT_RIGHT) {
                this.mX = FIELD_LIMIT_RIGHT;
            } else if(this.mX + this.getWidth() > CAMERA_RIGHT) {
                this.mX = CAMERA_RIGHT - this.getWidth();
            }
        }

        if(this.mY < FIELD_LIMIT_UP) {
            this.mY = FIELD_LIMIT_UP;
        } else if(this.mY + this.getHeight() > FIELD_LIMIT_DOWN) {
            this.mY = FIELD_LIMIT_DOWN - this.getHeight();
        }

        super.onManagedUpdate(pSecondsElapsed);
    }
}

