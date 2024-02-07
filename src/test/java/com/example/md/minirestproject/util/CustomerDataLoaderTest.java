package com.example.md.minirestproject.util;

import com.example.md.minirestproject.model.Customer;
import com.example.md.minirestproject.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerDataLoaderTest {
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CustomerDataLoader unit;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field csvPathField = CustomerDataLoader.class.getDeclaredField("csvPath");
        csvPathField.setAccessible(true);
        csvPathField.set(unit, "dummy.csv");
    }

    @Test
    void givenCustomersInCsv_whenApplicationRuns_thenAddCustomerCalled() {
        // Given
        List<Customer> customerList = List.of(new Customer(), new Customer());
        when(customerService.readCustomersFromCsv(any(Path.class))).thenReturn(customerList);
        when(customerService.addCustomer(any(Customer.class))).thenReturn(Mono.empty());

        // When
        unit.run();

        // Then
        verify(customerService, times(2)).addCustomer(any(Customer.class));
        verify(customerService, times(1)).readCustomersFromCsv(any(Path.class));
    }
    @Test
    void givenNoCustomersInCsv_whenApplicationRuns_thenAddCustomerIsNotCalled() {
        // Given
        List<Customer> customerList = Collections.emptyList();
        when(customerService.readCustomersFromCsv(any(Path.class))).thenReturn(customerList);

        // When
        unit.run();

        // Then
        verify(customerService, never()).addCustomer(any(Customer.class));
        verify(customerService, times(1)).readCustomersFromCsv(any(Path.class));
    }
}
