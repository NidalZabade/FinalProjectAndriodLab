package edu.birzeit.nidlibraheem.finalproject.utils;

import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHash {

    static int iterations = 1000; // You can adjust the number of iterations for security
    static int keyLength = 256; // Adjust the key length as needed


    public static String hashPassword(String password) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] salt = {0}; // Replace with secure random salt

            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            // Convert the derived key to a hexadecimal string
            byte[] hashBytes = secretKey.getEncoded();
            StringBuilder hexHash = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
    }

    public static boolean verifyPassword(String providedPassword, String storedPassword) {
        try {
            // Convert the stored password (hex string) back to bytes
            byte[] storedPasswordBytes = hexStringToByteArray(storedPassword);
            byte[] salt = {0}; // Replace with stored salt

            // Create a new PBEKeySpec with the provided password, salt, iterations, and key length
            KeySpec keySpec = new PBEKeySpec(providedPassword.toCharArray(), salt, iterations, keyLength);

            // Generate a secret key from the new key spec
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            // Generate the new hash for the provided password
            byte[] newHashBytes = secretKey.getEncoded();

            // Compare the new hash with the stored password hash
            return slowEquals(storedPasswordBytes, newHashBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle error appropriately
        }
    }

    // Utility method to compare two byte arrays in a constant time manner
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    // Utility method to convert a hexadecimal string to a byte array
    private static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
