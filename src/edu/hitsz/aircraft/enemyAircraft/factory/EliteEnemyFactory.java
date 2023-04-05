package edu.hitsz.aircraft.enemyaircraft.factory;

import edu.hitsz.aircraft.enemyaircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.enemyaircraft.EliteEnemy;
import edu.hitsz.aircraft.utils.Params;

import java.awt.desktop.SystemEventListener;

public class EliteEnemyFactory extends AbstractEnemyFactory {

    private int speedX;
    private int speedY;
    private int hp;

    public EliteEnemyFactory() {
        speedX = Params.Init.SpeedX.ELITE;
        speedY = Params.Init.SpeedY.ELITE;
        hp = Params.Init.Hp.ELITE;
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        return new EliteEnemy(super.getLocationX(), super.getLocationY(), speedX, speedY, hp);
    }
}

