package com.example.a47276138y.raspberry_pi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.things.contrib.driver.apa102.Apa102;
import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bin bin = new Bin("Paperera Dr.Trueta", "41.401831726, 2.20166586", 20.0f);

        try {
            bin.meteo.sensortemperature = RainbowHat.openSensor();
            bin.meteo.sensortemperature.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            bin.meteo.sensorpressure = RainbowHat.openSensor();
            bin.meteo.sensorpressure.setPressureOversampling(Bmx280.OVERSAMPLING_1X);

            Bmx280 sensortemperature = RainbowHat.openSensor();
            sensortemperature.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);

        }catch(IOException e){

        }

        /*try {
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

        }*/
    }

    private void Leds(String color, Boolean flag) throws IOException {
        Gpio led = RainbowHat.openLed(color);
        led.setValue(flag);
        //led.close();
    }

}
