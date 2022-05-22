package au.edu.jcu.cp3406.guesstheceleb;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import au.edu.jcu.cp3406.guesstheceleb.game.Game;
import au.edu.jcu.cp3406.guesstheceleb.game.Question;

public class QuestionFragment extends Fragment {
    private StateListener listener;
    private Game currentGame;
    private Question currentQuestion;
    private ImageView imageView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // setup access to view elements
        imageView = view.findViewById(R.id.image);
        GridView gridView = view.findViewById(R.id.names);

        adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            String guess = currentQuestion.getPossibleAnswers()[position];
            boolean correct = currentQuestion.check(guess);
            currentGame.updateScore(correct);

            if (currentGame.isGameOver())
                listener.onUpdate(State.GAME_OVER);
            else {
                showNextQuestion();
                listener.onUpdate(State.CONTINUE_GAME);
            }
        });

        return view;
    }

    public void setGame(Game game) {
        currentGame = game;
        showNextQuestion();
        listener.onUpdate(State.START_TIMER);
    }

    public void stopGame() {
        adapter.clear();
    }

    public String getScore() {
        return currentGame.getScore();
    }

    private void showNextQuestion() {
        currentQuestion = currentGame.next();
        imageView.setImageBitmap(currentQuestion.getCelebrityImage());

        adapter.clear();
        adapter.addAll(currentQuestion.getPossibleAnswers());
    }
}
