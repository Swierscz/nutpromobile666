package pl.wat.nutpromobile.fragments.training;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.main.MainActivity;
import pl.wat.nutpromobile.di.BaseFragment;
import pl.wat.nutpromobile.features.training.Training;
import pl.wat.nutpromobile.features.training.TrainingListener;
import pl.wat.nutpromobile.fragments.connection.FragmentConnection;
import pl.wat.nutpromobile.model.TrainingData;
import pl.wat.nutpromobile.model.TrainingType;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends BaseFragment<TrainingFragmentViewModel> implements TrainingListener {
    public static final String TAG = FragmentConnection.class.getSimpleName();

    @BindView(R.id.timerValueTextView)
    TextView timerValueTextView;

    @BindView(R.id.distanceValueTextView)
    TextView distanceValueTextView;

    @BindView(R.id.speedValueTextView)
    TextView speedValueTextView;

    @BindView(R.id.sensoricDataValueTextView)
    TextView sensoricDataValueTextView;

    @BindView(R.id.trainingTypeSpinner)
    AppCompatSpinner trainingTypeSpinner;

    private OnTrainingFragmentInteractionListener activityInteraction;

    private TrainingFragmentViewModel viewModel;

    private Training training;

    private TrainingType trainingType;

    public TrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof OnTrainingFragmentInteractionListener) {
            activityInteraction = (OnTrainingFragmentInteractionListener) getContext();
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement OnTrainingFragmentInteractionListener");
        }


        viewModel = ViewModelProviders.of(this).get(TrainingFragmentViewModel.class);
        training = activityInteraction.getTraining();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traingin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.training_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainingTypeSpinner.setAdapter(adapter);
        trainingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case "Bieg":
                        trainingType = TrainingType.RUN;
                        break;
                    case "Rower":
                        trainingType = TrainingType.BIKE;
                        break;
                    case "Pływanie":
                        trainingType = TrainingType.SWIM;
                        break;
                    case "Spacer":
                        trainingType = TrainingType.WALK;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.startTrainingButton)
    void onStartButtonClick(View view) {
        Log.i(TAG, "Training start button pressed");
        if (trainingType != null) {
            training.setTrainingListener(this);
            training.startTraining(trainingType);
        } else {
            Toast.makeText(getActivity(), "Wybierz najpierw rodzaj treningu", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.stopTrainingButton)
    void onStopButtonClick(View view) {
        Log.i(TAG, "Training stop button pressed");
        initStopTraining();
    }

    private void initStopTraining() {
        if (training != null) {
            training.removeTrainingListener();
            training.stopTraining();
        }
    }

    @Override
    public void onTrainingDataProcessed(TrainingData trainingData) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sensoricDataValueTextView.setText(trainingData.getSensoricData().getRawData());
            }
        });
    }

    @Override
    public void onTimeChange(String time) {
        timerValueTextView.setText(time);
    }

    @Override
    public void onDistanceChange(String distance) {
        distanceValueTextView.setText(distance);
    }

    @Override
    public void onSpeedChange(String speed) {
        speedValueTextView.setText(speed);
    }

    @Override
    public TrainingFragmentViewModel getViewModel() {
        viewModel  = ViewModelProviders.of(this).get(TrainingFragmentViewModel.class);
        return viewModel;
    }
}
