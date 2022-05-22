package in.itsf.quiz.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import in.itsf.quiz.R;

public class AudioHelper {
    private final SharedPreferences sharedPreferences;
    private final MediaPlayer mediaPlayer;

    public AudioHelper(Context context, int audioResourceId) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mediaPlayer = MediaPlayer.create(context, audioResourceId);
    }

    /**
     * Play the sound
     */
    public void play() {
        if (sharedPreferences.getBoolean("sounds", true)) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();

            mediaPlayer.start();
        }
    }

    /**
     * Store resource IDs for audio used
     */
    public static class Sounds {
        // Resources from https://material.io/design/sound/sound-resources.html

        public static final int MENU = R.raw.menu;
        public static final int DELETE = R.raw.delete;
        public static final int PUBLISH = R.raw.publish;
        public static final int INCORRECT = R.raw.incorrect;
        public static final int CORRECT = R.raw.correct;
    }
}
