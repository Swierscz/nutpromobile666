package pl.wat.nutpromobile.fragments.trainingSummary;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.db.row.TrainingSummaryRow;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingSummaryFragment extends Fragment {

    @BindView(R.id.trainingStartTime)
    TextView trainingStartTimeTextView;

    @BindView(R.id.trainingStopTime)
    TextView trainingStopTimeTextView;

    @BindView(R.id.trainingAverageSpeed)
    TextView trainingAverageSpeed;

    @BindView(R.id.trainingDistance)
    TextView trainingDistance;

    @BindView(R.id.trainingType)
    TextView trainingType;

    private TrainingSummaryViewModel trainingSummaryViewModel;

    public TrainingSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_training_summary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainingSummaryViewModel = ViewModelProviders.of(this).get(TrainingSummaryViewModel.class);
        int trainingSummaryId = getArguments().getInt(TrainingSummaryRow.class.getName());
        trainingSummaryViewModel.getTrainingSummary(trainingSummaryId).observe(this, new Observer<TrainingSummaryRow>() {
            @Override
            public void onChanged(TrainingSummaryRow trainingSummaryRow) {
                if (trainingSummaryRow != null)
                    setTrainingDataToView(trainingSummaryRow);
            }
        });
    }

    private void setTrainingDataToView(TrainingSummaryRow trainingSummaryRow) {
        trainingStartTimeTextView.setText(trainingSummaryRow.getStartTrainingTime());
        trainingStopTimeTextView.setText(trainingSummaryRow.getStopTrainingTime());
        trainingAverageSpeed.setText(String.valueOf(trainingSummaryRow.getAverageSpeed()));
        trainingType.setText(trainingSummaryRow.getTrainingType());
        trainingDistance.setText(String.valueOf(trainingSummaryRow.getDistance()));
    }
}
