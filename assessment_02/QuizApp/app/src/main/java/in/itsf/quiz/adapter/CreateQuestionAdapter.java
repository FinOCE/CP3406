package in.itsf.quiz.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import in.itsf.quiz.R;
import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.listener.TextUpdateWatcher;
import in.itsf.quiz.model.Answer;
import in.itsf.quiz.model.Question;

public class CreateQuestionAdapter extends RecyclerView.Adapter<CreateQuestionAdapter.QuestionViewHolder> {
    private final List<Question> questions;

    public CreateQuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_editable, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.bindQuestion(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public long getItemId(int position) {
        return questions.get(position).id;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public final EditText questionText;
        public final ImageButton addAnswerBtn;
        public final RecyclerView answersList;

        public CreateAnswerAdapter adapter;

        private final Context context;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();

            questionText = itemView.findViewById(R.id.questionText);
            addAnswerBtn = itemView.findViewById(R.id.addAnswerBtn);
            answersList = itemView.findViewById(R.id.answersList);
        }

        public void bindQuestion(Question question) {
            // Handle interactions with the question_editable layout
            questionText.setText(question.title);
            questionText.addTextChangedListener(new TextUpdateWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    question.title = questionText.getText().toString();
                }
            });

            addAnswerBtn.setOnClickListener((View view) -> {
                new AudioHelper(context, AudioHelper.Sounds.MENU).play();

                Random random = new Random();
                question.answers.add(new Answer(random.nextInt(999999), "", false));
                adapter.notifyItemInserted(question.answers.size());
            });

            // Setup adapter for answers
            adapter = new CreateAnswerAdapter(question.answers);
            adapter.setHasStableIds(true);
            answersList.setAdapter(adapter);

            // Add swipe action to mark answer as correct
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder t) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    new AudioHelper(context, AudioHelper.Sounds.DELETE).play();

                    int position = viewHolder.getLayoutPosition();
                    Answer answer = question.answers.get(position);
                    Log.i("CreateQuestionAdapter", answer.text);

                    for (Answer a : question.answers)
                        a.isCorrect = false;

                    answer.isCorrect = true;
                    adapter.notifyDataSetChanged();
                }
            });
            itemTouchHelper.attachToRecyclerView(answersList);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            answersList.setLayoutManager(layoutManager);
            answersList.setNestedScrollingEnabled(false);
        }
    }
}