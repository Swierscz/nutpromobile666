package pl.wat.nutpromobile.activity;

import pl.wat.nutpromobile.ble.Connection;

public interface OnFragmentInteractionListener {
    Connection getConnection();
    void startTraining();
    void stopTraining();
}