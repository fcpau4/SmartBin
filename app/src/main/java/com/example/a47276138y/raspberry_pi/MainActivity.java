package com.example.a47276138y.raspberry_pi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a47276138y.raspberry_pi.server.HttpServerThread;
import com.google.android.things.contrib.driver.apa102.Apa102;
import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.ServerSocket;


public class MainActivity extends AppCompatActivity {

    public Bin bin;
    public ManagerDB managerDB;
    private final String BIN_NAME = "PapereraDrTrueta";
    ServerSocket httpServerSocket;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bins");
        DatabaseReference child = myRef.child(BIN_NAME);

        try {
            bin = new Bin(BIN_NAME, "41.401831726, 2.20166586", 20.0f, RainbowHat.openSensor());
            managerDB = new ManagerDB(bin, child);
            managerDB.updateBin();

            bin.getSensors();
            managerDB.pushMeteo();

        }catch(IOException e){
            e.printStackTrace();
        }
        

        try {
            // Light up the Red LED.
            //Leds(RainbowHat.LED_RED, false);
            //Leds(RainbowHat.LED_GREEN, true);
            //Leds(RainbowHat.LED_BLUE, true);


            // Light up the rainbow

            Apa102 ledstrip = RainbowHat.openLedStrip();
            ledstrip.setBrightness(0);
            int[] rainbow = new int[RainbowHat.LEDSTRIP_LENGTH];
            for (int i = 0; i < rainbow.length; i++) {
                rainbow[i] = Color.HSVToColor(255, new float[]{i * 360.f / rainbow.length, 1.0f, 1.0f});
            }
            ledstrip.write(rainbow);
            // Close the device when done.
            ledstrip.close();



        }catch(IOException e){
            e.printStackTrace();
        }


        HttpServerThread httpServerThread = new HttpServerThread(httpServerSocket, this);
        httpServerThread.start();

    }

    public void Leds(String color, Boolean flag) throws IOException {
        Gpio led = RainbowHat.openLed(color);
        led.setValue(flag);
        //led.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (httpServerSocket != null) {
            try {
                httpServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bin getBin() {
        return bin;
    }
}
