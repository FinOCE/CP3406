package in.itsf.foleyapp;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import in.itsf.foleyapp.FoleyActivity.SoundCategory;

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
        soundPool.load(context, R.raw.animal_birds, 0);
        soundPool.load(context, R.raw.animal_dog, 0);
        soundPool.load(context, R.raw.animal_farm, 0);
        soundPool.load(context, R.raw.animal_pig, 0);

        soundPool.load(context, R.raw.human_applause, 0);
        soundPool.load(context, R.raw.human_breathing, 0);
        soundPool.load(context, R.raw.human_coughing, 0);
        soundPool.load(context, R.raw.human_kids, 0);

        soundPool.load(context, R.raw.nature_crickets, 0);
        soundPool.load(context, R.raw.nature_jungle, 0);
        soundPool.load(context, R.raw.nature_rain, 0);
        soundPool.load(context, R.raw.nature_wind, 0);

        soundPool.load(context, R.raw.technology_hum, 0);
        soundPool.load(context, R.raw.technology_powerup, 0);
        soundPool.load(context, R.raw.technology_scifi_computer, 0);
        soundPool.load(context, R.raw.technology_underwater_transmitter, 0);
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

    // the "Position" type doesn't exist, so I refactored to use an int as the position in the category
    public void play(SoundCategory soundCategory, int position) {
        if (!isReady()) return;

        List<Integer> sampleIds = soundLists.get(soundCategory);
        assert sampleIds != null;

        int sampleId = sampleIds.get(position);

        soundPool.play(sampleId, 1, 1,
                1, 0, 1);
    }

    // resume removed because it wasn't being used

    public void pause() {
        soundPool.autoPause();
    }
}