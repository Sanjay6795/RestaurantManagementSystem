package com.Interface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchBy implements Searchable{
	 /**
     * Searches for items in the menu based on the specified category.
     *
     * @param connection     The database connection.
     * @param searchCategory The category to search for.
     */
    
    public void  searchByCategory(Connection connection , String searchCategory ) {
        System.out.println("Search Results for Category: " + searchCategory);

        // SQL query to select items from the 'Menu' table based on category
        String selectQuery = "SELECT * FROM Menu WHERE category = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            // Set parameter in the prepared statement
            preparedStatement.setString(1, searchCategory.toLowerCase()); // Convert to lowercase for case-insensitive search

            // Execute the query
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	System.out.println("***************************************************************");
            	System.out.println("ItemID\tName\t\tPrice\tDescription");
            	System.out.println("***************************************************************\n");

                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String itemName = resultSet.getString("itemName");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    String category = resultSet.getString("category");
				
                    System.out.println(itemID+"\t"+itemName+"\t"+price+"\t"+description);
                } 
                System.out.println("***************************************************************\n");

            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

}
