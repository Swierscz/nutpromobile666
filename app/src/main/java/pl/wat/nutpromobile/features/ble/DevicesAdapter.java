package pl.wat.nutpromobile.features.ble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.wat.nutpromobile.R;
import pl.wat.nutpromobile.model.BluetoothDevice;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {

    private final int VIEW_EMPTY = 0;
    private final int VIEW_NOT_EMPTY = 1;
    private final List<BluetoothDevice> devices;
    private final ClickListener clickListener;

    public DevicesAdapter(List<BluetoothDevice> devices, ClickListener clickListener) {
        this.devices = devices;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_NOT_EMPTY) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.device_list, parent, false));
        } else if(viewType == VIEW_EMPTY) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.device_list_empty, parent, false
            ));
        } else return null;
    }

    @Override
    public int getItemViewType(int position) {
        return devices.isEmpty() ? VIEW_EMPTY : VIEW_NOT_EMPTY;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!devices.isEmpty()) {
            if(devices.get(position).getName()==null){
                holder.deviceName.setText("Name not found");
            }else{
                holder.deviceName.setText(devices.get(position).getName());
            }
            holder.deviceAddress.setText(devices.get(position).getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return devices.isEmpty() ? 1 : devices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceName;
        private TextView deviceAddress;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.device_list_device_name);
            deviceAddress = (TextView) itemView.findViewById(R.id.device_list_device_address);
            itemView.setOnClickListener(v -> clickListener.onClick(itemView, getAdapterPosition()));
        }
    }

    public interface ClickListener{
        void onClick(View v, Integer pos);
    }
}

