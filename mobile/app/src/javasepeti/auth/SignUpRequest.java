package com.example.javasepeti.auth;

public class SignUpRequest {
    public String fullName;
    public String phone;
    public String email;
    public String password;
    public String address;
    public String city;
    public String town;
    public String street;
    public String apartment;
    public String number;

    public SignUpRequest(String fullName, String phone, String email, String password,
                         String address, String city, String town,
                         String street, String apartment, String number) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.city = city;
        this.town = town;
        this.street = street;
        this.apartment = apartment;
        this.number = number;
    }
}
