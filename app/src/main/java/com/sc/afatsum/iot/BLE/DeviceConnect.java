package com.sc.afatsum.iot.BLE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


import com.sc.afatsum.iot.Handlers.HandlerConnectedBLE;
import com.sc.afatsum.iot.Handlers.HandlerQueue;
import com.sc.afatsum.iot.Handlers.HandlerSemaphore;
import com.sc.afatsum.iot.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by Mustafa on 27/06/2017.
 */

public class DeviceConnect {
    private final static String TAG = DeviceConnect.class.getSimpleName();

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private Context context;
    ArrayList<String> StoredDevices = new ArrayList<String>();

    Semaphore mutex;
    Semaphore semaphore;
    public LinkedBlockingQueue<int[]> queue;

    public DeviceConnect(Context cxt, String Dname, String Dadresse) {
        context = cxt;
        mDeviceName = Dname;
        mDeviceAddress = Dadresse;
        SaveDevice(Dadresse);
    }
    public void SaveDevice(String deviceAdresse){
        Log.d("BTSCAN1",deviceAdresse);
        BLE_ListStored tinydb = new BLE_ListStored(context);
        StoredDevices = tinydb.getListObject("BLEList");
        int sizelist = StoredDevices.size();
        int exist = 0;
        try {
            for (int i = sizelist - 1; i >= 0; i--) {
                if (StoredDevices.get(i).equals(StoredDevices)) {
                    exist = 1;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (exist == 0) {
            //on fait l'ajout le ID de BLE dans la liste
            StoredDevices.add(deviceAdresse);
        }
        tinydb.putListObject("BLEList", StoredDevices);
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            HandlerConnectedBLE.setBluetoothLeService(mBluetoothLeService);
            Fonction.ConnexionMode = 2;
            context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            // Automatically connects to the device upon successful start-up initialization.

            semaphore = HandlerSemaphore.getSemaphore();
            mutex = HandlerSemaphore.getMutex();
            queue = HandlerQueue.getLinkedBlockingQueue();
            Log.d(TAG, "get semaphore mutex queue");
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            HandlerConnectedBLE.setBluetoothLeService(null);
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG,"probleme detected dc");
                mConnected = false;
                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                // displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    public void ConnectService(BluetoothGattCharacteristic characteristic) {
        if (mGattCharacteristics != null) {
            Log.d("charact", characteristic.toString());
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(
                            mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                Fonction.gattCharacteristic=characteristic;
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(
                        characteristic, true);
            }
        } else {
        }
    }

    public void demarrer() {
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
        Log.d(TAG, "deviceConnect launch");
    }

    private void updateConnectionState(final int resourceId) {
        Log.d("connect stat", resourceId + "");
    }
/*
    private void displayData(String data) {
        if (data != null) {
            Log.d("data field", data + "length : "+data.length());
        }
    }*/

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                if (gattCharacteristic.getUuid().toString().equals("0000ffe1-0000-1000-8000-00805f9b34fb")) {
                    Log.d(TAG, "detected");
                    ConnectService(gattCharacteristic);
                }
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
