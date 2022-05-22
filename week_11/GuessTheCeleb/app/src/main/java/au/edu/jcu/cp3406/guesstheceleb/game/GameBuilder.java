package au.edu.jcu.cp3406.guesstheceleb.game;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBuilder {
    private final CelebrityManager celebrityManager;

    public GameBuilder(CelebrityManager celebrityManager) {
        this.celebrityManager = celebrityManager;
    }

    public Game create(Difficulty level) {
        int maxQuestions = celebrityManager.count();

        switch (level) {
            case EASY:
                return new Game(constructQuestions(3));
            case MEDIUM:
                return new Game(constructQuestions(8));
            case HARD:
                return new Game(constructQuestions(maxQuestions / 2));
            case EXPERT:
                return new Game(constructQuestions(celebrityManager.count()));
        }

        return null;
    }

    private Question[] constructQuestions(int count) {
        Integer[] possibleAnswerIds = generateIndices(count, celebrityManager.count());

        String[] possibleAnswers = new String[count];
        for (int i = 0; i < count; ++i)
            possibleAnswers[i] = celebrityManager.getName(possibleAnswerIds[i]);

        Integer[] answers = generateIndices(count, count);

        // construct the question list
        Question[] questions = new Question[count];
        for (int i = 0; i < count; ++i) {
            int answerId = possibleAnswerIds[answers[i]];
            String answer = celebrityManager.getName(answerId);
            Bitmap image = celebrityManager.getBitmap(answerId);
            Question question = new Question(answer, image, possibleAnswers);
            questions[i] = question;
        }

        return questions;
    }

    private static Integer[] generateIndices(int count, int bound) {
        // randomly select a set of unique indices
        Random random = new Random();

        List<Integer> selections = new ArrayList<>();
        while (selections.size() < count) {
            int selection = random.nextInt(bound);
            if (selections.contains(selection)) continue;

            selections.add(selection);
        }

        // convert the set into primitive array
        Integer[] results = new Integer[count];
        selections.toArray(results);

        return results;
    }
}
