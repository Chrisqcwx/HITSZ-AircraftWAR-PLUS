package edu.hitsz.aircraft.enemyaircraft;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BaseProp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BossEnemyTest {

    private BossEnemy boss;
    private BaseBullet bullet;

    @BeforeEach
    void setUp() {
        System.out.println("**---Executed before each test method in this class---**");
        boss = new BossEnemy(50,50,0,0,100);

    }

    @AfterEach
    void tearDown() {
        System.out.println("**---Executed after each test method in this class---**");
        boss = null;
    }

    @DisplayName("Test crash method")
    @Test
    void crash() {
        System.out.println("**---Test crash method executed---**");
        bullet = new HeroBullet(51,49,0,10,30);
        assertTrue(boss.crash(bullet));
        bullet = new HeroBullet(50,200,0,10,30);
        assertFalse(boss.crash(bullet));

    }

    @DisplayName("Test getProps method")
    @Test
    void getProps() {
        System.out.println("**---Test getProps method executed---**");
        List<BaseProp> props = boss.getProps();
        assertEquals(3, props.size());
    }
}