package com.example.md.minirestproject.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Customer {

    @Id
    @NotNull(message = "customer ref cannot be null")
    @CsvBindByName(column = "Customer Ref")
    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[a-zA-Z0-9]+$")
    private String ref;
    @NotNull(message = "name cannot be null")
    @CsvBindByName(column = "Customer Name")
    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^(?=\\s*\\S).*$")
    private String name;
    @Embedded
    @AttributeOverride( name = "line1", column = @Column(name = "addressLine1"))
    @AttributeOverride( name = "line2", column = @Column(name = "addressLine2"))
    @CsvRecurse
    private Address address;

    public Customer(String ref, String name, Address address) {
        this.ref = ref;
        this.name = name;
        this.address = address;
    }

    public Customer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
