package com.example.alex.bluetooth_java_example;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> mArrayAdapter=new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter;

    class BluetoothThread extends Thread {
        @Override
        public void run(){
            registerBroadcast();
            bluetoothLocalInit();
            mBluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new BluetoothThread().start();
    }

    @Override
    protected void onDestroy(){
        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            System.out.println("bluetooth didn't open, but now has success enabled");
        }
        else if(requestCode == RESULT_CANCELED){
            System.out.println("some error happen, or user deny to open bluetooth");
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // program cannt be here, I don'nt know why
                System.out.println("mReceiver receive ACTION_FOUND");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                System.out.println("mReceiver receive ACTION_BOND_STATE_CHANGED");
            }
            else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
                System.out.println("mReceiver receive ACTION_SCAN_MODE_CHANGED");
            }
            else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                System.out.println("mReceiver receive ACTION_STATE_CHANGED");
            }

            for (String str:mArrayAdapter) {
                System.out.println("413x: "+str);
            }
        }
    };

    private void registerBroadcast(){
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, intent);
    }

    private void bluetoothLocalInit(){

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        else{
            System.out.println("bluetooth has opened");
        }
    }
}
