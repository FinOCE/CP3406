package in.itsf.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.itsf.quiz.R;
import in.itsf.quiz.model.Answer;

public class QuizAnswerAdapter extends RecyclerView.Adapter<QuizAnswerAdapter.QuizAnswerViewHolder> {
    private final List<Answer> answers;

    public QuizAnswerAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    @NonNull
    @Override
    public QuizAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_answer, parent, false);
        return new QuizAnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizAnswerViewHolder holder, int position) {
        holder.bindAnswer(answers.get(position));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    @Override
    public long getItemId(int position) {
        return answers.get(position).id;
    }

    public static class QuizAnswerViewHolder extends RecyclerView.ViewHolder {
        public final Button quizAnswerText;

        public QuizAnswerViewHolder(View itemView) {
            super(itemView);

            quizAnswerText = (Button) itemView;
        }

        public void bindAnswer(Answer answer) {
            quizAnswerText.setText(answer.text);
        }
    }
}