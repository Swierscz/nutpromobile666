package pl.wat.nutpromobile.fragments.trainingsHistory;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingsHistoryFragment extends Fragment {
    public final static String TAG = TrainingsHistoryFragment.class.getSimpleName();

    @BindView(R.id.trainingsHistoryRecyclerView)
    RecyclerView trainingsHistoryRecyclerView;

    private TrainingHistoryViewModel trainingHistoryViewModel;

    private TrainingHistoryListAdapter trainingHistoryListAdapter;

    public TrainingsHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainings_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainingsHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trainingHistoryViewModel = ViewModelProviders.of(this).get(TrainingHistoryViewModel.class);
        trainingHistoryListAdapter = new TrainingHistoryListAdapter(
                (v, pos) -> {
            Toast.makeText(TrainingsHistoryFragment.this.getContext()
                    , "Click: " + trainingHistoryListAdapter.getElement(pos).getStartTrainingTime().toString(), Toast.LENGTH_SHORT).show();
        });
        trainingHistoryViewModel.getAllTrainings().observe(this, new Observer<List<TrainingSummaryRow>>() {
            @Override
            public void onChanged(@Nullable final List<TrainingSummaryRow> trainigSummaryRowList) {
                // Update the cached copy of the words in the adapter.
                trainingHistoryListAdapter.setTrainingList(trainigSummaryRowList);
                trainingHistoryListAdapter.setTrainingList(trainigSummaryRowList);
            }
        });
    }


}
