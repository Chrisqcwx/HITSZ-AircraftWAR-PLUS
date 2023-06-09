package edu.hitsz.aircraft.enemyaircraft;

import edu.hitsz.aircraft.utils.Params;
import edu.hitsz.aircraft.utils.RoundBulletGenerator;
import edu.hitsz.aircraft.utils.BulletGenerator;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.prop.BaseProp;
import edu.hitsz.prop.RandomPropGenerator;
import edu.hitsz.utils.MotionInfo;
import edu.hitsz.utils.params.Direction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class BossEnemy extends AbstractEnemyAircraft {
    /**
     * 子弹一次发射数量
     */
    private int shootNum = Params.Init.ShootNum.BOSS;

    /**
     * 子弹伤害
     */
    private int power = Params.Init.Power.BOSS;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = Direction.down;

    private final List<Float> propWeights = Arrays.asList(30f, 30f, 30f);
    private final RandomPropGenerator propSelector = new RandomPropGenerator(propWeights);

    BulletGenerator bulletGenerator = new RoundBulletGenerator(Params.Init.ShootNum.BOSS);

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    private BaseBullet getBullet(MotionInfo motionInfo) {
        return new EnemyBullet(motionInfo, power);
    }

    @Override
    public int getScore() {
        return 300;
    }

    @Override
    public List<BaseProp> getProps() {
        LinkedList<BaseProp> props = new LinkedList<>();
        IntStream.range(0,3).forEach(num -> {
            BaseProp prop = propSelector.nextProp(locationX+30*(num-1), locationY);
            props.add(prop) ;
        });
        return props;
    }


    @Override
    public List<BaseBullet> shoot() {
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speed = this.getSpeedY() + direction*10;
        return bulletGenerator.createBullets(x,y,speed, this::getBullet);
    }
}
