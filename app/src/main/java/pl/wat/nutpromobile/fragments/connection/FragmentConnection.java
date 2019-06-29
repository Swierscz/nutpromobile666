package pl.wat.nutpromobile.fragments.connection;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.ble.Connection;
import pl.wat.nutpromobile.ble.DevicesAdapter;
import pl.wat.nutpromobile.model.BluetoothDevice;

//W przypadku rozbudowy klasy dostosować do modelu MVP
public class FragmentConnection extends Fragment {
    public static final String TAG = FragmentConnection.class.getSimpleName();
    @BindView(R.id.fragment_connection_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_connection_recycler_view)
    RecyclerView devicesRecyclerView;
    private OnFragmentInteractionListener activityInteraction;

    private FragmentConnectionViewModel viewModel;

    public FragmentConnection() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        Connection getConnection();
    }


    public static FragmentConnection newInstance() {
        FragmentConnection fragment = new FragmentConnection();
        Log.i(TAG, "Fragment connection new instance created");
        return fragment;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
        viewModel = ViewModelProviders.of(this).get(FragmentConnectionViewModel.class);
        defineLiveDataObservers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, TAG + " view created");
        devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.updateDeviceList(new ArrayList<>());
        defineOnRefreshBehaviour();

    }

    private void setTestBluetoothDeviceData() {
        List<BluetoothDevice> testData = new ArrayList<>();
        testData.add(new BluetoothDevice("bbbbb", "111:222:333"));
        testData.add(new BluetoothDevice("bbbbb2", "2111:222:333"));
        testData.add(new BluetoothDevice("bbbbb3", "3111:222:333"));
        viewModel.updateDeviceList(testData);
    }

    private void defineOnRefreshBehaviour() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchBluetoothDevices();
        });
    }

    private void defineLiveDataObservers() {
        final Observer<List<BluetoothDevice>> deviceListObserver;
        deviceListObserver = devices -> {
            specifyDevicesListBehaviourAndRefreshData(devices);
        };


        viewModel.getBtDevices().observe(this, deviceListObserver);
    }

    private void fetchBluetoothDevices() {
        Single<List<BluetoothDevice>> deviceSingle = Single.fromCallable(() ->
                activityInteraction.getConnection().getBleScanner().scanForDevices());

        deviceSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BluetoothDevice>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<BluetoothDevice> bluetoothDevices) {
                        viewModel.updateDeviceList(bluetoothDevices);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


    private void specifyDevicesListBehaviourAndRefreshData(final List<BluetoothDevice> devices) {
        DevicesAdapter listAdapter = new DevicesAdapter(devices, (v, pos) ->
        {
            Toast.makeText(getContext(), "Device name: " + devices.get(pos).getName(), Toast.LENGTH_SHORT).show();
            activityInteraction.getConnection().connectToDevice(devices.get(pos).getAddress());
        });

        devicesRecyclerView.setAdapter(listAdapter);
        devicesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.i(TAG, TAG + " devices list data changed");
    }
}

