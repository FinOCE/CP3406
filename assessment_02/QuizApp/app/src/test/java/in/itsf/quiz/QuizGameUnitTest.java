package in.itsf.quiz;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import in.itsf.quiz.model.Question;
import in.itsf.quiz.model.Quiz;
import in.itsf.quiz.model.QuizGame;

public class QuizGameUnitTest {
    @Test
    public void nextQuestionTest() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "Question 1", new ArrayList<>()));
        questions.add(new Question(1, "Question 2", new ArrayList<>()));
        questions.add(new Question(1, "Question 3", new ArrayList<>()));
        questions.add(new Question(1, "Question 4", new ArrayList<>()));
        Quiz quiz = new Quiz(1, "Example quiz", questions, questions.size());
        QuizGame quizGame = new QuizGame(quiz);

        assertEquals(-1, quizGame.currentQuestion);

        quizGame.nextQuestion(0);
        assertEquals(0, quizGame.currentQuestion);

        quizGame.nextQuestion(0);
        assertEquals(1, quizGame.currentQuestion);
    }

    @Test
    public void getTimerTextTest() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "Question 1", new ArrayList<>()));
        questions.add(new Question(1, "Question 2", new ArrayList<>()));
        questions.add(new Question(1, "Question 3", new ArrayList<>()));
        questions.add(new Question(1, "Question 4", new ArrayList<>()));
        Quiz quiz = new Quiz(1, "Example quiz", questions, questions.size());
        QuizGame quizGame = new QuizGame(quiz);
        quizGame.nextQuestion(0);

        System.out.println(quizGame.getTimerText());
        assertEquals("3.0", quizGame.getTimerText()); // 3.0 because 2 * 1.5 = 3
    }

    @Test
    public void tickTest() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "Question 1", new ArrayList<>()));
        questions.add(new Question(1, "Question 2", new ArrayList<>()));
        questions.add(new Question(1, "Question 3", new ArrayList<>()));
        questions.add(new Question(1, "Question 4", new ArrayList<>()));
        Quiz quiz = new Quiz(1, "Example quiz", questions, questions.size());
        QuizGame quizGame = new QuizGame(quiz);
        quizGame.nextQuestion(0);

        final float timeLeft = quizGame.currentQuestionTimeLeft;
        quizGame.tick();
        assertEquals(timeLeft - QuizGame.TICK_DURATION, quizGame.currentQuestionTimeLeft, 0.001);
    }
}
