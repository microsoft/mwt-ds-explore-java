package com.mwt.utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jmorra on 2/29/16.
 */
public class PRGTest {

    @Test
    public void uniform() {
        PRG prg = new PRG(1);
        float actual = prg.uniformUnitInterval();
        assertEquals(0.40170848f, actual, 1e-5f);
    }

    @Test
    public void uniformInt() {
        PRG prg = new PRG(1);
        int actual = prg.uniformInt(0, 9);
        assertEquals(7, actual);
    }
}
