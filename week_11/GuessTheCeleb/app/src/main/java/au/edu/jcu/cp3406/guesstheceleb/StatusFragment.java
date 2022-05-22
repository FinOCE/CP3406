package au.edu.jcu.cp3406.guesstheceleb;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatusFragment extends Fragment {
    private StateListener listener;
    private TextView message;
    private TextView score;
    private Handler handler;
    private int timeRemaining;
    private final Runnable countdown = new Runnable() {
        @Override
        public void run() {
            if (timeRemaining-- > 0) {
                setMessage("Time remaining: " + timeRemaining);
                handler.postDelayed(countdown, 1000);
            } else
                listener.onUpdate(State.GAME_OVER);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        // setup access to its views
        message = view.findViewById(R.id.message);
        score = view.findViewById(R.id.score);

        return view;
    }

    public void startTimer(int duration) {
        timeRemaining = duration;
        handler.postDelayed(countdown, 1000);
    }

    public void stopTimer() {
        timeRemaining = 0;
    }

    public void setMessage(String text) {
        message.setText(text);
    }

    public void setScore(String text) {
        score.setText(text);
    }
}

