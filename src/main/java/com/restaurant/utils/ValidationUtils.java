package com.restaurant.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        // Format sénégalais : 77|76|78|70 xxx xx xx
        String phoneRegex = "^7[0-8][0-9]{7}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone).matches();
    }
    
    public static boolean isValidPrice(double price) {
        return price > 0;
    }
    
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
}
