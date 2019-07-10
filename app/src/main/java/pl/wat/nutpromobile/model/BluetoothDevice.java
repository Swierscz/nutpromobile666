package pl.wat.nutpromobile.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BluetoothDevice {
    private String name;
    private String address;

}
