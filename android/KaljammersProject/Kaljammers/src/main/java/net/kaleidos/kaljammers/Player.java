package net.kaleidos.kaljammers;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends Sprite {

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

}

