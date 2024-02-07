package com.example.md.minirestproject.model;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    @CsvBindByName(column = "Address Line 1")
    private String line1;
    @CsvBindByName(column = "Address Line 2")
    private String line2;
    @CsvBindByName(column = "Town")
    private String town;
    @CsvBindByName(column = "County")
    private String county;
    @CsvBindByName(column = "Country")
    private String country;
    @CsvBindByName(column = "Postcode")
    private String postCode;

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}