package test;

import org.junit.Test;
import team04.project3.util.Util;

import static org.junit.Assert.assertEquals;

public class TestUtil {
    @Test
    public void testIsInteger(){
        assertEquals(true, Util.isInteger("10"));
        assertEquals(true,Util.isInteger("-321"));
        assertEquals(false,Util.isInteger("hello"));
        assertEquals(false,Util.isInteger("ten10"));
        assertEquals(true,Util.isInteger("0"));
    }
}
