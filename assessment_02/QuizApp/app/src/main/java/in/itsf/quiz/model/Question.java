package in.itsf.quiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question {
    public static final float BASE_TIME_PER_WORD = 1f;

    public int id;
    public String title;
    public List<Answer> answers;

    public Question(int id, String title, List<Answer> answers) {
        Collections.shuffle(answers);

        this.id = id;
        this.title = title;
        this.answers = answers;
    }

    /**
     * Get an empty question
     */
    public static Question getEmpty() {
        Random random = new Random();
        List<Answer> answers = new ArrayList<>();
        answers.add(Answer.getEmpty());
        return new Question(random.nextInt(Integer.MAX_VALUE), "", answers);
    }

    /**
     * Get the amount of time allotted for a given sentence
     */
    public static float getTimeFromString(String str, int difficulty) {
        float difficultyMultiplier = (3f - (float) difficulty) * 0.5f;
        return str.split(" ").length * BASE_TIME_PER_WORD * difficultyMultiplier;
    }

    /**
     * Add an answer to the question
     */
    public void addAnswer(Answer answer) {
        answers.add(answer);
        Collections.shuffle(answers);
    }

    /**
     * Remove an answer from the question by the Answer class
     */
    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    /**
     * Remove an answer from the question by index in the ArrayList
     */
    public void removeAnswer(int i) {
        answers.remove(i);
    }
}
