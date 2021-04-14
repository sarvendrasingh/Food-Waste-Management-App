package com.example.food_waste_management_app;

public class CustomerInfo {
    // string variable for
    // storing employee name.
    private String CustomerName;

    // string variable for storing
    // employee contact number
    private String CustomerEmail;

    // string variable for storing
    // employee address.
    private String CustomerAddress;

    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    public CustomerInfo() {

    }

    // created getter and setter methods
    // for all our variables.
    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String CustomerEmail) {
        this.CustomerEmail = CustomerEmail;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String CustomerAddress) {
        this.CustomerAddress = CustomerAddress;
    }
}
