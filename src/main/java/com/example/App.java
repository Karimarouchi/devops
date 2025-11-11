package com.example;

import javax.servlet.http.HttpServletRequest;

public class App {
    public static void main(String[] args) {
        System.out.println("✅ Application DevSecOps Demo is running!");
    }

    public void insecureMethod(HttpServletRequest request) {
        // ❌ Mauvaise pratique : afficher un mot de passe dans les logs
        System.out.println("Password: " + request.getParameter("password"));
    }
}
