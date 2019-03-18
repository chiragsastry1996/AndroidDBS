package com.hash.nishant.dbs.Status;

public class Status {
    private String incedent_number;
    private String status;
    private String value;

    public Status(String incedent_number, String status, String value) {
        this.incedent_number = incedent_number;
        this.status = status;
        this.value = value;

    }


    public String getIncedent_number() {
        return incedent_number;
    }

    public String getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }
}