package edu.hitsz.aircraft;

import edu.hitsz.aircraft.utils.BulletGenerator;

/**
 * @author Chris
 */
public interface IAircraftForProp {
    /**
    ** 加血道具调用
     */
    void increaseHp(int hp);
    /**
     ** 火力道具调用
     */
    void setShootStrategy(BulletGenerator bulletGenerator);
    /**
     ** 炸弹道具调用
     */
    void addBomb(int num);
}
