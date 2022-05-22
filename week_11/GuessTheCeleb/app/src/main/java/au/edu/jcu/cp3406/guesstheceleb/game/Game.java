package au.edu.jcu.cp3406.guesstheceleb.game;

import java.util.Locale;

public class Game {
    private final Question[] questions;
    private int score;
    private int questionNumber;

    public Game(Question[] questions) {
        this.score = 0;
        this.questionNumber = 0;
        this.questions = questions;
    }

    public int count() {
        return questions.length;
    }

    public Question next() {
        Question result = null;

        if (questionNumber < questions.length) {
            result = questions[questionNumber];
            ++questionNumber;
        }

        return result;
    }

    public void updateScore(boolean answeredCorrectly) {
        if (answeredCorrectly)
            ++score;
    }

    public String getScore() {
        return String.format(
                Locale.getDefault(),
                "Score: %d/%d",
                score,
                questions.length
        );
    }

    public boolean isGameOver() {
        return questionNumber >= questions.length;
    }
}
