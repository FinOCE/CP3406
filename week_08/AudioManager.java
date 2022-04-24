
package au.edu.jcu.foleyapp;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This code demonstrates one way to load several sounds
// into a sound pool. Each sound has a unique sampleId.
// SampleId's are not the same as the raw resource ids

public class AudioManager implements SoundPool.OnLoadCompleteListener {
    private final Map<SoundCategory, List<Integer>> soundLists;
    private SoundCategory currentSoundCategory;

    private final SoundPool soundPool;
    private int loadCount;

    public AudioManager(Context context) {
        soundLists = new HashMap<>();
        for (SoundCategory category : SoundCategory.values()) {
            soundLists.put(category, new ArrayList<>());
        }
        currentSoundCategory = SoundCategory.animal;

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10);
        soundPool = builder.build();
        soundPool.setOnLoadCompleteListener(this);

        // load order matches Sound's declaration order
        // TODO: replace in here the names of your 16 raw sound samples
        soundPool.load(context, R.raw.animal_birds_singing, 0);
        soundPool.load(context, R.raw.animal_cat_meow, 0);
        soundPool.load(context, R.raw.animal_dog_breathing, 0);
        soundPool.load(context, R.raw.animal_happy_dog_barks, 0);

        soundPool.load(context, R.raw.human_boy_saying_hiya, 0);
        soundPool.load(context, R.raw.human_children_laughing, 0);
        soundPool.load(context, R.raw.human_children_talking, 0);
        soundPool.load(context, R.raw.human_man_yawning, 0);

        soundPool.load(context, R.raw.nature_forest_rain, 0);
        soundPool.load(context, R.raw.nature_forest_with_woodpeckers, 0);
        soundPool.load(context, R.raw.nature_summer_night_in_the_forest, 0);
        soundPool.load(context, R.raw.nature_tropical_bird_squeak, 0);

        soundPool.load(context, R.raw.technology_fast_clock_ticking, 0);
        soundPool.load(context, R.raw.technology_fast_keyboard_typing, 0);
        soundPool.load(context, R.raw.technology_long_clock_gong, 0);
        soundPool.load(context, R.raw.technology_wall_clock, 0);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool,
                               int sampleId, int status) {

        // store the new sampleId in the current sound's sampleId list
        List<Integer> sampleIds = soundLists.get(currentSoundCategory);
        assert sampleIds != null;
        sampleIds.add(sampleId);
        ++loadCount;
        Log.i("AudioManager", "loadCount: " + loadCount + " current sound loaded: " + currentSoundCategory.toString());

        if (loadCount % 4 == 0) { // every 4 loads change to the next sound
            int index = currentSoundCategory.ordinal();
            if (index < SoundCategory.values().length - 1) {
                currentSoundCategory = SoundCategory.values()[index + 1];
            }
        }

    }

    public boolean isReady() {
        return loadCount == 16;
    }

    public void play(SoundCategory soundCategory, Position position) {
        if (!isReady()) return;

        List<Integer> sampleIds = soundLists.get(soundCategory);
        assert sampleIds != null;

        int index = position.ordinal();
        int sampleId = sampleIds.get(index);

        soundPool.play(sampleId, 1, 1,
                1, 0, 1);
    }

    public void resume() {
        soundPool.autoResume();
    }

    public void pause() {
        soundPool.autoPause();
    }
}