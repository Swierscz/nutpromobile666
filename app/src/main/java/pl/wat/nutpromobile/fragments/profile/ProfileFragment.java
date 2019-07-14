package pl.wat.nutpromobile.fragments.profile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.db.row.UserRow;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profileFragmentFirstNameEditText)
    TextInputEditText profileFragmentFirstNameEditText;

    @BindView(R.id.profileFragmentLastNameEditText)
    TextInputEditText profileFragmentLastNameEditText;

    @BindView(R.id.profileFragmentAgeEditText)
    TextInputEditText profileFragmentAgeEditText;

    @BindView(R.id.profileFragmentHeightEditText)
    TextInputEditText profileFragmentHeightEditText;

    private ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getUser().observe(this, new Observer<UserRow>() {
            @Override
            public void onChanged(UserRow userRow) {
                if (userRow != null) {
                    setUserDataToView(userRow);
                }
            }
        });
    }

    private void setUserDataToView(UserRow userRow) {
        profileFragmentFirstNameEditText.append(userRow.getFirstName());
        profileFragmentLastNameEditText.append(userRow.getLastName());
        profileFragmentAgeEditText.append(String.valueOf(userRow.getAge()));
        profileFragmentHeightEditText.append(String.valueOf(userRow.getFirstName()));
    }
}
