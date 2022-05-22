package in.itsf.quiz;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import in.itsf.quiz.model.Answer;
import in.itsf.quiz.model.Question;

public class QuestionUnitTest {
    @Test
    public void getTimeFromStringTest() {
        assertEquals(Question.BASE_TIME_PER_WORD, Question.getTimeFromString("", 1), 0.0);
        assertEquals(3 * Question.BASE_TIME_PER_WORD, Question.getTimeFromString("One two three", 1), 0.0);
        assertEquals(5 * Question.BASE_TIME_PER_WORD, Question.getTimeFromString("3x^2 + 2x + 5", 1), 0.0);
        assertEquals(Question.BASE_TIME_PER_WORD * 1.5, Question.getTimeFromString("Easy", 0), 0.0);
        assertEquals(Question.BASE_TIME_PER_WORD * 0.5, Question.getTimeFromString("Hard", 2), 0.0);
    }

    @Test
    public void addAnswerTest() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(0, "Why not?", true));
        answers.add(new Answer(1, "Yes", false));

        Question question = new Question(0, "Why is a tree?", answers);
        assertEquals(2, question.answers.size());

        question.addAnswer(new Answer(2, "Hamburger", false));
        assertEquals(3, question.answers.size());
    }

    @Test
    public void removeAnswerStringTest() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(0, "Why not?", true));

        Answer answerToRemove = new Answer(1, "Yes", false);
        answers.add(answerToRemove);

        Question question = new Question(0, "Why is a tree?", answers);
        question.removeAnswer(answerToRemove);

        assertEquals(1, question.answers.size());
        assertFalse(question.answers.contains(answerToRemove));
    }

    @Test
    public void removeAnswerIntTest() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(0, "Why not?", true));
        answers.add(new Answer(1, "Yes", false));

        Question question = new Question(0, "Why is a tree?", answers);

        int i = 1;
        Answer removedAnswer = question.answers.get(i);
        question.removeAnswer(i);

        assertEquals(1, question.answers.size());
        assertFalse(question.answers.contains(removedAnswer));
    }
}
