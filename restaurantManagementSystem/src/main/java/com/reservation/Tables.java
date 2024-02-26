package com.reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * Represents a table in the restaurant with functionalities 
 * like adding, deleting, and displaying tables.
 */
public class Tables {
    private int tableNo;
    private String tableStatus;

    public Tables(int tableNo, String tableStatus) {
        this.tableNo = tableNo;
        this.tableStatus = tableStatus;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }
    
    /**
     * Adds a new table to the database.
     *
     * @param connection The database connection.
     * @throws SQLException 
     */

    public static void addTable(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        
        
        
        String maxID = "SELECT MAX(ReservationID) FROM DATABASE.reservation";
        PreparedStatement maxIdStatement = connection.prepareStatement(maxID);
        ResultSet resultSet = maxIdStatement.executeQuery();
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
        int tableNo = maxId + 1;


        System.out.println("Enter Table Status:");
        String tableStatus = scanner.nextLine();
        
        System.out.println("Enter table Price :");
        double price = scanner.nextDouble();

        String insertQuery = "INSERT INTO Tables (tableNo, tableStatus, price) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, tableNo);
            preparedStatement.setString(2, tableStatus);
            preparedStatement.setDouble(3 , price);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Table added successfully.");
            } else {
                System.out.println("Failed to add the table.");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
          
        }
    }
    /**
     * Deletes a table from the database based on the specified table number.
     *
     * @param connection The database connection.
     */
    public static void deleteTable(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Table Number to delete:");
        int tableNoToDelete = scanner.nextInt();

        String deleteQuery = "DELETE FROM Tables WHERE tableNo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, tableNoToDelete);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Table deleted successfully.");
            } else {
                System.out.println("No table found with the specified Table Number.");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }
    /**
     * Displays all tables present in the database.
     *
     * @param connection The database connection.
     */

    public static void displayAllTables(Connection connection) {
        System.out.println("All Tables:");

        String selectQuery = "SELECT * FROM Tables";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
        	 System.out.println("************************************");
             System.out.println("*        Table Information          *");
             System.out.println("************************************");
             System.out.println("TableNo\t\tPrice\tTableStatus\n");
             
            while (resultSet.next()) {
            	
                int tableNo = resultSet.getInt("tableNo");
                String tableStatus = resultSet.getString("tableStatus");
                double price = resultSet.getDouble("price");

            	System.out.println(tableNo+"\t\t"+price+"\t"+tableStatus);
               /*
                System.out.println("* TableNo: " + tableNo);
                System.out.println("* TableStatus: " + tableStatus);
                System.out.println("* Price: " + price);
              */
            }
            System.out.println("************************************\n");            

        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
