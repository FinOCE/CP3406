package in.itsf.quiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz {
    public int id;
    public String title;
    public List<Question> questions;

    public Quiz(int id, String title, List<Question> questions, int length) {
        Collections.shuffle(questions);

        // Remove excess questions if necessary
        while (questions.size() > length) {
            questions.remove(length);
        }

        // Repeat questions if necessary
        List<Question> duplicateQuestions = new ArrayList<>(questions);
        int i = 0;

        while (questions.size() < length) {
            // Shuffle and reuse questions once all are used
            if (i == duplicateQuestions.size()) {
                Collections.shuffle(duplicateQuestions);
                i = 0;
            }

            // Add question to the quiz
            Question question = duplicateQuestions.get(i);
            questions.add(question);
            i++;
        }

        /*
        This resizing never ended up being used in the application itself, but would be a logical
        future update for the app, since all that would need to be implemented is some addition
        things to the UI.
         */

        // Assign variables to the class
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    /**
     * Add a question to the quiz
     */
    public void addQuestion(Question question) {
        questions.add(question);
    }
}
