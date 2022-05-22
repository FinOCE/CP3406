package in.itsf.quiz.model;

import java.util.Random;

public class Answer {
    public int id;
    public String text;
    public boolean isCorrect;

    public Answer(int id, String text, boolean isCorrect) {
        this.id = id;
        this.text = text;
        this.isCorrect = isCorrect;
    }

    /**
     * Get an empty answer
     */
    public static Answer getEmpty() {
        Random random = new Random();
        return new Answer(random.nextInt(Integer.MAX_VALUE), "", false);
    }
}
