package in.itsf.quiz;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import in.itsf.quiz.helper.DatabaseHelper;

public class DatabaseHelperUnitTest {
    @Test
    public void getListOptionsTest() {
        List<String> options = new ArrayList<>();
        assertEquals("()", DatabaseHelper.getListOptions(options));

        options.add("1");
        assertEquals("(1)", DatabaseHelper.getListOptions(options));

        options.add("2");
        assertEquals("(1,2)", DatabaseHelper.getListOptions(options));

        options.add("3");
        assertEquals("(1,2,3)", DatabaseHelper.getListOptions(options));
    }
}
