package com.sc.afatsum.iot.Handlers;


import com.sc.afatsum.iot.BLE.BluetoothLeService;

/**
 * Created by Mustafa on 28/06/2017.
 */

//Class permet d'enregistrer temporairement le service de BLE connecté

public class HandlerConnectedBLE {
    public static BluetoothLeService bluetoothLeService;

    public static BluetoothLeService getBluetoothLeService() {
        return bluetoothLeService;
    }

    public static void setBluetoothLeService(BluetoothLeService bluetoothLeService) {
        HandlerConnectedBLE.bluetoothLeService = bluetoothLeService;
    }

}
