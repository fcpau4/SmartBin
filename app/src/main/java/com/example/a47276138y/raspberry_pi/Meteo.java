package com.example.a47276138y.raspberry_pi;

import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.firebase.database.Exclude;

import java.io.IOException;
import java.util.Date;

/**
 * Created by 47276138y on 28/03/17.
 */

public class Meteo {
    @Exclude
    public Bmx280 sensor;

    @Exclude
    public long timestamp;

    public float degrees;
    public float pressure;

    public Meteo(Bmx280 sensor) {
        try {
            this.sensor = sensor;
            this.sensor.setTemperatureOversampling(Bmx280.OVERSAMPLING_1X);
            this.sensor.setPressureOversampling(Bmx280.OVERSAMPLING_1X);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getSensors(){
        this.timestamp = new Date().getTime();
        try {
            this.degrees = this.sensor.readTemperature();
            this.pressure = this.sensor.readPressure();
            //this.sensor.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
