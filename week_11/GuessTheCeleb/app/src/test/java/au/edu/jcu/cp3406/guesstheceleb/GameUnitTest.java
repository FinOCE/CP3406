package au.edu.jcu.cp3406.guesstheceleb;

import org.junit.Test;

import au.edu.jcu.cp3406.guesstheceleb.game.Game;
import au.edu.jcu.cp3406.guesstheceleb.game.Question;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameUnitTest {
    @Test
    public void testQuestion() {
        Question question = new Question("bob", null, new String[]{"bob", "jane"});

        assertTrue(question.check("bob"));
        assertFalse(question.check("jane"));
    }

    @Test
    public void testGame() {
        Question[] questions = new Question[3];
        String[] answers = new String[]{"bob", "jane", "harry"};

        for (int i = 0; i < 3; ++i)
            questions[i] = new Question(answers[i], null, answers);

        Game game = new Game(questions);

        while (!game.isGameOver()) {
            Question question = game.next();
            game.updateScore(question.check("bob"));
        }

        assertEquals("Score: 1/3", game.getScore());
    }
}
