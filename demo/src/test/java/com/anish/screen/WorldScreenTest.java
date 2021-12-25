package com.anish.screen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.KeyEvent;
import java.awt.Color;

import com.anish.calabashbros.Bullet;
import com.anish.calabashbros.Minions;

import org.junit.jupiter.api.BeforeAll;

public class WorldScreenTest {

    static WorldScreen testW;
    int Ax;
    int Ay;

    @BeforeAll
    public static void init(){
        testW = new WorldScreen(0);
        
    }


    @Test
    public void testOpen(){
        System.out.println("test1");
        assertEquals(0, testW.A.getX());
        
    }   
    @Test
    public void testAttack(){
        Bullet t = new Bullet(testW.getworld(), 1, 10, 0);
        t.run();
        assertEquals(4, testW.A.hp);
    }
    @Test
    public void testEnemy(){
        Bullet t = new Bullet(testW.getworld(), 1, 28, 0);
        Minions en = new Minions(new Color(1,1,1), 1, testW.getworld(),25,0);
        t.run();

        assertEquals(0, en.hp);
    }
    @Test
    public void testJudge(){
        testW.getworld().put(testW.getworld().setFloor(),0,0);
        assertEquals(true, testW.judge(0,0));
    }
    @Test
    public void testLoadMap(){
        testW = new WorldScreen(1);
        assertEquals(1, testW.getworld().score);
        
    }
    


}
