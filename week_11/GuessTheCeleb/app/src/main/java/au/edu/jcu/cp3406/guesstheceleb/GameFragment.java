package au.edu.jcu.cp3406.guesstheceleb;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import au.edu.jcu.cp3406.guesstheceleb.game.Difficulty;

public class GameFragment extends Fragment {
    private StateListener listener;
    private Difficulty level;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }

    public Difficulty getLevel() {
        return level;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        final Spinner spinner = view.findViewById(R.id.difficulty);
        final TextView currentLevel = view.findViewById(R.id.current_level);

        // handle button click by triggering state listener update and record current difficulty level
        view.findViewById(R.id.play).setOnClickListener(v -> {
            String selection = spinner.getSelectedItem().toString();

            Log.i("GameFragment", "selection: " + selection);

            level = Difficulty.valueOf(selection.toUpperCase());
            currentLevel.setText(String.format(Locale.getDefault(), "Level: %s", level));

            listener.onUpdate(State.START_GAME);
        });

        return view;
    }
}
