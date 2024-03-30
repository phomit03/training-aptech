package com.example.thread_queue_jpa.controller;

import com.example.thread_queue_jpa.entity.Customer;
import com.example.thread_queue_jpa.service.CustomerMultiThreadQueueService;
import com.example.thread_queue_jpa.service.CustomerQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    CustomerMultiThreadQueueService customerMultiThreadQueueService;

    @Autowired
    CustomerQueueService customerQueueService;

    @PostMapping("/multi_thread/save")
    public ResponseEntity<String> addCustomerMultiThread(@RequestBody List<Customer> customers) {
        customerMultiThreadQueueService.enQueueCustomer(customers);
        return new ResponseEntity<>("Customer added successfully using multi thread", HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public ResponseEntity<String> addCustomerThread(@RequestBody List<Customer> customers) {
        customerQueueService.enQueueCustomer(customers);
        return new ResponseEntity<>("Customer added successfully using thread", HttpStatus.CREATED);
    }

    /*@PutMapping("update/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        customerQueueService.editCustomer(customer);
        return new ResponseEntity<>("Customer updated successfully", HttpStatus.OK);
    }*/
}
