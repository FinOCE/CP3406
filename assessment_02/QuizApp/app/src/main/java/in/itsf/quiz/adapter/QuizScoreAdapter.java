package in.itsf.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import in.itsf.quiz.R;
import in.itsf.quiz.model.Score;

public class QuizScoreAdapter extends RecyclerView.Adapter<QuizScoreAdapter.ScoreViewHolder> {
    private final List<Score> scores;

    public QuizScoreAdapter(List<Score> scores) {
        this.scores = scores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        holder.bindQuiz(scores.get(position), position);
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    @Override
    public long getItemId(int position) {
        return scores.get(position).id;
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        public final TextView scoreCountText;
        public final TextView scoreValueText;

        public ScoreViewHolder(View itemView) {
            super(itemView);

            scoreCountText = itemView.findViewById(R.id.scoreCountText);
            scoreValueText = itemView.findViewById(R.id.scoreValueText);
        }

        public void bindQuiz(Score score, int position) {
            scoreCountText.setText(String.format(Locale.getDefault(), "%d", position + 1));
            scoreValueText.setText(String.format(Locale.getDefault(), "%d", score.value));
        }
    }
}
