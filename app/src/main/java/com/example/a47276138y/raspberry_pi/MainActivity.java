package com.example.a47276138y.raspberry_pi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.things.contrib.driver.apa102.Apa102;
import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bin bin = new Bin("PapereraDrTrueta", "41.401831726, 2.20166586", 20.0f);

        try {
            bin.meteo.sensor = RainbowHat.openSensor();
            bin.meteo.sensor.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            bin.meteo.sensor.setPressureOversampling(Bmx280.OVERSAMPLING_1X);
            bin.meteo.degrees = bin.meteo.sensor.readTemperature();
            bin.meteo.pressure = bin.meteo.sensor.readPressure();
            bin.meteo.sensor.close();

            //bin.meteo.sensor = RainbowHat.openSensor();
            //bin.meteo.sensor.setPressureOversampling(Bmx280.OVERSAMPLING_1X);
            //bin.meteo.pressure = bin.meteo.sensor.readPressure();
        }catch(IOException e){
            e.printStackTrace();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bins");
        myRef.child(bin.get_name()).setValue(bin);

        /*
        try {
            // Light up the Red LED.
            Leds(RainbowHat.LED_RED, false);
            //Leds(RainbowHat.LED_GREEN, true);
            //Leds(RainbowHat.LED_BLUE, true);


            // Light up the rainbow
            Apa102 ledstrip = RainbowHat.openLedStrip();
            ledstrip.setBrightness(31);
            int[] rainbow = new int[RainbowHat.LEDSTRIP_LENGTH];
            for (int i = 0; i < rainbow.length; i++) {
                rainbow[i] = Color.HSVToColor(255, new float[]{i * 360.f / rainbow.length, 1.0f, 1.0f});
            }
            ledstrip.write(rainbow);
            // Close the device when done.
            //ledstrip.close();


        }catch(IOException e){

        }
        */
    }

    private void Leds(String color, Boolean flag) throws IOException {
        Gpio led = RainbowHat.openLed(color);
        led.setValue(flag);
        //led.close();
    }

}
