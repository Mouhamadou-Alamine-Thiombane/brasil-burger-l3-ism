package com.restaurant.utils;

import java.util.Scanner;

public class ConsoleUtils {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void pause() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
    
    public static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }
    
    public static void printSuccess(String message) {
        System.out.println("✅ " + message);
    }
    
    public static void printError(String message) {
        System.out.println("❌ " + message);
    }
    
    public static void printInfo(String message) {
        System.out.println("ℹ️  " + message);
    }
}
