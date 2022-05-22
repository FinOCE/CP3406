package in.itsf.quiz;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.itsf.quiz.model.Question;
import in.itsf.quiz.model.Quiz;

public class QuizUnitTest {
    @Test
    public void constructorTooManyQuestionsTest() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(0, "Question 1", new ArrayList<>()));
        questions.add(new Question(1, "Question 2", new ArrayList<>()));
        questions.add(new Question(2, "Question 3", new ArrayList<>()));
        questions.add(new Question(3, "Question 4", new ArrayList<>()));
        assertEquals(4, questions.size());

        Quiz quiz = new Quiz(0, "Example quiz", questions, 2);
        assertEquals(2, quiz.questions.size());
    }

    @Test
    public void constructorTooFewQuestionsTest() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(0, "Question 1", new ArrayList<>()));
        questions.add(new Question(1, "Question 2", new ArrayList<>()));
        questions.add(new Question(2, "Question 3", new ArrayList<>()));
        questions.add(new Question(3, "Question 4", new ArrayList<>()));
        assertEquals(4, questions.size());

        Quiz quiz = new Quiz(0, "Example quiz", questions, 12);
        assertEquals(12, quiz.questions.size());

        Map<String, Integer> questionFrequency = new HashMap<>();
        for (Question question : quiz.questions) {
            boolean exists = questionFrequency.containsKey(question.title); // no inspection is set below because this boolean guarantees it
            //noinspection ConstantConditions
            int currentFrequency = exists ? questionFrequency.get(question.title) : 0;
            questionFrequency.put(question.title, ++currentFrequency);
        }

        for (int frequency : questionFrequency.values()) {
            assertEquals(3, frequency); // Each question should be present 3 times because 12 / 4 = 3
        }
    }

    @Test
    public void addQuestionTest() {
        List<Question> questions = new ArrayList<>();
        Quiz quiz = new Quiz(0, "Example quiz", questions, questions.size());
        assertEquals(0, quiz.questions.size());

        Question question = new Question(0, "Why is a tree?", new ArrayList<>());
        quiz.addQuestion(question);
        assertEquals(1, quiz.questions.size());

        quiz.addQuestion(question);
        assertEquals(2, quiz.questions.size());
    }
}
