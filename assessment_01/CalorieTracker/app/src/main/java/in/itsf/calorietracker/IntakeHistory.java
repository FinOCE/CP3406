package in.itsf.calorietracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

public class IntakeHistory implements Parcelable {
    private int mData;
    private ArrayList<Meal> meals;

    public IntakeHistory() {
        meals = new ArrayList<>();
    }

    public IntakeHistory(Meal[] mealsArray) {
        meals = new ArrayList<>();
        Collections.addAll(meals, mealsArray);
    }

    /**
     * Add a meal to the history
     * @param meal The meal to add
     */
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    /**
     * Get the meals in the history
     * @return An ArrayList of meals
     */
    public ArrayList<Meal> getMeals() {
        return meals;
    }

    /**
     * Clear the meal history
     */
    public void clearHistory() {
        meals = new ArrayList<>();
    }

    // Below constructor and methods is used to pass the history class through state changes
    // Based on the answer from https://stackoverflow.com/questions/3172333/how-to-save-an-instance-of-a-custom-class-in-onsaveinstancestate

    public IntakeHistory(Parcel in) {
        mData = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<IntakeHistory> CREATOR = new Parcelable.Creator<IntakeHistory>() {
        public IntakeHistory createFromParcel(Parcel in) {
            return new IntakeHistory(in);
        }

        public IntakeHistory[] newArray(int size) {
            return new IntakeHistory[size];
        }
    };
}
