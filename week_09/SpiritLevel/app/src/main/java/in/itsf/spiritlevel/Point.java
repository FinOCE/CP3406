package in.itsf.spiritlevel;

import java.util.Locale;

public class Point {
    public float x;
    public float y;

    public Point() {}

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "x %.2f y %.2f", x, y);
    }
}
