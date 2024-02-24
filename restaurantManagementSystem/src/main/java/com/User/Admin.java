package com.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.order.Menu;
import com.reservation.TableReservation;
import com.reservation.Tables;
/**
 * The class Admin Represents functionalities for 
 * managing reservations, tables, and menu items.
 */
public class Admin {
	
	
	private static String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String userName = "DATABASE";
    private static String password = "Sanjay@143s";

    
    public static Connection connection; 
    /**
     * Handles the login functionality for the admin.
     *
     */

    public static void adminLogin(Connection connection) {
    	
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Enter the Admin UserName");
        String inputUserName = scanner.nextLine(); 
    	System.out.println("Enter the Admin Password");
        String inputPassword = scanner.nextLine(); 

        try {
            if (connection != null) {
                if (isAdminValid(connection, inputUserName, inputPassword)) {
                    System.out.println("Login successful as Administrator!\n\n");
                    AdminFunctionality();
                } else {
                    System.out.println("Invalid UserName or Password. Login failed.");
                }
            } else {
                System.out.println("Not Connected");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //  Validates the admin credentials against the database.

    private static boolean isAdminValid(Connection connection, String inputUserName, String inputPassword) throws SQLException {
        String query = "SELECT * FROM ADMIN WHERE UserName = ? AND Password = ?";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            stat.setString(1, inputUserName);
            stat.setString(2, inputPassword);

            try (ResultSet resultSet = stat.executeQuery()) {
                return resultSet.next(); 
            }
        }
    }
    
    /**
     * Handles the functionalities available to the admin.
     */
    
   public static void AdminFunctionality() {
	   Scanner scanner = new Scanner(System.in);
	   try {
       	Class.forName("oracle.jdbc.driver.OracleDriver");
           connection = DriverManager.getConnection(URL, userName, password);
       } catch (SQLException e) {
           System.err.println("Error connecting to the database: " + e.getMessage());
           System.exit(1);
       }
       catch(ClassNotFoundException e) {
       	e.getMessage();
       }

	   boolean exitAdmin = false;
	   while (!exitAdmin) {
		   try {

		   System.out.println("******************************************");
	        System.out.println("*         Admin Functionalities         *");
	        System.out.println("******************************************");
	        System.out.println("* 1) Make Reservation                   *");
	        System.out.println("* 2) Cancel Reservation                 *");
	        System.out.println("* 3) View Reserved Tables               *");
	        System.out.println("* 4) Add Table                          *");
	        System.out.println("* 5) Delete Table                       *");
	        System.out.println("* 6) Display All Tables                 *");
	        System.out.println("* 7) Add Menu                            *");
	        System.out.println("* 8) Delete Menu                         *");
	        System.out.println("* 9) Display Menu                        *");
	        System.out.println("* 10) Go Back                            *");
	        System.out.println("******************************************\n");
		    System.out.println("Enter your choice :");
		  //  try {
		    int option = scanner.nextInt();
		    switch (option) {
		        case 1:
		            TableReservation.reserveTables(connection);
		            break;
		        case 2:
		            TableReservation.cancelReservation(connection);
		            break;
		        case 3:
		            TableReservation.displayReservedTables(connection);
		            break;
		        case 4:
		        	Tables.addTable(connection);
		            break;
		        case 5: 
		        	Tables.deleteTable(connection);
		        	break;
		        case 6: 
		        	Tables.displayAllTables(connection);
		        	break;
		        	
		        case 7:
		        	Menu.addItem(connection);
		        	break;
		        case 8:
		            Menu.deleteItem(connection);
		            break;
		        case 9:
		            Menu.displayAvailableItems(connection);
		            break;
		        case 10:
		            exitAdmin = true;
		            break;
		        default:
		            System.out.println("Invalid choice. Please enter a number between 1 and 10.");
		            break;
		    }
		    
	   
		}catch(InputMismatchException e) {
			System.out.println(e.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	   }
   }
}
