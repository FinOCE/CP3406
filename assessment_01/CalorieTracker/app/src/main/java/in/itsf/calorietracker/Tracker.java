package in.itsf.calorietracker;

import androidx.annotation.NonNull;

public class Tracker {
    public static final int DEFAULT_REQUIRED = 2000;

    private int calIntake;
    private int calRequired;

    public Tracker(int intake, int required) {
        calIntake = intake;
        calRequired = required;
    }

    @NonNull
    @Override
    public String toString() {
        return getCalorieIntake() + " / " + getRequiredCalories();
    }

    /**
     * Set the required amount of daily calories
     * @param required The amount of calories required daily
     */
    public void setCaloriesRequired(int required) {
        calRequired = required;
    }

    /**
     * Add calories to the daily intake
     * @param intake The amount of calories to add
     */
    public void addCalories(int intake) {
        calIntake += intake;
    }

    /**
     * Reset caloric intake to 0
     */
    public void clearCalories() {
        calIntake = 0;
    }

    /**
     * Get the amount of calories eaten
     * @return The number of calories eaten
     */
    public int getCalorieIntake() {
        return calIntake;
    }

    /**
     * Get the amount of calories required daily
     * @return The number of calories required
     */
    public int getRequiredCalories() {
        return calRequired;
    }
}
