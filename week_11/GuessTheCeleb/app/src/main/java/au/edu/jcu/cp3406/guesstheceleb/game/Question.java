package au.edu.jcu.cp3406.guesstheceleb.game;

import android.graphics.Bitmap;

public class Question {
    private final String celebrityName;
    private final Bitmap celebrityImage;
    private final String[] possibleAnswers;

    public Question(String celebrityName, Bitmap celebrityImage, String[] possibleAnswers) {
        this.celebrityName = celebrityName;
        this.celebrityImage = celebrityImage;
        this.possibleAnswers = possibleAnswers;
    }

    public boolean check(String guess) {
        return guess.equals(celebrityName);
    }

    public Bitmap getCelebrityImage() {
        return celebrityImage;
    }

    public String[] getPossibleAnswers() {
        return possibleAnswers;
    }
}

