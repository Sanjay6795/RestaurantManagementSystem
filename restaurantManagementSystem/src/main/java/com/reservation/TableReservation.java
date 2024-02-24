package com.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.payment.*;

public class TableReservation {

    static Scanner sc = new Scanner(System.in);
    Bill bill = new Bill();
    public static void ReserveTable(Connection connection) throws SQLException {
        // Display available tables
    
        displayAvailableTables(connection);
     try {
        System.out.println("Enter customerId");
        int customerID = sc.nextInt();
        // Get reservation details
        System.out.println("Enter Reservation Date (yyyy-MM-DD):");
        String reservationDate = sc.next();

        System.out.println("Enter Reservation Time (HH:mm):");
        String reservationTime = sc.next();

        System.out.println("Enter Table Number:");
        int tableNumber = sc.nextInt();
        
        String maxreserveID = "SELECT MAX(ReservationID) FROM DATABASE.reservation";
        PreparedStatement maxIdStatement = connection.prepareStatement(maxreserveID);
        ResultSet resultSet = maxIdStatement.executeQuery();
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
        int reservationID = maxId + 1;
       
    
        // Check if the selected table is available
        if (isTableAvailable(connection, tableNumber, reservationDate, reservationTime)) {
            // If available, proceed with the reservation
            String status = "reserved";

            String insertQuery = "INSERT INTO Reservation (ReservationID ,CustomerID ,ReservationDate, ReservationTime, TableNo, Status) VALUES (?,?, TO_DATE(?, 'YYYY-MM-DD'), TO_TIMESTAMP(?, 'HH24:MI'), ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, reservationID);
                preparedStatement.setInt(2, customerID);
                preparedStatement.setString(3, reservationDate);
                preparedStatement.setString(4, reservationTime);
                preparedStatement.setInt(5, tableNumber);
                preparedStatement.setString(6, status);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                	
                   // System.out.println("Table Booking Successful.");
                    double price = getTablePrice(connection, tableNumber); 
                    
                    //System.out.println(price);
                    try {
                    	Bill.generateBill( connection ,reservationID, customerID, tableNumber,  price);
                    }
                    catch(Exception e){
                    	System.out.println(e.getMessage());
  
                    }
                } 
                else {
                    System.out.println("Failed to book the table.");
                }
            } catch (SQLException e) {
                System.err.println("Database Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // If not available, display a message
            System.out.println("The selected table is unavailable for the specific date and time. Please choose another table.");
        }
     }catch(InputMismatchException e) {
        	System.out.println(e.getMessage());
     }
    }
    
  
    
    public static void reserveTables(Connection connection) {
        // Display available tables
        displayAvailableTables(connection);

        try {
            // Get reservation details

            System.out.println("\nEnter customerID:");
            int customerID = sc.nextInt();

            System.out.println("Enter AdminID:");
            int adminID = sc.nextInt();

            System.out.println("Enter Reservation Date (yyyy-MM-DD):");
            String reservationDate = sc.next();

            System.out.println("Enter Reservation Time (HH:mm):");
            String reservationTime = sc.next();

            System.out.println("Enter Table Number:");
            int tableNumber = sc.nextInt();
            
            
            String maxreserveID = "SELECT MAX(ReservationID) FROM DATABASE.reservation";
            PreparedStatement maxIdStatement = connection.prepareStatement(maxreserveID);
            ResultSet resultSet = maxIdStatement.executeQuery();
            int maxId = 0;
            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }
            int reservationID = maxId + 1;

            if (isTableAvailable(connection, tableNumber, reservationDate, reservationTime)) {
                String status = "reserved";
                String insertQuery = "INSERT INTO Reservation (ReservationID, CustomerID, ReservationDate, ReservationTime, AdminID, TableNo, Status) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_TIMESTAMP(?, 'HH24:MI'), ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setInt(1, reservationID);
                    preparedStatement.setInt(2, customerID);
                    preparedStatement.setString(3, reservationDate);
                    preparedStatement.setString(4, reservationTime);
                    preparedStatement.setInt(5, adminID);
                    preparedStatement.setInt(6, tableNumber);
                    preparedStatement.setString(7, status);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        //System.out.println("Table Booking Successful.");
                        double price = getTablePrice(connection, tableNumber); 
                        
                        try {
                        	Bill.generateBill( connection ,reservationID, customerID, tableNumber,  price);
                        }
                        catch(Exception e){
                        	System.out.println(e.getMessage());
      
                        }
                        
                        
                    } else {
                        System.out.println("Failed to book the table.");
                    }
                }
            } else {
                // If not available, display a message
                System.out.println("The selected table is unavailable for the specific date and time. Please choose another table.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter valid values.");
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            //e.printStackTrace();
        }
    }

    private static void displayAvailableTables(Connection connection) {
        // SQL query to select available tables
        String selectQuery = "SELECT TableNo, price FROM Tables WHERE TableStatus = 'available'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            System.out.println("Available Tables:\n");
            System.out.println("TableNumber\tprice");

            while (resultSet.next()) {
                int tableNo = resultSet.getInt("TableNo");
                double price = resultSet.getDouble("price");
                System.out.println(tableNo+"\t\t"+price);
            }

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isTableAvailable(Connection connection, int tableNumber, String reservationDate, String reservationTime) {
        String checkAvailabilityQuery = "SELECT 1 FROM Tables t " +
                "WHERE t.TableNo = ? AND t.TableStatus = 'available' " +
                "AND NOT EXISTS (" +
                "   SELECT 1 FROM Reservation r " +
                "   WHERE r.TableNo = t.TableNo " +
                "   AND r.ReservationDate = TO_DATE(?, 'YYYY-MM-DD') " +
                "   AND r.ReservationTime = TO_TIMESTAMP(?, 'HH24:MI')" +
                ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(checkAvailabilityQuery)) {

            preparedStatement.setInt(1, tableNumber);
            preparedStatement.setString(2, reservationDate);
            preparedStatement.setString(3, reservationTime);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); 
            }

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
  
    public static void cancelReservation(Connection connection) {
        // Display reserved tables
        //displayReservedTables(connection);

        System.out.println("Enter Reservation ID to cancel:");
        int reservationID = sc.nextInt();

        // SQL query to delete a reservation
        String cancelQuery = "UPDATE reservation SET Status = 'Cancelled' WHERE ReservationID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(cancelQuery)) {
            preparedStatement.setInt(1, reservationID);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reservation canceled successfully.");
                System.out.println("your payment will be refunded");

            } else {
                System.out.println("Failed to cancel the reservation. Reservation ID not found.");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public  static void displayReservedTables(Connection connection) {
        // SQL query to select reserved tables
        String selectReservedQuery = "SELECT ReservationID, TableNo FROM Reservation WHERE Status = 'reserved'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectReservedQuery)) {

            System.out.println("Reserved Tables:");

            while (resultSet.next()) {
                int reservationID = resultSet.getInt("ReservationID");
                int tableNo = resultSet.getInt("TableNo");
                System.out.println("Reservation ID: " + reservationID + ", Table No: " + tableNo);
            }

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static double getTablePrice(Connection connection, int tableNo) {
        double price = -1; // Default value in case the table number is not found

        String selectQuery = "SELECT price FROM Tables WHERE tableNo = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, tableNo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getDouble("price");
                } else {
                    System.out.println("Table number " + tableNo + " not found.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }

        return price;
    }

}
