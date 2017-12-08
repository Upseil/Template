package com.upseil.game.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.badlogic.gdx.math.RandomXS128;

public class RestoreRandomTest {
    
    @Test
    public void testRestoreRandom() {
        RandomXS128 random = new RandomXS128();
        long seed0 = random.getState(0);
        long seed1 = random.getState(1);

        long before1 = random.nextLong();
        long before2 = random.nextLong();
        long before3 = random.nextLong();
        
        random.setState(seed0, seed1);

        long after1 = random.nextLong();
        long after2 = random.nextLong();
        long after3 = random.nextLong();

        assertThat(before1, is(after1));
        assertThat(before2, is(after2));
        assertThat(before3, is(after3));
    }
    
}
