package com.payment;

import java.util.Scanner;

public class Payment {
    public static void paymentMethod(double amount) {
        Scanner scanner = new Scanner(System.in);
        if(amount > 0) {
        	System.out.println("Enter payment type (1)credit card, 2)upi, 3)cash):");
            int paymentType = scanner.nextInt();

            switch (paymentType) {
                case 1:
                    CreditCard creditCardPayment = new CreditCard();
                    creditCardPayment.makePayment(amount);
                    break;
                case 2:
                    UPI upiPayment = new UPI();
                    upiPayment.makePayment(amount);
                    break;
                case 3:
                    Cash cashPayment = new Cash();
                    cashPayment.makePayment(amount);
                    break;
                default:
                    System.out.println("Invalid payment type");
            }
        }
        else {
        	System.out.println("cannot  process payment");
        }
        
    }
}
