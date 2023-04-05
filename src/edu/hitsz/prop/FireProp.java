package edu.hitsz.prop;

import edu.hitsz.aircraft.IAircraftForProp;
import edu.hitsz.aircraft.utils.RoundBulletGenerator;

public class FireProp extends BaseProp {
    private final int bulletNum = 3;

    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(IAircraftForProp aircraft) {
        aircraft.setShootStrategy(new RoundBulletGenerator(bulletNum));
        System.out.println("FireSupply active!");
    }
}
