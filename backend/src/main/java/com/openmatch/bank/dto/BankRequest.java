package com.openmatch.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating and updating banking entities.
 */
public class BankRequest {

    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 20)
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 200)
    private String name;

    @Size(max = 100)
    private String country;

    private boolean active = true;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
