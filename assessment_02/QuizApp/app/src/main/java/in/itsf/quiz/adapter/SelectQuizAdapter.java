package in.itsf.quiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import in.itsf.quiz.R;
import in.itsf.quiz.activity.CreateActivity;
import in.itsf.quiz.activity.ScoreActivity;
import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.model.Quiz;

public class SelectQuizAdapter extends RecyclerView.Adapter<SelectQuizAdapter.QuizViewHolder> {
    private ClickListener clickListener;
    private final List<Quiz> quizzes;

    public SelectQuizAdapter(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_selectable, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {
        holder.bindQuiz(quizzes.get(position));
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    @Override
    public long getItemId(int position) {
        return quizzes.get(position).id;
    }

    /**
     * Set the listener for item click events
     */
    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView quizNameText;
        public final TextView quizSizeText;
        public final ImageButton quizScoresBtn;
        public final ImageButton quizEditBtn;

        private final Context context;

        public QuizViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            quizNameText = itemView.findViewById(R.id.quizNameText);
            quizSizeText = itemView.findViewById(R.id.quizSizeText);
            quizScoresBtn = itemView.findViewById(R.id.quizScoresBtn);
            quizEditBtn = itemView.findViewById(R.id.quizEditBtn);

            itemView.setOnClickListener(this);
        }

        public void bindQuiz(Quiz quiz) {
            quizNameText.setText(quiz.title);
            quizSizeText.setText(String.format(
                    Locale.getDefault(),
                    "%d %s",
                    quiz.questions.size(),
                    quiz.questions.size() == 1 ? "question" : "questions")
            );
            quizScoresBtn.setOnClickListener((View view) -> {
                new AudioHelper(context, AudioHelper.Sounds.MENU).play();

                Intent intent = new Intent(context, ScoreActivity.class);
                intent.putExtra("quizId", quiz.id);
                context.startActivity(intent);
            });
            quizEditBtn.setOnClickListener((View view) -> {
                new AudioHelper(context, AudioHelper.Sounds.MENU).play();

                Intent intent = new Intent(context, CreateActivity.class);
                intent.putExtra("quizId", quiz.id);
                context.startActivity(intent);
            });
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}
