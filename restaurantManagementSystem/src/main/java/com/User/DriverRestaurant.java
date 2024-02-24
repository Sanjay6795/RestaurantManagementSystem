package com.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.order.Menu;
import com.reservation.TableReservation;

/**
 * The main class for the Restaurant Management System.
 */
public class DriverRestaurant {

	private static String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String userName = "DATABASE";
    private static String password = "Sanjay@143s";

    
    public static Connection connection; 
    /**
     * The main entry point for the Restaurant Management System.
     *
     */
    public static void main(String[] args) {
    	
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
       
        try {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(URL, userName, password);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        catch(ClassNotFoundException e) {
        	e.getMessage();
        }
        
        while(true){
           try {
            	System.out.println("===========================================");
            	System.out.println("|     Welcome to Restaurant Management     |");
            	System.out.println("|               System                     |");
            	System.out.println("===========================================\n\n");

            	System.out.println("╔════════════════════════════════════════╗");
            	System.out.println("║         Select User Type               ║");
            	System.out.println("╚════════════════════════════════════════╝");
            	System.out.println("1. Admin");
            	System.out.println("2. Customer");
            	System.out.println("3. Exit");

                System.out.print("Enter your choice (1-3): ");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                System.out.println();

                switch (choice) {
                    case 1:
            			Admin.adminLogin(connection);
            			break;
                    case 2:
                    	Customer.CustomerOperation(connection);
                        break;
                    case 3:
                        System.out.println("Thank You");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 3");
                        break;
                }
        	
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.\n\n");
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("Invalid Input\n\n");
            }
        }
    }
    
}  
   