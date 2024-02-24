package com.payment;

public class CreditCard extends Payment{
	void makePayment(double amount) {
    	System.out.println("Payment successfull");
        System.out.println("Paid with Credit Card. Amount: " + amount+"\n\n");
    }
}
