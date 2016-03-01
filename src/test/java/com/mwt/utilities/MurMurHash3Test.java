package com.mwt.utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by jmorra on 2/29/16.
 */
public class MurMurHash3Test {

    @Test
    public void sameValue() {
        String value = "abc";
        assertEquals(MurMurHash3.computeIdHash(value), MurMurHash3.computeIdHash(value));
    }

    @Test
    public void differentValues() {
        assertNotEquals(MurMurHash3.computeIdHash("abc"), MurMurHash3.computeIdHash("def"));
    }
}
