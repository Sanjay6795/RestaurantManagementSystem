package com.User;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Exceptions.ContactInvalidException;
import com.Exceptions.EmailInvalid;
import com.Exceptions.UserNameInvalidException;
import com.Exceptions.passwordInvalid;
import com.order.MakeOrder;
import com.order.Menu;
import com.reservation.TableReservation;
import com.Interface.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *  The customer class Represents  with registration, login, and
 *  various operations functionalities.
 */
public class Customer {
	
	static Scanner sc = new Scanner(System.in);
	static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    static String dbuserName = "DATABASE";
    static String dbPassword = "Sanjay@143s";
    
    public static void registerNewCustomer() {
        Scanner sc = new Scanner(System.in);
        try {

        String userName = getUserName(sc);
        String password = getValidPassword(sc);
        System.out.println("Enter First Name:");
        String firstName = sc.next();
        System.out.println("Enter Last Name:");
        String lastName = sc.next();
        System.out.println("enter the gender (M -> Male | F -> Female):");
        String gender = sc.next();
        String email = getValidEmail(sc);
        String contactNumber = getValidContactNumber(sc);
        System.out.println("enter the city");
        String city = sc.next();
        
        Connection conn = DriverManager.getConnection(url, dbuserName, dbPassword);
        String maxPersonIdQuery = "SELECT MAX(CustomerID) FROM DATABASE.customer";
        PreparedStatement maxIdStatement = conn.prepareStatement(maxPersonIdQuery);
        ResultSet resultSet = maxIdStatement.executeQuery();
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
        int customerid = maxId + 1;
        
        
       
        String Query = "INSERT INTO Customer (CustomerID, UserName, Password, FirstName, LastName, Gender, Email, contactNumber , City) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, dbuserName, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(Query)) {

            preparedStatement.setInt(1, customerid);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, firstName);
            preparedStatement.setString(5, lastName);
            preparedStatement.setString(6, gender);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, contactNumber);
            preparedStatement.setString(9, city);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
            	
            	System.out.println("Your customer Id is : "+customerid);
            
