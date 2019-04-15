package com.sc.afatsum.iot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sc.afatsum.iot.BLE.DeviceScanActivity;
import com.sc.afatsum.iot.Handlers.HandlerConnectedBLE;

import static com.sc.afatsum.iot.Config.ValToShow;

public class Mainpage extends AppCompatActivity {
    Button BTTemp;
    Button BTHumd;
    Button BTLumOn;
    Button BTLumOff;
    LinearLayout LLBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphvalue);
        BTTemp = (Button) findViewById(R.id.bttemp);
        BTTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValToShow = 1;
                ShowTemp();
            }
        });
        BTHumd = (Button) findViewById(R.id.bthumd);
        BTHumd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValToShow = 3;
                showHumd();
            }
        });
        LLBluetooth = (LinearLayout) findViewById(R.id.LLBluetooth);
        LLBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BLEConnect();
            }
        });
        BTLumOn = (Button) findViewById(R.id.BTLum);
        BTLumOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lum(1);
            }
        });

        BTLumOff = (Button) findViewById(R.id.BTLumOff);
        BTLumOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lum(0);
            }
        });
    }
 
    public void Lum(int i) {
        if (HandlerConnectedBLE.bluetoothLeService != null){
            Log.d("sendBLEData","code140");
            HandlerConnectedBLE.bluetoothLeService.WriteValue("code14"+i+"\n");}

    }

    public void BLEConnect() {

        Intent intent = new Intent(this, DeviceScanActivity.class);
        this.startActivity(intent);
    }

    public void ShowTemp() {

        Intent intent = new Intent(this, graphvalue.class);
        this.startActivity(intent);
    }

    public void showHumd() {

        Intent intent = new Intent(this, graphvalue.class);
        this.startActivity(intent);
    }

}
