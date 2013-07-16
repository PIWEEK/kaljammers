package net.kaleidos.kaljammers;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends Sprite {

    private static final int FIELD_LIMIT_UP = 50;
    private static final int FIELD_LIMIT_DOWN = 480;
    private static final int FIELD_LIMIT_RIGHT = 395;
    private static final int FIELD_LIMIT_LEFT = 0;

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    private float velX = 7f;
    private float velY = 7f;

    public float getStrength() {
        return strength;
    }

    private float strength = 500f;

    public Player(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

    }

    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(this.mX < FIELD_LIMIT_LEFT) {
            this.mX = FIELD_LIMIT_LEFT;
        } else if(this.mX + this.getWidth() > FIELD_LIMIT_RIGHT) {
            this.mX = FIELD_LIMIT_RIGHT - this.getWidth();
        }

        if(this.mY < FIELD_LIMIT_UP) {
            this.mY = FIELD_LIMIT_UP;
        } else if(this.mY + this.getHeight() > FIELD_LIMIT_DOWN) {
            this.mY = FIELD_LIMIT_DOWN - this.getHeight();
        }

        super.onManagedUpdate(pSecondsElapsed);
    }
}

