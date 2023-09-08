package edu.birzeit.nidlibraheem.finalproject.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public User(String email, String firstName, String lastName, String password, String confirmPassword) throws IllegalArgumentException {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;

    }

    public User(long id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static void validateEmail(String email) throws IllegalArgumentException {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email address format");
        }
    }

    public static void validateName(String name, String fieldName) throws IllegalArgumentException {
        if (name.length() < 3 || name.length() > 10) {
            throw new IllegalArgumentException(fieldName + " must be between 3 and 10 characters");
        }
    }

    public static void validatePassword(String password, String confirmPassword) throws IllegalArgumentException {
        if (password.length() < 6 || password.length() > 12) {
            throw new IllegalArgumentException("Password must be between 6 and 12 characters");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }
}
