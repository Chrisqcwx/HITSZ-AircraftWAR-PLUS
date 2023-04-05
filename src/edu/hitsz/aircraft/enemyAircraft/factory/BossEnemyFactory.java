package edu.hitsz.aircraft.enemyaircraft.factory;

import edu.hitsz.aircraft.enemyaircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.enemyaircraft.BossEnemy;
import edu.hitsz.aircraft.utils.Params;

public class BossEnemyFactory extends AbstractEnemyFactory {

    private int speedX;
    private int speedY;
    private int hp;

    public BossEnemyFactory() {
        speedX = Params.Init.SpeedX.BOSS;
        speedY = Params.Init.SpeedY.BOSS;
        hp = Params.Init.Hp.BOSS;
    }

    @Override
    public AbstractEnemyAircraft createEnemy() {
        return new BossEnemy(super.getLocationX(), super.getLocationY(), speedX, speedY, hp);
    }
}
