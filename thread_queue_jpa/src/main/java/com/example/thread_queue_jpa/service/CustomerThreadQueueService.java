package com.example.thread_queue_jpa.service;

import com.example.thread_queue_jpa.entity.Customer;
import com.example.thread_queue_jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class CustomerThreadQueueService {
    @Autowired
    private CustomerRepository customerRepository;
    private static final int QUEUE_SIZE = 3;
    private final BlockingQueue<Customer> customerQueue = new LinkedBlockingQueue<>();

    // day du lieu vao queue
    public void enQueueCustomer(List<Customer> customers) {
        customerQueue.addAll(customers);
        processQueue();     //Xu ly queue
    }

    // Ham xu ly queue
    private void processQueue() {
        //Tao 1 list de luu danh sach can xu ly
        List<Customer> listCustomerHandle = new ArrayList<>();
        while (!customerQueue.isEmpty() && listCustomerHandle.size() < QUEUE_SIZE) {
            listCustomerHandle.add(customerQueue.poll());    //Lay du lieu tu queue
        }
        if (!listCustomerHandle.isEmpty()) {
            customerRepository.saveAll((listCustomerHandle));    //Luu vao db

            //Neu queue con du lieu, tiep tuc thuc hien tien trinh
            if (!customerQueue.isEmpty()) {
                processQueue();
            }
        }
    }
}
