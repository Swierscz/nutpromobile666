package pl.wat.nutpromobile.fragments.connection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.wat.nutpromobile.model.BluetoothDevice;

public class FragmentConnectionViewModel extends ViewModel {
    private FragmentConnectionRepository repository = new FragmentConnectionRepository();
    private MutableLiveData<List<BluetoothDevice>> btDevices;
    public MutableLiveData<List<BluetoothDevice>> getBtDevices(){
        if(btDevices == null){
            btDevices = new MutableLiveData<>();
        }
        return btDevices;
    }

    public void updateDeviceList(List<BluetoothDevice> bluetoothDevices){
        btDevices.setValue(bluetoothDevices);
    }

}
