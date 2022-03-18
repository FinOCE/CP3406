package in.itsf.stopwatch;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Stopwatch {
    private int hours, minutes, seconds;

    Stopwatch() {
        hours = minutes = seconds = 0;
    }

    Stopwatch(String time) {
        String[] times = time.split(":");
        hours = Integer.parseInt(times[0]);
        minutes = Integer.parseInt(times[1]);
        seconds = Integer.parseInt(times[2]);
    }

    void tick() {
        seconds += 1;

        if (seconds >= 60) {
            seconds -= 60;
            minutes += 1;
        }

        if (minutes >= 60) {
            minutes -= 60;
            hours += 1;
        }

        if (hours >= 60) {
            hours -= 60;
            // Overflow - Reset to 0
        }
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
