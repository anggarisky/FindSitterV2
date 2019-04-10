package com.example.findsitter;

public class Sitter {

    String cleaner_id, cleaner_name, latDiff, lngDiff;


    public Sitter() {
    }

    public Sitter(String cleaner_id, String cleaner_name, String latDiff, String lngDiff) {
        this.cleaner_id = cleaner_id;
        this.cleaner_name = cleaner_name;
        this.latDiff = latDiff;
        this.lngDiff = lngDiff;
    }

    public String getCleaner_id() {
        return cleaner_id;
    }

    public void setCleaner_id(String cleaner_id) {
        this.cleaner_id = cleaner_id;
    }

    public String getCleaner_name() {
        return cleaner_name;
    }

    public void setCleaner_name(String cleaner_name) {
        this.cleaner_name = cleaner_name;
    }

    public String getLatDiff() {
        return latDiff;
    }

    public void setLatDiff(String latDiff) {
        this.latDiff = latDiff;
    }

    public String getLngDiff() {
        return lngDiff;
    }

    public void setLngDiff(String lngDiff) {
        this.lngDiff = lngDiff;
    }
}
