package com.example.a47276138y.raspberry_pi;

/**
 * Created by 47276138y on 28/03/17.
 */

public class Bin {

    public String _name;
    public String location;
    public float totalWeight;
    public float currWeight;
    public Meteo meteo;

    public Bin(String _name, String location, float totalWeight) {
        this._name = _name;
        this.location = location;
        this.totalWeight = totalWeight;
    }


}
