package edu.hitsz.aircraft;

import edu.hitsz.aircraft.utils.DirectBulletGenerator;
import edu.hitsz.utils.MotionInfo;
import edu.hitsz.aircraft.utils.Params;
import edu.hitsz.aircraft.utils.BulletGenerator;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.utils.params.Direction;

import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft implements IAircraftForProp {

    /**
     * 子弹一次发射数量
     */
//    private int shootNum = Params.Init.ShootNum.HERO;

    /**
     * 子弹伤害
     */
    private int power = Params.Init.Power.HERO;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = Direction.up;

    BulletGenerator bulletGenerator = new DirectBulletGenerator(Params.Init.ShootNum.HERO);

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    private static volatile HeroAircraft heroAircraft = null;

    public static HeroAircraft getInstance() {
        if (heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if (heroAircraft == null) {

                    heroAircraft = new HeroAircraft(
                            Params.Init.LocationX.HERO,
                            Params.Init.LocationY.HERO,
                            Params.Init.SpeedX.HERO,
                            Params.Init.SpeedY.HERO,
                            Params.Init.Hp.HERO
                    );
                }
            }
        }
        return heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    private BaseBullet getBullet(MotionInfo motionInfo) {
        return new HeroBullet(motionInfo, power);
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speed = this.getSpeedY() + direction*10;
        return bulletGenerator.createBullets(x, y, speed, this::getBullet);
    }

    @Override
    public void increaseHp(int hp) {
        this.hp = Math.min(this.hp + hp, this.maxHp);
    }

    @Override
    public void setShootStrategy(BulletGenerator bulletGenerator) {
        this.bulletGenerator = bulletGenerator;
    }

    @Override
    public void addBomb(int num) {

    }
}
