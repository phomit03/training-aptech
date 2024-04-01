package com.example.thread_queue_jpa.service;

import com.example.thread_queue_jpa.entity.Customer;
import com.example.thread_queue_jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CustomerMultiThreadQueueService {
    @Autowired
    private CustomerRepository customerRepository;

    //dat 1 bien static cho kich thuoc queue
    private static final int QUEUE_SIZE = 3;

    //khai bao 1 thang BlockingQueue
    private final BlockingQueue<Customer> customerQueue = new LinkedBlockingQueue<>();

    //So luong lam viec
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void enQueueCustomer(List<Customer> customers) {
        //Chia danh sach thanh tung phan nho (theo size_queue) neu day vao so luong lon 1 thoi diem
        List<List<Customer>> listCustomerHandled = handleProcessQueue(customers);

        //Them tung phan nho day vao queue va xu ly tung phan
        for (List<Customer> customersHandled : listCustomerHandled) {
            customerQueue.addAll(customersHandled);    // day tung phan nho vao queue
            processQueue();     //Xu ly
        }
    }

    // Xu ly danh sach theo tung phan nho
    private List<List<Customer>> handleProcessQueue(List<Customer> customers) {
        List<List<Customer>> listCustomerHandle = new ArrayList<>();
        for (int i = 0; i < customers.size(); i += CustomerMultiThreadQueueService.QUEUE_SIZE) {
            //lay so phan tu theo size neu kich thuoc vuot qua, neu khong vuot lay so phan tu do luon
            int indexFlag = Math.min(i + CustomerMultiThreadQueueService.QUEUE_SIZE, customers.size());
            //lay so phan tử từ i -> indexFlag
            listCustomerHandle.add(customers.subList(i, indexFlag));
        }
        return listCustomerHandle;
    }

    private void processQueue() {
        executorService.execute(new QueueProcess());
    }

    //xu ly queue va day vao db
    private class QueueProcess implements Runnable {
        @Override
        public void run() {
            List<Customer> listCustomerHandled = new ArrayList<>();

            if (!customerQueue.isEmpty()) {
                while (!customerQueue.isEmpty() && listCustomerHandled.size() < QUEUE_SIZE) {
                    listCustomerHandled.add(customerQueue.poll());  //lay du lieu duoc xu ly tu queue
                }
                if (!listCustomerHandled.isEmpty()) {
                    customerRepository.saveAll((listCustomerHandled));    //Luu vao db
                    customerQueue.clear();  //clear
                }
            }
        }
    }
}
