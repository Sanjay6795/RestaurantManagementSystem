package com.Interface;

import java.sql.Connection;

public interface Searchable {
    void searchByCategory(Connection connection, String category);
    
}