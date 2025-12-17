package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static <connection> void main(String[] args) {
            String url = "jdbc:postgresql://localhost:5432/mini_dish_db";
        String user = "postgres";
        String password = "sarobidy";

        try {
            connection connection = (connection) DriverManager.getConnection(url , user , password);
            System.out.println("connection réussie!");
        } catch (SQLException e) {
            System.err.println("erreur de connection : "+ e.getMessage());
        }
    }
}