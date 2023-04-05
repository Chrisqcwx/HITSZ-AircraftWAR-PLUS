package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.utils.Params;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloodPropTest {

    private HeroAircraft heroAircraft;
    private BloodProp bloodProp;

    @BeforeEach
    void setUp() {
        System.out.println("**---Executed before each test method in this class---**");
        heroAircraft = HeroAircraft.getInstance();
        bloodProp = new BloodProp(0,0,0,0);
    }

    @AfterEach
    void tearDown() {
        System.out.println("**---Executed after each test method in this class---**");
        heroAircraft = null;
    }

    @DisplayName("Test vanish method")
    @Test
    void vanish() {
        System.out.println("**---Test vanish method executed---**");
        assertFalse(bloodProp.notValid());
        bloodProp.vanish();
        assertTrue(bloodProp.notValid());
    }

    @DisplayName("Test effect method")
    @Test
    void effect() {
        System.out.println("**---Test effect method executed---**");
        int decreaseHp = 300;
        heroAircraft.decreaseHp(decreaseHp);
        bloodProp.effect(heroAircraft);
        assertEquals(Params.Init.Hp.HERO-decreaseHp+100, heroAircraft.getHp());
    }
}