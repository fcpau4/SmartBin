package com.example.a47276138y.raspberry_pi;

import com.google.android.things.contrib.driver.bmx280.Bmx280;
import com.google.firebase.database.Exclude;

import java.io.IOException;
import java.util.Date;

/**
 * Created by 47276138y on 28/03/17.
 */

public class Bin {

    public String _name;
    public String location;
    public float totalWeight;
    public float currWeight;
    public long updated_at;

    @Exclude
    public Meteo meteo;

    public Bin(String _name, String location, float totalWeight, Bmx280 sensor) {
        this._name = _name;
        this.location = location;
        this.totalWeight = totalWeight;

        this.meteo = new Meteo(sensor);
        this.meteo.sensor = sensor;
    }



    public void getSensors(){
        this.meteo.getSensors();
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getCurrWeight() {
        return currWeight;
    }

    public void setCurrWeight(float currWeight) {
        this.currWeight = currWeight;
    }
}
