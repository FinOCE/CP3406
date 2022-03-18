package in.itsf.stopwatch;

import org.junit.Test;

import static org.junit.Assert.*;

public class StopwatchTests {
    @Test
    public void testConstructor() {
        Stopwatch stopwatch = new Stopwatch();

        assertEquals("00:00:00", stopwatch.toString());
    }

    @Test
    public void testTicker() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.tick();
        assertEquals("00:00:01", stopwatch.toString());

        for (int i = 0; i < 59; ++i) {
            stopwatch.tick();
        }
        assertEquals("00:01:00", stopwatch.toString());

        for (int i = 0; i < 59 * 60; i++) {
            stopwatch.tick();
        }
        assertEquals("01:00:00", stopwatch.toString());
    }
}