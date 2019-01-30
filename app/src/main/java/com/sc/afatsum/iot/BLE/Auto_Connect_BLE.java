package com.sc.afatsum.iot.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
//Classe permet d'etablir une connexion automatique avec le Bluetooth LE
public class Auto_Connect_BLE {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    DeviceConnect deviceConnect;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    BLE_ListStored tinydb;
    ArrayList<String> StoredDevice = new ArrayList<String>();

    Context context;
    String TAG = Auto_Connect_BLE.class.getSimpleName();

    public Auto_Connect_BLE(Context ctx) {
        context = ctx;
        mHandler = new Handler();
        tinydb = new BLE_ListStored(ctx);
    }

    public void demarrer() {
        StoredDevice = tinydb.getListObject("BLEtList");
        // getActionBar().setTitle(R.string.title_devices);
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "error_bluetooth_not_supported", Toast.LENGTH_SHORT).show();
            return;
        }
        scanLeDevice(true);
    }

    protected void ConnectDevice(final BluetoothDevice device) {
        if (device == null) return;
        if (deviceConnect == null) {
            deviceConnect = new DeviceConnect(context, device.getName(), device.getAddress());
            Log.d(TAG, "ConnectDevice launch");
        }
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        deviceConnect.demarrer();
    }

    private void scanLeDevice(final boolean enable) {
        Log.d(TAG, "ScanAuto BLE");
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.d(TAG, "startScanAuto BLE");
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    Log.d(TAG, "scanCallback device : " + device.getName());

                    int sizelist = StoredDevice.size();
                    Log.d("BTSCAN", device.getAddress() + "size : " + sizelist);
                    int connect = 0;

                    try {
                        for (int i = sizelist - 1; i >= 0; i--) {
                            Log.d("BTSCAN stored", StoredDevice.get(i));
                            if (StoredDevice.get(i).equals(device.getAddress())) {
                                if (connect == 0) {
                                    ConnectDevice(device);
                                    Log.d("BTSCAN", "connected BLE");
                                }
                                connect = 1;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (connect == 1) {
                        tinydb.putListObject("BLEList", StoredDevice);
                        mScanning = false;
                    }
                }
            };
}