package com.example.a47276138y.raspberry_pi;

/**
 * Created by 47276138y on 28/03/17.
 */

public class Bin {

    public String _name;
    public String location;
    public float totalWeight;
    public float currWeight;
    public Meteo meteo = new Meteo();

    public Bin(String _name, String location, float totalWeight) {
        this._name = _name;
        this.location = location;
        this.totalWeight = totalWeight;
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
