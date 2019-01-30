package com.sc.afatsum.iot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sc.afatsum.iot.BLE.DeviceScanActivity;
import com.sc.afatsum.iot.Handlers.HandlerConnectedBLE;

import static com.sc.afatsum.iot.Config.ValToShow;

public class Mainpage extends AppCompatActivity {
    Button BTTemp;
    Button BTHumd;
    Button BTLum;
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
        BTLum = (Button) findViewById(R.id.BTLum);
        BTLum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lum();
            }
        });
    }

    public void Lum() {
        if (HandlerConnectedBLE.bluetoothLeService != null)
            HandlerConnectedBLE.bluetoothLeService.WriteValue("code140\n");

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
