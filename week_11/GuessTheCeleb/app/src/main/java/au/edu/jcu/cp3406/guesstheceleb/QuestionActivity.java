package au.edu.jcu.cp3406.guesstheceleb;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.jcu.cp3406.guesstheceleb.game.CelebrityManager;
import au.edu.jcu.cp3406.guesstheceleb.game.Difficulty;
import au.edu.jcu.cp3406.guesstheceleb.game.Game;
import au.edu.jcu.cp3406.guesstheceleb.game.GameBuilder;

public class QuestionActivity extends AppCompatActivity implements StateListener {
    private StatusFragment statusFragment;
    private QuestionFragment questionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        CelebrityManager celebrityManager = new CelebrityManager(getAssets(), "celebs");
        GameBuilder gameBuilder = new GameBuilder(celebrityManager);
        FragmentManager fragmentManager = getSupportFragmentManager();

        statusFragment = (StatusFragment) fragmentManager.findFragmentById(R.id.status);
        questionFragment = (QuestionFragment) fragmentManager.findFragmentById(R.id.question);

        Intent intent = getIntent();
        Difficulty level = Difficulty.valueOf(intent.getStringExtra("level"));

        statusFragment.setMessage("Go!");

        Game game = gameBuilder.create(level);
        questionFragment.setGame(game);
    }

    @Override
    public void onUpdate(State state) {
        switch (state) {
            case START_TIMER:
                statusFragment.startTimer(60);

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
    }
}
