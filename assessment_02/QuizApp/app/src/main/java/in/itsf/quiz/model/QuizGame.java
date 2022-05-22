package in.itsf.quiz.model;

import java.util.Locale;

public class QuizGame {
    public static final float TICK_DURATION = 0.1f;
    public Quiz quiz;
    public boolean[] correctlyAnswered;
    public float score = 0;
    public int currentQuestion = -1;
    public float currentQuestionTime;
    public float currentQuestionTimeLeft;

    public QuizGame(Quiz quiz) {
        this.quiz = quiz;
        correctlyAnswered = new boolean[quiz.questions.size()];
    }

    /**
     * Progress to the next question
     */
    public Question nextQuestion(int difficulty) {
        Question question = quiz.questions.get(++currentQuestion);

        currentQuestionTime = Question.getTimeFromString(question.title, difficulty);
        for (Answer answer : question.answers)
            currentQuestionTime += Question.getTimeFromString(answer.text, difficulty);

        currentQuestionTimeLeft = currentQuestionTime;

        return question;
    }

    public String getTimerText() {
        return String.format(Locale.getDefault(), "%.1f", currentQuestionTimeLeft);
    }

    public void tick() {
        currentQuestionTimeLeft -= TICK_DURATION;
    }
}
