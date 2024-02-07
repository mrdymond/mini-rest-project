package com.example.md.minirestproject.controller;

import com.example.md.minirestproject.model.Address;
import com.example.md.minirestproject.model.Customer;
import com.example.md.minirestproject.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CustomerControllerTest {
    public static final String CUSTOMER_JSON = "{\"ref\":\"xxx\",\"name\":\"Dummy Customer\"}";
    public static final String POST_URL = "/customers";
    public static final String GET_URL = "/customers/xxx";
    private Customer customer;
    @MockBean private CustomerRepository customerRepository;
    @Autowired MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        customer = new Customer("xxx","Dummy Customer", new Address());
    }

    @Test
    void givenCustomerDoesNotExist_whenAddCustomer_then201Returned() throws Exception {
        // given
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // when
        mockMvc.perform(post(POST_URL)
                    .contentType("application/json")
                    .content(CUSTOMER_JSON))
        //then
                    .andExpect(status().isCreated());
    }

    @Test
    void givenCustomerExists_whenAddCustomer_then400Returned() throws Exception {
        // given
        when(customerRepository.findById(any(String.class))).thenReturn(java.util.Optional.of(customer));

        //when
        mockMvc.perform(post(POST_URL)
                    .contentType("application/json")
                    .content(CUSTOMER_JSON))
         //then
                    .andExpect(status().isBadRequest());
    }

    @Test
    void givenCustomerExists_whenGetCustomer_then200Returned() throws Exception {
        //given
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.of(customer));

        //when
        mockMvc.perform(get(GET_URL))
        //then
                .andExpect(status().isOk())
                .andExpect(content().json(CUSTOMER_JSON));
    }

    @Test
    void givenCustomerDoesNotExist_whenGetCustomer_then404Returned() throws Exception {
        //given
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.empty());

        //when
        mockMvc.perform(get(GET_URL))
        //then
                .andExpect(status().isNotFound());
    }
}
