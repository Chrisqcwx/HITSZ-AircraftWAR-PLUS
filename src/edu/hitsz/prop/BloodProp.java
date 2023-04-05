package edu.hitsz.prop;

import edu.hitsz.aircraft.IAircraftForProp;

public class BloodProp extends BaseProp {

    private int increaseBlood = 100;

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(IAircraftForProp aircraft) {
        aircraft.increaseHp(increaseBlood);
        System.out.println("BloodSupply active!");
    }

}
