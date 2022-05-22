package in.itsf.quiz.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.itsf.quiz.R;
import in.itsf.quiz.helper.AudioHelper;
import in.itsf.quiz.listener.TextUpdateWatcher;
import in.itsf.quiz.model.Answer;

public class CreateAnswerAdapter extends RecyclerView.Adapter<CreateAnswerAdapter.AnswerViewHolder> {
    private final List<Answer> answers;

    public CreateAnswerAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_editable, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
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

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        public final ImageButton answerCorrectBtn;
        public final EditText answerText;
        public final ImageButton answerRemoveBtn;

        private final Context context;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();

            answerCorrectBtn = itemView.findViewById(R.id.answerCorrectBtn);
            answerText = itemView.findViewById(R.id.answerText);
            answerRemoveBtn = itemView.findViewById(R.id.answerRemoveBtn);
        }

        public void bindAnswer(Answer answer) {
            // Handle answerCorrectBtn ImageButton
            answerCorrectBtn.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(answer.isCorrect
                    ? R.color.primary
                    : R.color.alternate
            )));

            answerCorrectBtn.setOnClickListener((View view) -> {
                new AudioHelper(context, AudioHelper.Sounds.MENU).play();

                answer.isCorrect = !answer.isCorrect;
                notifyDataSetChanged();
            });

            // Handle answerText EditText
            answerText.setText(answer.text);
            answerText.addTextChangedListener(new TextUpdateWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    answer.text = answerText.getText().toString();
                }
            });

            // Handle answerRemoveBtn ImageButton
            answerRemoveBtn.setOnClickListener((View view) -> {
                if (answers.size() == 1) {
                    Toast.makeText(context, "Questions must have at least one answer", Toast.LENGTH_SHORT).show();
                } else {
                    new AudioHelper(context, AudioHelper.Sounds.DELETE).play();

                    answers.remove(answer);
                    notifyDataSetChanged();
                }
            });
        }
    }
}