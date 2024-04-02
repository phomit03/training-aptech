package com.example.demojunittest;

public class Utils {
    public static boolean ktNt(int n ) {
        if (n < 0)
            throw new ArithmeticException();

        if (n >= 2) {
            for (int i = 2; i <= Math.sqrt(n); i++)
                if (n % i == 0)
                    return false;

            return true;
        }

        return false;
    }
}
