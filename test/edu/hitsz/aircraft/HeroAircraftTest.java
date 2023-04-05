package edu.hitsz.aircraft;

import edu.hitsz.aircraft.utils.Params;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        System.out.println("**---Executed before each test method in this class---**");
        heroAircraft = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**---Executed after each test method in this class---**");
        heroAircraft = null;
    }

    @DisplayName("Test getInstance Method")
    @Test
    void getInstance() {
        System.out.println("**---Test getInstance method executed---**");
        assertNotNull(HeroAircraft.getInstance());
    }

    @DisplayName("Test increase Method")
    @Test
    void increaseHp() {
        System.out.println("**---Test increaseHp method executed---**");
        heroAircraft.decreaseHp(100);
        heroAircraft.increaseHp(200);
        assertTrue(heroAircraft.getHp()<= Params.Init.Hp.HERO);
    }
}