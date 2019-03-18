package com.hash.nishant.dbs.Phone;

import android.graphics.drawable.Drawable;

public class Phone {
    private String name;
    private String number;
    private String email;

    public Phone(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;

    }


    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }
}