package net.kaleidos.kaljammers;

/**
 * Created by palba on 17/07/13.
 */
public abstract class GameEngine {
    public abstract byte mainLoop(GameOneActivity game, float secondsElapsed, byte status, int lastMove, boolean buttonPresed);
}
