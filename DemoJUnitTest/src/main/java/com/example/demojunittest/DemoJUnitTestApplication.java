package com.example.demojunittest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoJUnitTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJUnitTestApplication.class, args);

        int[] a = {2, 3, 5, 6, 4, 8, 9, 11};

        int sum = 0;
        for (int x: a)
            if (Utils.ktNt(x))
                sum += x;

        System.out.println(sum);
    }

}
