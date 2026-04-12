package com.address.model.entity;

import com.address.model.enums.AddressType;
import jakarta.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long empId;
    private String street;
    private String city;
    private String pinCode;
    private String country;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    public Address(Long id, Long empId, String street, String city, String pinCode, String country, AddressType addressType) {
        this.id = id;
        this.empId = empId;
        this.street = street;
        this.city = city;
        this.pinCode = pinCode;
        this.country = country;
        this.addressType = addressType;
    }

    public Address() {
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", empId=" + empId +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", country='" + country + '\'' +
                ", addressType=" + addressType +
                '}';
    }
}
