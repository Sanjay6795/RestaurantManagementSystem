package com.payment;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bill {
	  public static void generateBill(Connection connection, int ReservationID ,int customerID, int tableNo,  double price) {
          Scanner sc = new Scanner(System.in);
		  try {
			  
			    String maxreserveID = "SELECT MAX(BillID) FROM DATABASE.Bill";
		        PreparedStatement maxIdStatement = connection.prepareStatement(maxreserveID);
		        ResultSet resultSet = maxIdStatement.executeQuery();
		        int maxId = 0;
		        if (resultSet.next()) {
		            maxId = resultSet.getInt(1);
		        }
		        int billID = maxId + 1;
			  
        	  
                String insertQuery = "INSERT INTO Bill (BillID, ReservationID, CustomerID, TableNo,  Amount) VALUES (?, ?, ?, ?, ?)";
              
              try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            	  preparedStatement.setInt(1, billID);
            	  preparedStatement.setInt(2, ReservationID);

            	  preparedStatement.setInt(3, customerID);
                  preparedStatement.setInt(4, tableNo);
                  
                  preparedStatement.setDouble(5, price);

                  int rowsAffected = preparedStatement.executeUpdate();

                  if (rowsAffected > 0) {
                     // System.out.println("Bill generated successfully.");
                      // Display the generated bill
                      displayBill(connection,billID);
                  } else {
                      System.out.println("Failed to generate the bill.");
                  }
              }
          } catch (SQLException e) {
              System.err.println("Database Error: " + e.getMessage());
              e.printStackTrace();
          }
      }

	  public static void displayBill(Connection connection,  int billID) {
		  //DriverPayment payment = new DriverPayment();
		  // Fetch the details of the generated bill
          String selectQuery = "SELECT * FROM Bill WHERE BillID = ?";
          try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
              selectStatement.setInt(1, billID);

              try (ResultSet resultSet = selectStatement.executeQuery()) {
                  if (resultSet.next()) {
                	  System.out.println("***********************************************");
                	  System.out.println("*                  Generated Bill             *");
                	  System.out.println("***********************************************");
                	  System.out.println("* BillID:       \t" + resultSet.getInt("BillID") + "                      *");
                	  System.out.println("* ReservationId:\t" + resultSet.getInt("ReservationID") + "                   *");

                	  System.out.println("* CustomerID:   \t" + resultSet.getInt("CustomerID") + "                     *");
                	  System.out.println("* TableNo:      \t" + resultSet.getInt("TableNo") + "                     *");
                	  System.out.println("* Amount:       \t" + resultSet.getDouble("Amount") + "                   *");
                	  System.out.println("***********************************************\n\n");

                  }
                  
                  Payment.paymentMethod(resultSet.getDouble("Amount"));
              }       
		            
		        
		    } catch (SQLException e) {
		        System.err.println("Database Error: " + e.getMessage());
		        e.printStackTrace();
		    }
		}   
}
