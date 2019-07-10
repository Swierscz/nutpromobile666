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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.fragments.connection.FragmentConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {
    public static final String TAG = FragmentConnection.class.getSimpleName();

    private OnTrainingFragmentInteractionListener activityInteraction;

    private TrainingFragmentViewModel viewModel;

    @BindView(R.id.test)
    TextView textView;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("test")){
                if(intent !=null )
                    if(textView!=null)
                    textView.setText(intent.getStringExtra("test1"));
            }
        }
    };

    public TrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
        viewModel = ViewModelProviders.of(this).get(TrainingFragmentViewModel.class);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("test"));
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
        if (getContext() instanceof OnTrainingFragmentInteractionListener) {
            activityInteraction = (OnTrainingFragmentInteractionListener) getContext();
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement OnTrainingFragmentInteractionListener");
        }
        Log.i(TAG, TAG + " creation started");

    }

    @OnClick(R.id.startTrainingButton)
    void onStartButtonClick(View view) {
        Log.i(TAG, "Training start button pressed");
        activityInteraction.getTraining().startTraining();
    }

    @OnClick(R.id.stopTrainingButton)
    void onStopButtonClick(View view) {
        Log.i(TAG, "Training stop button pressed");
        activityInteraction.getTraining().stopTraining();
    }

}
