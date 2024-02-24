package com.payment;

public class UPI extends Payment{
    void makePayment(double amount) {
    	System.out.println("Payment successfull");
        System.out.println("Paid with UPI. Amount: " + amount+"\n\n");
    }
}