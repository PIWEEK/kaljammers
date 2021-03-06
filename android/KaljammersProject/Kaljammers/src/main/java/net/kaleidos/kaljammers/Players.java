package net.kaleidos.kaljammers;

/**
 * Created by antonio on 18/07/13.
 */
public enum Players {
    PLAYER1(1.5f,0.5f,Player.SPRITE_NINJA),
    PLAYER2(1.2f,0.8f,Player.SPRITE_PRIMI),
    PLAYER3(0.8f,1.2f,Player.SPRITE_TONYO),
    PLAYER4(0.5f,1.5f,Player.SPRITE_PABLO);

    private float vel;
    private float strenght;
    private String sprite;

    Players(float vel, float strenght, String sprite) {
        this.vel = vel;
        this.strenght = strenght;
        this.sprite = sprite;
    }

    public float getVel() {
        return vel;
    }

    public float getStrenght() {
        return strenght;
    }

    public String getSprite() {
        return sprite;
    }
}
