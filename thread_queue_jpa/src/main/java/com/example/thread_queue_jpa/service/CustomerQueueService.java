package com.example.thread_queue_jpa.service;

import com.example.thread_queue_jpa.entity.Customer;
import com.example.thread_queue_jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class CustomerQueueService {
    @Autowired
    private CustomerRepository customerRepository;
    private static final int QUEUE_SIZE = 3;
    private final BlockingQueue<Customer> customerQueue = new LinkedBlockingQueue<>();

    // day du lieu vao queue
    public void enQueueCustomer(List<Customer> customers) {
        for (Customer customer : customers) {
            customerQueue.add(customer);    //day du lieu vao queue

            // kiem tra neu du lieu day vao queue qua kich thuoc toi da
            if (customerQueue.size() >= QUEUE_SIZE) {
                processQueue();     // Xu ly queue
            }
        }
    }

    // Ham xu ly queue
    private void processQueue() {
        //Tao 1 list de luu danh sach can xu ly
        List<Customer> listCustomerHandled = new ArrayList<>();

        //kiem tra queue co gia tri hay khong
        if (!customerQueue.isEmpty()) {
            while (!customerQueue.isEmpty() && listCustomerHandled.size() < QUEUE_SIZE) {
                listCustomerHandled.add(customerQueue.poll());    //Lay du lieu tu queue voi kich thuoc toi da
            }
            if (!listCustomerHandled.isEmpty()) {
                customerRepository.saveAll((listCustomerHandled));    //Luu vao db
                customerQueue.clear();  //clear
            }
        }
    }
}
