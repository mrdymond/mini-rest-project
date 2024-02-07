package com.example.md.minirestproject.util;

import com.example.md.minirestproject.model.Customer;
import com.example.md.minirestproject.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

/**
 * Runs at startup to read customers from csv and load into database.
 */
@Component
public class CustomerDataLoader implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(CustomerDataLoader.class);
    @Value("${csvPath}")
    private String csvPath;
    private CustomerService customerService;

    @Autowired
    public CustomerDataLoader(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) {
        if (args.length > 0) {
            csvPath = args[0];
        }
        List<Customer> customers = customerService.readCustomersFromCsv(Path.of(csvPath));
        for (Customer customer : customers) {
            addCustomer(customer);
        }
    }

    private void addCustomer(Customer customer) {
        customerService.addCustomer(customer).subscribe(
                successResponse -> logger.info("Customer {} added successfully.", successResponse.getBody().getRef()),
                errorResponse -> logger.error("Error adding customer {}: {} ",customer.getRef(), errorResponse.getMessage())
        );
    }
}
