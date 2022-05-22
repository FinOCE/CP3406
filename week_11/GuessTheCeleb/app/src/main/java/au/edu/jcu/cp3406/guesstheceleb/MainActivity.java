package au.edu.jcu.cp3406.guesstheceleb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import au.edu.jcu.cp3406.guesstheceleb.game.CelebrityManager;
import au.edu.jcu.cp3406.guesstheceleb.game.Difficulty;
import au.edu.jcu.cp3406.guesstheceleb.game.Game;
import au.edu.jcu.cp3406.guesstheceleb.game.GameBuilder;

public class MainActivity extends AppCompatActivity implements StateListener {
    private GameFragment gameFragment;
    private StatusFragment statusFragment;
    private QuestionFragment questionFragment;
    private boolean isLargeScreen;
    private GameBuilder gameBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CelebrityManager celebrityManager = new CelebrityManager(getAssets(), "celebs");
        gameBuilder = new GameBuilder(celebrityManager);
        FragmentManager fragmentManager = getSupportFragmentManager();

        gameFragment = (GameFragment) fragmentManager.findFragmentById(R.id.game);
        statusFragment = (StatusFragment) fragmentManager.findFragmentById(R.id.status);
        questionFragment = (QuestionFragment) fragmentManager.findFragmentById(R.id.question);

        isLargeScreen = statusFragment != null;
    }

    @Override
    public void onUpdate(State state) {
        Difficulty level = gameFragment.getLevel();
        String text = String.format(Locale.getDefault(), "state: %s level: %s", state, level);
        Log.i("MainActivity", text);

        if (isLargeScreen)
            switch (state) {
                case START_GAME:
                    statusFragment.setMessage("Go!");
                    Game game = gameBuilder.create(level);
                    questionFragment.setGame(game);

                    break;
                case START_TIMER:
                    statusFragment.startTimer(computeDuration(level));

                    break;
                case CONTINUE_GAME:
                    statusFragment.setScore(questionFragment.getScore());

                    break;
                case GAME_OVER:
                    statusFragment.stopTimer();
                    questionFragment.stopGame();
                    statusFragment.setScore(questionFragment.getScore());
                    statusFragment.setMessage("Game over!");

                    break;
            }
        else {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("level", level.toString());
            startActivity(intent);
        }
    }

    private int computeDuration(Difficulty level) {
        switch (level) {
            case EASY:
                return 10;
            case MEDIUM:
                return 20;
            case HARD:
                return 30;
            default:
                return 60;
        }
    }
}

