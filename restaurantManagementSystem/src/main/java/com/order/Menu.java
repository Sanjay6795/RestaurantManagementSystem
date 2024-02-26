package com.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Menu {
    private int itemID;
    private String itemName;
    private String description;
    private double price;
    private String category;
    private String availabilityStatus;
    private int adminID;

   

    public Menu(int itemID, String itemName, String description, double price, String category,
                String availabilityStatus, int adminID) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.availabilityStatus = availabilityStatus;
        this.adminID = adminID;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    
    public int getAdminID() {
        return adminID;
    } 
    
    public  static void addItem(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        try {

        System.out.println("Enter Item Name:");
        String itemName = scanner.nextLine();
        System.out.println("Enter Description:");
        String description = scanner.nextLine();
        System.out.println("Enter Price:");
        double price = scanner.nextDouble();
        scanner.nextLine(); 
        System.out.println("Enter Category:");
        String category = scanner.nextLine();
        System.out.println("Enter Availability Status:");
        String availabilityStatus = scanner.nextLine();
        System.out.println("Enter Admin ID:");
        int adminID = scanner.nextInt();
        
        
        String maxID = "SELECT MAX(ItemID) FROM DATABASE.Menu";
        PreparedStatement maxIdStatement = connection.prepareStatement(maxID);
        ResultSet resultSet = maxIdStatement.executeQuery();
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
        int itemID = maxId + 1;
        
        
        
        String insertQuery = "INSERT INTO Menu (itemID, itemName, description, price, category, " +
                "availabilityStatus, adminID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, itemID);
            preparedStatement.setString(2, itemName);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.setString(5, category);
            preparedStatement.setString(6, availabilityStatus);
            preparedStatement.setInt(7, adminID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item added to the menu successfully.");
            } else {
                System.out.println("Failed to add the item to the menu.");
            }
        } 
        }catch (SQLException  e) {
            System.err.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
  
    public static void deleteItem(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Item ID to delete:");
        int itemIDToDelete = scanner.nextInt();

        String deleteQuery = "DELETE FROM Menu WHERE itemID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, itemIDToDelete);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item deleted from the menu successfully.");
            } else {
                System.out.println("No item found with the specified Item ID.");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            //e.printStackTrace();
        }
    }
    
   

        public  static void displayAvailableItems(Connection connection) {
            System.out.println("Available Menu Items:");

            String selectQuery = "SELECT * FROM Menu WHERE availabilityStatus = 'Available' OR availabilityStatus = 'available'";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("***************************************************************************");
                System.out.println("*                               Menu Information                          *");
                System.out.println("***************************************************************************");
                System.out.println("ItemID\tItemName\t\tPrice\tDescription");
                System.out.println("***************************************************************************");

                while (resultSet.next()) {
                    int itemID = resultSet.getInt("itemID");
                    String itemName = resultSet.getString("itemName");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");

                    System.out.println(itemID+"\t"+itemName+"\t\t"+price+"\t"+description);
                    
                }
                System.out.println("***************************************************************************");


            } catch (SQLException | InputMismatchException e) {
                System.err.println("Database Error: " + e.getMessage());
                //e.printStackTrace();
            }
        }
        
               
}


