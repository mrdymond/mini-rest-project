package com.example.md.minirestproject.service;

import com.example.md.minirestproject.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock WebClient webClient;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;
    @Mock private WebClient.RequestBodySpec requestBodySpecMock;
    @SuppressWarnings("rawtypes")
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock private WebClient.ResponseSpec responseSpecMock;
    @InjectMocks
    CustomerService unit;

    @Test
    void givenSuccess_whenAddingCustomer_thenSuccessResponseReceived() {
        // Given
        Customer newCustomer = new Customer();
        ResponseEntity<Customer> expectedResponse = ResponseEntity.ok(newCustomer);
        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("/customers")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any(), eq(Customer.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.toEntity(Customer.class)).thenReturn(Mono.just(expectedResponse));

        // When
        StepVerifier.create(unit.addCustomer(newCustomer))
        // then
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void givenFailure_whenAddingCustomer_thenErrorResponseReceived() {
        // Given
        Customer newCustomer = new Customer();
        WebClientResponseException exception = WebClientResponseException.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null);

        when(webClient.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("/customers")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.body(any(), eq(Customer.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.toEntity(Customer.class)).thenReturn(Mono.error(exception));

        // When
        StepVerifier.create(unit.addCustomer(newCustomer))
        // then
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().contains("Internal Server Error"))
                .verify();
    }

    @Test
    void givenInvalidColumnLength_whenReadCustomersFromCsv_thenRowsExcludedFromList() {
        // given
        String validCsv = "src/test/resources/customers_invalid_columns.csv";

        // when
        List<Customer> customerList = unit.readCustomersFromCsv(Path.of(validCsv));

        // then
        assertEquals(2, customerList.size());
    }

    @Test
    void givenAllRecordsValid_whenReadCustomersFromCsv_thenAllRowsPresentInList() {
        // given
        String validCsv = "src/test/resources/customers_all_valid.csv";

        // when
        List<Customer> customerList = unit.readCustomersFromCsv(Path.of(validCsv));

        // then
        assertEquals(4, customerList.size());
    }

    @Test
    void givenInvalidOrNullFields_whenReadCustomersFromCsv_thenRowsAreExcludedFromList() {
        // given
        String csv_with_null_and_invalid_ref_and_null_name = "src/test/resources/customers_invalid_fields.csv";

        // when
        List<Customer> customerList = unit.readCustomersFromCsv(Path.of(csv_with_null_and_invalid_ref_and_null_name));

        // then
        assertEquals(1, customerList.size());
    }

    @Test
    void givenNullAddressFields_whenReadCustomersFromCsv_thenAllRowsPresentInList() {
        // given
        String csvWithNullName = "src/test/resources/customers_null_address_data.csv";

        // when
        List<Customer> customerList = unit.readCustomersFromCsv(Path.of(csvWithNullName));

        // then
        assertEquals(4, customerList.size());
    }

    @Test
    void givenCsvFileDoesNotExist_whenReadCustomersFromCsv_thenEmptyListReturned() {
        // given
        String nonExistentCsv = "no.csv";

        // when
        List<Customer> customerList = unit.readCustomersFromCsv(Path.of(nonExistentCsv));

        // then
        assertEquals(0, customerList.size());
    }
}
