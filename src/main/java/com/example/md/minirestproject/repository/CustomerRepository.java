package com.example.md.minirestproject.repository;

import com.example.md.minirestproject.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
}
