package pl.wat.nutpromobile.fragments.training;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.activity.OnFragmentInteractionListener;
import pl.wat.nutpromobile.fragments.connection.FragmentConnection;
import pl.wat.nutpromobile.fragments.connection.FragmentConnectionViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {
    public static final String TAG = FragmentConnection.class.getSimpleName();

    private OnFragmentInteractionListener activityInteraction;

    private TrainingFragmentViewModel viewModel;

    public TrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
        viewModel = ViewModelProviders.of(this).get(TrainingFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traingin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void onCreateInit() {
        if (getContext() instanceof OnFragmentInteractionListener) {
            activityInteraction = (OnFragmentInteractionListener) getContext();
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.i(TAG, TAG + " creation started");

    }

    @OnClick(R.id.startTrainingButton)
    void onStartButtonClick(View view) {
        System.out.println("START");
        activityInteraction.startTraining();
    }

    @OnClick(R.id.stopTrainingButton)
    void onStopButtonClick(View view) {
        System.out.println("STOP");
        activityInteraction.stopTraining();
    }

}
