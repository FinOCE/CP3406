package in.itsf.quiz.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import in.itsf.quiz.model.Answer;
import in.itsf.quiz.model.Question;
import in.itsf.quiz.model.Quiz;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Many of these methods are not used in the app but are kept in for the sake of if it were to be updated in the future, it may need them

    private static final String DB_NAME = "quiz";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            // Create quiz table
            db.execSQL("CREATE TABLE quiz (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL);");

            // Create question table
            db.execSQL("CREATE TABLE question (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "quizId INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "FOREIGN KEY (quizId) REFERENCES quiz(_id));");

            // Create answer table
            db.execSQL("CREATE TABLE answer (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "questionId INTEGER NOT NULL," +
                    "text TEXT NOT NULL," +
                    "isCorrect BOOLEAN NOT NULL," +
                    "FOREIGN KEY (questionId) REFERENCES question(_id));"
            );

            // Create score table
            db.execSQL("CREATE TABLE score (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "quizId INTEGER NOT NULL," +
                    "value INTEGER NOT NULL," +
                    "FOREIGN KEY (quizId) REFERENCES quiz(_id));"
            );
        }
    }

    /**
     * Convert a List<String> into a set of options for a SQLite query
     */
    public static String getListOptions(List<String> options) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        sb.append('(');

        for (String option : options) {
            if (!isFirst)
                sb.append(',');
            isFirst = false;

            sb.append(option);
        }

        sb.append(')');

        return sb.toString();
    }

    /**
     * Create a new quiz
     */
    public static int insertQuiz(SQLiteDatabase db, String title) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("title", title);
        long id = db.insert("quiz", null, answerValues);
        Log.i("CreateActivity", Long.toString(id));
        return (int) id;
    }

    /**
     * Update an existing quiz
     */
    public static void updateQuiz(SQLiteDatabase db, int id, String title) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("title", title);
        db.update("quiz", answerValues, "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Delete a quiz and all items that reference it
     */
    public static void deleteQuiz(SQLiteDatabase db, int id) throws SQLiteException {
        Log.i("DatabaseHelper", String.format("Deleting quiz %d", id));

        // Fetch all relevant question IDs
        Cursor cursor = db.query(
                "question",
                new String[]{"_id"},
                "quizId = ?",
                new String[]{Integer.toString(id)},
                null, null, null);

        // Create array of question IDs
        List<String> questionIds = new ArrayList<>();

        if (cursor.moveToFirst())
            while (cursor.moveToNext())
                questionIds.add(Integer.toString(cursor.getInt(0)));

        cursor.close();

        // Submit delete queries
        db.delete("answer", "questionId IN " + getListOptions(questionIds), null);
        db.delete("question", "quizId = ?", new String[]{Integer.toString(id)});
        db.delete("quiz", "_id = ?", new String[]{Integer.toString(id)});
        db.delete("score", "quizId = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Create a new quiz question
     */
    public static int insertQuestion(SQLiteDatabase db, int quizId, String title) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("quizId", quizId);
        answerValues.put("title", title);
        return (int) db.insert("question", null, answerValues);
    }

    /**
     * Update an existing quiz question
     */
    public static void updateQuestion(SQLiteDatabase db, int id, String title) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("title", title);
        db.update("question", answerValues, "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Delete a quiz question and all answers that reference it
     */
    public static void deleteQuestion(SQLiteDatabase db, int id) throws SQLiteException {
        db.delete("answer", "questionId = ?", new String[]{Integer.toString(id)});
        db.delete("question", "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Create a new quiz question answer
     */
    public static int insertAnswer(SQLiteDatabase db, int questionId, String text, boolean isCorrect) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("questionId", questionId);
        answerValues.put("text", text);
        answerValues.put("isCorrect", isCorrect);
        return (int) db.insert("answer", null, answerValues);
    }

    /**
     * Update an existing quiz question answer
     */
    public static void updateAnswer(SQLiteDatabase db, int id, String text, boolean isCorrect) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("text", text);
        answerValues.put("isCorrect", isCorrect ? 1 : 0);
        db.update("answer", answerValues, "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Delete a quiz question answer
     */
    public static void deleteAnswer(SQLiteDatabase db, int id) throws SQLiteException {
        db.delete("answer", "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Create a new quiz score
     */
    public static int insertScore(SQLiteDatabase db, int quizId, int value) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("quizId", quizId);
        answerValues.put("value", value);
        return (int) db.insert("score", null, answerValues);
    }

    /**
     * Update an existing quiz question answer
     */
    public static void updateScore(SQLiteDatabase db, int id, int value) throws SQLiteException {
        ContentValues answerValues = new ContentValues();
        answerValues.put("value", value);
        db.update("score", answerValues, "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Delete a quiz score
     */
    public static void deleteScore(SQLiteDatabase db, int id) throws SQLiteException {
        db.delete("score", "_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * Fetch an entire quiz from the database (quizId = -1 for random, length = -1 for all questions once)
     */
    public static Quiz fetchQuiz(SQLiteDatabase db, int quizId, int length) throws Exception {
        Log.i("DatabaseHelper", String.format("Fetching quiz %d", quizId));
        Cursor cursor;

        // Get a random quiz when required
        if (quizId == -1) {
            cursor = db.query(
                    "quiz",
                    new String[]{"_id"},
                    null, null, null, null, null
            );

            if (!cursor.moveToFirst())
                throw new Exception("No quizzes exist yet, why not try create one?");

            List<Integer> quizIds = new ArrayList<>();

            do
                quizIds.add(cursor.getInt(0));
            while (cursor.moveToNext());

            Random random = new Random();
            quizId = quizIds.get(random.nextInt(quizIds.size()));

            cursor.close();
        }

        // Get the quiz title
        cursor = db.query(
                "quiz",
                new String[]{"_id", "title"},
                "_id = ?",
                new String[]{Integer.toString(quizId)},
                null, null, null);

        if (!cursor.moveToFirst())
            throw new Exception("Quiz could not be found");

        String quizTitle = cursor.getString(1);

        cursor.close();

        // Get the questions
        cursor = db.query(
                "question",
                new String[]{"_id", "title"},
                "quizId = ?",
                new String[]{Integer.toString(quizId)},
                null, null, null
        );

        if (!cursor.moveToFirst())
            throw new Exception("No questions could be found");

        List<Question> questions = new ArrayList<>();

        do {
            Question question = new Question(cursor.getInt(0), cursor.getString(1), new ArrayList<>());
            questions.add(question);
        } while (cursor.moveToNext());

        cursor.close();

        // Get the answers
        List<String> questionIds = new ArrayList<>();

        for (Question question : questions)
            questionIds.add(Integer.toString(question.id));

        cursor = db.query(
                "answer",
                new String[]{"_id", "questionId", "text", "isCorrect"},
                "questionId IN " + DatabaseHelper.getListOptions(questionIds), // SQLite doesn't seem to like a list as an arg
                null, null, null, null
        );

        if (!cursor.moveToFirst())
            throw new Exception("No answers could be found");

        do
            for (Question question : questions)
                if (question.id == cursor.getInt(1)) {
                    Answer answer = new Answer(cursor.getInt(0), cursor.getString(2), cursor.getInt(3) == 1);
                    question.addAnswer(answer);
                    break;
                }
        while (cursor.moveToNext());

        cursor.close();

        // Create the quiz
        return new Quiz(quizId, quizTitle, questions, length == -1 ? questions.size() : length);
    }
}
