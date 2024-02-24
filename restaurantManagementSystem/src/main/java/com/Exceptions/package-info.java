package com.Exceptions;

/*
// Database connection details
final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
final String userName = "DATABASE";
final String Password = "Sanjay@143s";

try (
    Connection connection = DriverManager.getConnection(URL, userName, Password);
    PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO Persons (PersonID, FirstName, LastName, Gender, DOB, Email, Phone) VALUES (?, ?, ?, ?, ?, ?, ?)"
    );
) {
    preparedStatement.setInt(1, personId);
    preparedStatement.setString(2, firstName);
    preparedStatement.setString(3, lastName);
    preparedStatement.setString(4, gender);
    preparedStatement.setString(5, dob);
    preparedStatement.setString(6, email);
    preparedStatement.setString(7, contactNumber);

    int affectedRows = preparedStatement.executeUpdate();

    if (affectedRows > 0) {
        System.out.println("User registration successful!");
    } else {
        System.err.println("User registration failed.");
    }

} catch (SQLException e) {
    System.err.println("Error during user registration. Please try again.");
    e.printStackTrace();
}
}
*/