package edu.hitsz.aircraft.enemyaircraft.factory;

import edu.hitsz.aircraft.enemyaircraft.AbstractEnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * @author Chris
 */
public abstract class AbstractEnemyFactory {

    public abstract AbstractEnemyAircraft createEnemy();

    protected int getLocationX() {
        return (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
    }

    protected int getLocationY() {
        return (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
    }
}
