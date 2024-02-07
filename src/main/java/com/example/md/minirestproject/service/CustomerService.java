package com.example.md.minirestproject.service;

import com.example.md.minirestproject.model.Customer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Service Class used to load customer data from a CSV file and add it to the database.
 */
@Service
public class CustomerService {
    public static final String CUSTOMER_ENDPOINT = "/customers";
    Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final WebClient webClient;

    @Autowired
    public CustomerService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<Customer>> addCustomer(Customer newCustomer) {
        return webClient.post()
                .uri(CUSTOMER_ENDPOINT)
                .body(Mono.just(newCustomer), Customer.class)
                .retrieve()
                .toEntity(Customer.class)
                .onErrorMap(error -> {
                    String exceptionMessage = null;
                    if (error instanceof WebClientResponseException webClientResponseException) {
                        exceptionMessage = webClientResponseException.getMessage();
                    }
                    String errorText = "Failed to add customer, ";
                    return new RuntimeException(errorText + exceptionMessage, error);
                });
    }

    public List<Customer> readCustomersFromCsv(Path path) {
        if (!Files.exists(path)) {
            logger.error("Unable to load customers from file. File {} does not exist", path.getFileName());
            return Collections.emptyList();
        }

        List<Customer> customerList = Collections.emptyList();
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<Customer> cb = new CsvToBeanBuilder<Customer>(reader)
                    .withThrowExceptions(false)
                    .withType(Customer.class)
                    .build();

           customerList = cb.parse();
           cb.getCapturedExceptions().forEach(e -> logger.error("Error parsing csv record", e));
        } catch (IOException e) {
            logger.error("Unable to parse {}", path.getFileName(), e);
        }
        return customerList;
    }
}
