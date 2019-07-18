package pl.wat.nutpromobile.fragments.trainingsHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;

public class TrainingHistoryListAdapter extends RecyclerView.Adapter<TrainingHistoryListAdapter.MyViewHolder> {

    private List<TrainingSummaryRow> trainingSummaryList;
    private final ClickListener clickListener;

    public TrainingHistoryListAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView trainingName;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            trainingName = itemView.findViewById(R.id.training_title);
            itemView.setOnClickListener(v -> clickListener.onClick(itemView, getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrainingHistoryListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trainings_history_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (trainingSummaryList != null) {
            TrainingSummaryRow buf = trainingSummaryList.get(position);
            holder.trainingName.setText(buf.getStartTrainingTime().toString());
        } else {
            holder.trainingName.setText("No training");
        }
    }

    @Override
    public int getItemCount() {
        return trainingSummaryList==null ? 0 : trainingSummaryList.size();
    }

    public void setTrainingList(List<TrainingSummaryRow> trainingList) {
        this.trainingSummaryList = trainingList;
        notifyDataSetChanged();
    }

    public TrainingSummaryRow getElement(Integer pos) {
        return trainingSummaryList.get(pos);
    }

    public interface ClickListener {
        void onClick(View v, Integer pos);
    }
}
