package edu.hitsz.prop;

import edu.hitsz.aircraft.IAircraftForProp;

public class BombProp extends BaseProp {

    private int bombNum = 1;

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(IAircraftForProp aircraft) {
        aircraft.addBomb(bombNum);
        System.out.println("BombSupply active!");
    }
}
