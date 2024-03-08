package com.example.sampleproject;

public class contact {
    private String contactName;
    private String Number;
    public contact(String contactName) {
        this.contactName=contactName;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return contactName;
    }

    public contact(String contactName, String number) {
        this.contactName = contactName;
        Number = number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getNumber() {
        return Number;
    }
}
