package com.anish.calabashbros;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulletTest {
    @Test
    void testRun() {

        World t = new World(0);
        Bullet x = new Bullet(t, 1, 29, 0);
        x.run();
        assertEquals(0,x.getX());
        assertEquals(-1,x.dir);
    }
}
