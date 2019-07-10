package pl.wat.nutpromobile.fragments.more;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.fragments.connection.FragmentConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {
    public final static String TAG = MoreFragment.class.getSimpleName();
    @BindView(R.id.moreRecyclerView)
    RecyclerView moreRecyclerView;

    private MoreElementsAdapter moreElementsAdapter;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moreElementsAdapter = new MoreElementsAdapter(
                new ArrayList<>(Arrays.asList(MoreElements.elements))
                , (v, pos) -> {
                    Toast.makeText(MoreFragment.this.getContext()
                            , "Click: " + moreElementsAdapter.getElement(pos), Toast.LENGTH_SHORT).show();
                    //TODO zmienić Options na coś statycznego
                    if(moreElementsAdapter.getElement(pos).equals("Options")){
                        Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment).navigate(R.id.settings);
                    }else if(moreElementsAdapter.getElement(pos).equals("Connections")){
                        Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment).navigate(R.id.bluetoothConnection);
                    }

        });
        moreRecyclerView.setAdapter(moreElementsAdapter);
    }
}
