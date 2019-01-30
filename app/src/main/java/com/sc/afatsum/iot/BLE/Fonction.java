package com.sc.afatsum.iot.BLE;

import android.bluetooth.BluetoothGattCharacteristic;

import com.sc.afatsum.iot.Handlers.HandlerConnectedBLE;

/**
 * Created by Mustafa on 10/03/2017.
 */


public class Fonction {


    static BluetoothLeService bluetoothLeService;

    //BLECharacteristic
    public static BluetoothGattCharacteristic gattCharacteristic;
    public static int ConnexionMode = 0; //type de connexion avec 0 : BT classic, 1 : OTG , 2 :BLE

    //Distance entre le smartphone et BLE en RSSI
    public static int BLE_Distance_RSSI = 0;



    public static boolean isConnected_BLE() {

        bluetoothLeService = HandlerConnectedBLE.getBluetoothLeService();
        return ((bluetoothLeService != null));
    }

}