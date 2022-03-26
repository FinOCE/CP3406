package in.itsf.calorietracker;

import java.util.Date;

public class Meal {
    private final Date time;
    private final int calories;

    public Meal(Date time, int calories) {
        this.time = time;
        this.calories = calories;
    }

    /**
     * Get the time the meal was eaten
     * @return The time the meal was eaten
     */
    public Date getDate() {
        return time;
    }

    /**
     * Get the calories contained in the meal
     * @return The number of calories in the meal
     */
    public int getCalories() {
        return calories;
    }
}
