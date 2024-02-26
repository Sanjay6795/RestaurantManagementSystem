package com.User;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Exceptions.CityException;
import com.Exceptions.ContactInvalidException;
import com.Exceptions.EmailInvalid;
import com.Exceptions.FirstNameException;
import com.Exceptions.GenderException;
import com.Exceptions.LastNameException;
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
        String firstName = getFirstName(sc);
        String lastName = getLastName(sc);
        String gender = getGender(sc);
        String email = getValidEmail(sc);
        String contactNumber = getValidContactNumber(sc);
        String city = getCity(sc);
        
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
            	            
                System.out.println("New Customer Registered Successfully.");
            	System.out.println("Your customer Id is : "+customerid+"\n");
            	

            } else {
                System.out.println("Failed to register the new customer.");
            }
        } 
   
        }catch(Exception e) {    
        	System.out.println( e.getMessage());
        }
        
    }

    private static String getValidEmail(Scanner sc) {
        while (true) {
        	System.out.println("Enter Email:");
            String email = sc.next();
            try {
            if (isValidEmail(email)) {
                return email;
            }
            }catch(Exception e) {
            	System.out.println(e.getMessage());
            }
        }
    }
    private static boolean isValidEmail(String email) throws EmailInvalid {
        if(!email.matches( "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
        	throw new EmailInvalid("Invalid Email");
        }
       return true;
    }
    
    

    private static String getUserName(Scanner sc)  {
    	while(true) {
    		System.out.println("Enter the userName");
    		String userName = sc.next();
    		try {
    			if(isValidUserName(userName)) {
    				return userName;
    			}
    		}
    		catch(UserNameInvalidException e) {
        	System.out.println(e.getMessage());
    		}
    	}
    	
    }
    private static boolean isValidUserName(String userName) throws UserNameInvalidException {
    	if(!userName.matches("^[a-zA-Z0-9_]+$")) {
    		throw new UserNameInvalidException("Invalid UserName");
    	}
    	return true;
    }
    
    
    private static String getValidPassword(Scanner sc)  {
    	
    		while (true) {
            	System.out.println("password should be alphaNumeric character");
            	System.out.println("Enter Password:");
                String password = sc.next(); 
                try {
                	if (isValidPassword(password)) {
                		return password;
                	} 
                }catch(passwordInvalid e) {
                	System.out.println(e.getMessage());
                }
                
            }   
    }
    private static boolean isValidPassword(String password) throws passwordInvalid {
    	if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+]).{8,}$")){
    		throw new passwordInvalid("Invalid Password");
    	}
        return true;
    }
    
    
    
    private static String getFirstName(Scanner sc) throws FirstNameException{
    	while (true) {
        	System.out.println("Enter First Name:");
            String firstName = sc.next();
            try {
            if (isValidFirstName(firstName)) {
                return firstName;
            } 
            }catch(FirstNameException e) {
            	System.out.println(e.getMessage());
            }
        }
    }
    private static boolean isValidFirstName(String firstName) throws FirstNameException {
        if(!firstName.matches( "^[A-Za-z]{1,20}$")) {
        	throw new FirstNameException("Invalid FirstName");
        }
       return true;
    }
   
    
    private static String getLastName(Scanner sc) {
    	while (true) {
        	System.out.println("Enter Last Name:");
            String lastName = sc.next();
            try {
            if (isValidLastName(lastName)) {
                return lastName;
            } 
            }catch(LastNameException e) {
            	System.out.println(e.getMessage());
            }
        }
    }
    private static boolean isValidLastName(String lastName) throws LastNameException {
        if(!lastName.matches( "^[A-Za-z]{1,20}$")) {
        	throw new LastNameException("Invalid LastName");
        }
       return true;
    }
   
 
    private static String getValidContactNumber(Scanner sc) {
        System.out.println("Enter 10 digit contact number ,first digit must be greater than 5");
        while (true) {
        	System.out.println("Enter Contact Number:");
            String contactNumber = sc.next();
            
            try {
            	if (isValidContactNumber(contactNumber)) {
            		return contactNumber;
            	} 
            }catch(ContactInvalidException e) {
            	System.out.println(e.getMessage());

            }
        }
    }
 
    private static boolean isValidContactNumber(String contactNumber) throws ContactInvalidException{
         if(!contactNumber.matches("[6-9]\\d{9}")) {
        	 throw new ContactInvalidException("Invalid Contact Number");
         }
         return true;
    }
  
    
    private static String getGender(Scanner sc) {
    	while(true){
    		System.out.println("enter the gender (M -> Male | F -> Female):");
    		String gender = sc.next().toUpperCase();
    		try {
    			if(isValidGender(gender)) {
        			return gender;
    			}
    		}catch(GenderException e){
    			System.out.println(e.getMessage());	
    		}
    		
    	}	
    }
    private static boolean isValidGender(String gender) throws GenderException{
		if(!gender.matches("^[MF]$")) {
			throw new GenderException("Invalid Gender");
		}
		return true;
	}

    private static String getCity(Scanner sc) throws CityException {
        while (true) {
            System.out.println("Enter City:");
            String city = sc.next();
            try {
                if (isValidCity(city)) {
                    return city;
                }
            } catch (CityException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean isValidCity(String city) throws CityException {
        if (!city.matches("^[A-Za-z\\s]{1,50}$")) {
            throw new CityException("Invalid City. city contains only alphabets");
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
	
	

