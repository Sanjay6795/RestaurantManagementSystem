package com.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.payment.*;
public class MakeOrder {
	
	
	public static void getOrder(Connection connection) throws SQLException {
		Scanner sc = new Scanner(System.in);
		ArrayList<OrderItem> orderDetails = new ArrayList<>();
        List<Menu> menuItems = fetchMenuFromDatabase(connection);

		while (true) {
		
			System.out.println("Enter the item ID :");
			int orderItemID = sc.nextInt();

			if (orderItemID <= 0) {
				break; 
			}
       
            System.out.println("Enter the quantity for item " + orderItemID + ":");
            int quantity = sc.nextInt();
            System.out.println("Enter customerID :");
            int customerID = sc.nextInt();
            sc.nextLine();
            OrderItem orderItem = new OrderItem(customerID,orderItemID, quantity);

            orderDetails.add(orderItem);
          
            System.out.println("Enter negative values to finish order");
            
        }
		
		
    
		if(!orderDetails.isEmpty()) {
			System.out.println("Order has been placed please process the payment");
			double totalAmount = calculateTotalPrice(menuItems,orderDetails);
			insertOrderItems(connection ,orderDetails );
		
			System.out.println("Order Details:\n");
	        for (OrderItem orderItem : orderDetails) {
	            System.out.println(orderItem);
	        }
			
			System.out.println("\nTotal Amount : " + totalAmount);

			Payment.paymentMethod(totalAmount);
			
		}
	}

	public static void insertOrderItems(Connection connection, List<OrderItem> orderItems) {
        String insertOrderItemQuery = "INSERT INTO OrderItem (CustomerID, ItemID, Quantity) VALUES ( ?, ?, ?)";
        
        try {
            for (OrderItem orderItem : orderItems) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderItemQuery)) {
                    preparedStatement.setInt(1, orderItem.getCustomerID());
                    preparedStatement.setInt(2, orderItem.getItemID());
                    preparedStatement.setInt(3, orderItem.getQuantity());
                    
                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows <= 0) {
                        System.out.println("Error inserting order item into Order_Item table. No rows affected.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting order items into Order_Item table: " + e.getMessage());
        }
    }
	
	
	
	public static List<Menu> fetchMenuFromDatabase(Connection connection) {
        List<Menu> menuItems = new ArrayList<>();

        try {
            String query = "SELECT ItemID, ItemName, Description, Price, Category, AvailabilityStatus, AdminID FROM Menu";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int itemId = resultSet.getInt("ItemID");
                    String itemName = resultSet.getString("ItemName");
                    double price = resultSet.getDouble("Price");
                    String category = resultSet.getString("Category");
                    String availability = resultSet.getString("AvailabilityStatus");
                    String description = resultSet.getString("Description");
                    int adminId = resultSet.getInt("AdminID");

                    
                    // Add fetched menu item to the ArrayList
                    menuItems.add(new Menu(itemId, itemName,description, price, category, availability, adminId));
                }
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return menuItems;
    }
	

	private static double calculateTotalPrice(List<Menu> menuItems, List<OrderItem> orderDetails) {
	    double totalAmount = 0.0;

	    for (OrderItem orderItem : orderDetails) {
	        int orderItemID = orderItem.getItemID();
	        int quantity = orderItem.getQuantity();

	        // Find the corresponding menu item based on orderItemID
	        Menu menuItem = null;
	        for (Menu item : menuItems) {
	            if (item.getItemID() == orderItemID) {
	                menuItem = item;
	                break;
	            }
	        }

	        if (menuItem != null) {
	            double itemPrice = menuItem.getPrice();
	            totalAmount += itemPrice * quantity;
	        }
	    }

	    return totalAmount;
	}


}
