package in.itsf.guessinggame;

import java.util.Random;

public class Game {
    public int answer;

    public Game() {
        Random random = new Random();
        answer = random.nextInt(11) + 1;
    }

    public boolean check(int value) {
        return value == answer;
    }
}
