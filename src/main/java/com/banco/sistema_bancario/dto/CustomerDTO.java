package com.banco.sistema_bancario.dto;

/**
 * Data Transfer Object for Customer information
 */
public class CustomerDTO {
    private String name;
    private String email;
    private String birthday; // Format: YYYYMMDD

    // Default constructor
    public CustomerDTO() {
    }

    // Constructor with all fields
    public CustomerDTO(String name, String email, String birthday) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}