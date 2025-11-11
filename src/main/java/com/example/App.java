package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Classe factice pour simuler un HttpServletRequest
class FakeRequest {
    public String getParameter(String name) {
        if ("username".equals(name)) return "admin";
        if ("password".equals(name)) return "P@ssw0rd"; // mot de passe codé en dur
        return "";
    }
}
String secret = "API_KEY=TEST123456789";
public class App {
    public static void main(String[] args) {
        System.out.println("✅ Application DevSecOps Demo is running!");
        App app = new App();
        FakeRequest req = new FakeRequest();
        app.insecureLog(req);
    }

    // 🔥 Vulnérabilité 1 : log d’un mot de passe
    public void insecureLog(FakeRequest request) {
        System.out.println("Password: " + request.getParameter("password"));
    }

    // 🔥 Vulnérabilité 2 : injection SQL par concaténation
    public void getUser(FakeRequest request, Connection conn) throws SQLException {
        String user = request.getParameter("username");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + user + "'");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
