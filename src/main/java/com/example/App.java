package com.example;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;

public class App {
    public static void main(String[] args) {
        System.out.println("✅ Applicationnnn DevSecOps Demo is running!");
    }


    public void insecureLog(HttpServletRequest request) {
        // Semgrep doit détecter l'affichage du password
        System.out.println("Password: " + request.getParameter("password"));
    }


   public void getUser(HttpServletRequest request, Connection conn) throws SQLException {
        String user = request.getParameter("username");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user + "'");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
