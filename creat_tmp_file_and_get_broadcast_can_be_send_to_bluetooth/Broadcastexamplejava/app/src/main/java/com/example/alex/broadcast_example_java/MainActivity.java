package com.example.alex.broadcast_example_java;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private String file_path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_broadcast_can_send_to_bluetooth();
    }

    private void append_to_file(String str){
        try {
            File file = new File(file_path);
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true), 96);
            out.write(str);
            out.newLine();
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getTempFile(Context context) {
        String fileName = "broadcast_can_send_to_bluetooth";
        File file=new File(fileName);
        try {
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        System.out.println(file.getPath());
        file_path=file.getPath();
        return file;
    }

    void send_a_intent(String action){
        Intent intent=new Intent();
        intent.setAction(action);
        try{
            sendBroadcast(intent);
            System.out.println("Success "+action);
            append_to_file(action);
        }
        catch(Exception e){
            System.out.println("fail to send broadcast "+action);
            //System.out.println("Error " + e.getMessage());
        }

    }

    void get_broadcast_can_send_to_bluetooth() {
        getTempFile(this.getApplicationContext());
        Resources res =getResources();
        String[] broadcast_names = res.getStringArray(R.array.broadcast_names);
        for (String name:broadcast_names) {
            send_a_intent(name);
        }
    }
}
