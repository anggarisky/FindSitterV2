package com.example.findsitter;

public class Sitter {

    String cleaner_name, cleaner_id;

    public Sitter() {
    }

    public Sitter(String cleaner_name, String cleaner_id) {
        this.cleaner_name = cleaner_name;
        this.cleaner_id = cleaner_id;
    }

    public String getCleaner_name() {
        return cleaner_name;
    }

    public void setCleaner_name(String cleaner_name) {
        this.cleaner_name = cleaner_name;
    }

    public String getCleaner_id() {
        return cleaner_id;
    }

    public void setCleaner_id(String cleaner_id) {
        this.cleaner_id = cleaner_id;
    }
}
