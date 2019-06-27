package pl.wat.nutpromobile.fragments.training;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import pl.wat.nutpromobile.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {


    public TrainingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traingin, container, false);
    }

}
