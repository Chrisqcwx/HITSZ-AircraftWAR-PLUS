package edu.hitsz.aircraft.enemyaircraft;

import edu.hitsz.aircraft.utils.DirectBulletGenerator;
import edu.hitsz.aircraft.utils.Params;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.utils.MotionInfo;
import edu.hitsz.aircraft.utils.BulletGenerator;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.*;
import edu.hitsz.utils.params.Direction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class EliteEnemy extends AbstractEnemyAircraft {

    /**
     * 子弹一次发射数量
     */
//    private int shootNum = Params.Init.ShootNum.ELITE;

    /**
     * 子弹伤害
     */
    private int power = Params.Init.Power.ELITE;

    private final List<Float> propWeights = Arrays.asList(30f, 30f, 30f, 10f);
    private final RandomPropGenerator propSelector = new RandomPropGenerator(propWeights);

    BulletGenerator bulletGenerator = new DirectBulletGenerator(Params.Init.ShootNum.ELITE);


    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = Direction.down;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    private BaseBullet getBullet(MotionInfo motionInfo) {
        return new EnemyBullet(motionInfo, power);
    }

    @Override
    public int getScore() {
        return 100;
    }

    @Override
    public List<BaseProp> getProps() {
        LinkedList<BaseProp> props = new LinkedList<>();
        BaseProp prop = propSelector.nextProp(locationX, locationY);
        if (prop != null) {
            props.add(prop) ;
        }
        return props;
    }


    @Override
    public List<BaseBullet> shoot() {
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speed = this.getSpeedY() + direction*5;
        return bulletGenerator.createBullets(x, y, speed, this::getBullet);
    }


}