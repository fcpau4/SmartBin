package com.example.a47276138y.raspberry_pi;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * Created by 47276138y on 04/04/17.
 */

public class ManagerDB {
    private Bin paperera;
    private DatabaseReference db;

    public ManagerDB(Bin paperera, DatabaseReference db) {
        this.paperera = paperera;
        this.db = db;
    }

    public void updateBin(){
        paperera.updated_at = new Date().getTime();
        db.child("bin").setValue(paperera);
    }

    public void pushMeteo(){
        db.child("meteo").child(""+paperera.meteo.timestamp).setValue(paperera.meteo);
    }

}
