package edu.hitsz.aircraft.enemyaircraft;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ICreateReward;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.utils.MotionInfo;

import java.util.List;

/**
 * @author Chris
 */
public abstract class AbstractEnemyAircraft extends AbstractAircraft implements ICreateReward {



    public AbstractEnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    @Override
    public abstract int getScore();

    @Override
    public abstract List<BaseProp> getProps();

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }
//    @Override
//    public void forward() {
//        super.forward();
//        // 判定 y 轴向下飞行出界
//        if (locationY >= Main.WINDOW_HEIGHT ) {
//            vanish();
//        }
//    }
}