                System.out.println("New Customer Registered Successfully.");
            } else {
                System.out.println("Failed to register the new customer.");
            }
        } 
        }catch (SQLException | passwordInvalid | UserNameInvalidException | EmailInvalid |ContactInvalidException e) {
            System.out.println( e.getMessage());
           // e.printStackTrace();
        }
    }

    private static String getValidEmail(Scanner sc) throws EmailInvalid{
        while (true) {
        	System.out.println("Enter Email:");
            String email = sc.next();
            if (isValidEmail(email)) {
                return email;
            } 
        }
    }

    private static String getUserName(Scanner sc) throws UserNameInvalidException {
    	while(true) {
    		System.out.println("Enter the userName");
    		String userName = sc.next();
    		if(isValidUserName(userName)) {
    			return userName;
    		}
    	}
    	
    }
    private static String getValidPassword(Scanner sc) throws passwordInvalid {
    	
    		while (true) {
            	System.out.println("password should be alphaNumeric character");
            	System.out.println("Enter Password:");
                String password = sc.next();
                if (isValidPassword(password)) {
                    return password;
                } 
                
            }   
    }

    private static String getValidContactNumber(Scanner sc) throws ContactInvalidException{
        System.out.println("Enter 10 digit contact number ,first digit must be greater than 5");
        while (true) {
        	System.out.println("Enter Contact Number:");
            String contactNumber = sc.next();
            if (isValidContactNumber(contactNumber)) {
                return contactNumber;
            } 
        }
    }


    private static boolean isValidEmail(String email) throws EmailInvalid {
        if(!email.matches( "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
        	throw new EmailInvalid("Invalid Email");
        }
       return true;
    }

    private static boolean isValidUserName(String userName) throws UserNameInvalidException {
    	if(!userName.matches("^[a-zA-Z0-9_]+$")) {
    		throw new UserNameInvalidException("Invalid UserName");
    	}
    	return true;
    }
    
    private static boolean isValidPassword(String password) throws passwordInvalid {
    	if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+]).{8,}$")){
    		throw new passwordInvalid("Invalid Password");
    	}
        return true;
    }

    private static boolean isValidContactNumber(String contactNumber) throws ContactInvalidException{
         if(!contactNumber.matches("[6-9]\\d{9}")) {
        	 throw new ContactInvalidException("Invalid Contact Number");
         }
         return true;
    }

	
    public static boolean login() {
    	
        System.out.println("Enter User Name:");
        String UserName = sc.next();

        System.out.println("Enter Password:");
        String Password = sc.next();

        String selectQuery = "SELECT * FROM Customer WHERE UserName = ? AND Password = ?";

        try (Connection connection = DriverManager.getConnection(url, dbuserName, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, UserName);
            preparedStatement.setString(2, Password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login Successful!\n");
                
                return true;
            } else {
                System.out.println("Invalid details. Login failed.\n");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            return false;
        }  
    }

    public static  void CustomerOperation(Connection connection) {
    	SearchBy search = new SearchBy();
    	
    	boolean exitCustomer = false;
    	while(!exitCustomer) {
    	
    		System.out.println("**************************************");
    		System.out.println("*            User System             *");
    		System.out.println("**************************************");
    		System.out.println("*  1. Register                      *");
    		System.out.println("*  2. Login                         *");
    		System.out.println("*  3. Go Back                       *");
    		System.out.println("**************************************\n");

    		System.out.print("Enter your choice (1-3): ");
    		int customerChoice = sc.nextInt();
    		sc.nextLine(); 

    	
    	
    	
    		switch (customerChoice) {
    			case 1:
    				registerNewCustomer();
    				break;
    			case 2:
    		
    				boolean isLoggedIn = login();

    				if (isLoggedIn) {
    				    boolean goBack = false;
    				    while (!goBack) {
    				    	System.out.println("**********************************************");
    				    	System.out.println("*                Main Menu                    *");
    				    	System.out.println("**********************************************");
    				    	System.out.println("*  1) Reservation                           *");
    				    	System.out.println("*  2) Order                                 *");
    				    	System.out.println("*  3) Go Back                               *");
    				    	System.out.println("**********************************************\n");

    				        System.out.println("Please enter the above operation you want to perform");
    				        int option1 = sc.nextInt();
    				        switch (option1) {
    				            case 1:
    				            	System.out.println("**********************************************");
    				            	System.out.println("*        Welcome to Reservation System       *");
    				            	System.out.println("**********************************************");
    				            	System.out.println("*  1) Make Reservation                      *");
    				            	System.out.println("*  2) Cancel Reservation                    *");
    				            	System.out.println("**********************************************\n");

    				                System.out.println("Enter your option :");
    				                int option = sc.nextInt();
    				               switch(option) {
    				            case 1:
    				                try {
    				                	TableReservation.ReserveTable(connection);
    				                } catch (SQLException e) {
    				                	
    				                	e.printStackTrace();
    				                }
    				                break;
    				            case 2:
    				            	TableReservation.cancelReservation(connection);
    				            	break;
    				            	
    				            default :
    				            	System.out.println("please enter the correct option(1-2)");
    				            	break;
    				             }
    				                break;
    				            case 2:
    				            	boolean back = false;
    				            	while(!back) {
    				            		System.out.println("**********************************************");
    				            		System.out.println("*          Welcome to Order System           *");
    				            		System.out.println("**********************************************");
    				            		System.out.println("*  1) Display Menu                           *");
    				            		System.out.println("*  2) Search by category                     *");
    				            		System.out.println("*  3) Make Order                             *");
    				            		System.out.println("*  4) Go back                                *");
    				            		System.out.println("**********************************************\n");

    				            		System.out.println("Enter your option :");
    				                	int option2 = sc.nextInt();
    				                	switch(option2) {
    				                		case 1:
    				                			Menu.displayAvailableItems(connection);
    				                			break;
    				                		case 2:
    				                			System.out.println("enter the category to search (snack , drink , veg , nonveg) :");
    				                			String category = sc.next();
    				                			search.searchByCategory(connection , category);
    				                			break;
    				                		case 3:
    				                			
    				                			
    				                			try {
    				                				MakeOrder.getOrder(connection);
    				                			} catch (SQLException e) {
    				                				System.out.println(e.getMessage());
    				                				e.printStackTrace();
    				                			}
    				                			break;
    				                		case 4:
    				                			back = true;
    				                			break;
    				                		default :
    				                			System.out.println("Enter the correct option (1-4)");
    				                	}
    				            	}
    				                break;
    				            case 3:
    				                goBack = true;
    				                break;
    				            default:
    				                System.out.println("Invalid choice. Please enter number (1-3)");
    				        }
    				    }
    				}

    				break;
    				case 3:
    					exitCustomer=true;
    					break;
    			default:
    				System.out.println("Invalid choice. Please enter 1 - 3.");
    		}
        }
        
    }
       
}
	
	

