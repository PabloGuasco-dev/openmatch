package com.openmatch.bank.dto;

import java.time.Instant;

/**
 * Response DTO for banking entity.
 */
public class BankResponse {

    private Long id;
    private String code;
    private String name;
    private String country;
    private boolean active;
    private Instant creationDate;

    public BankResponse() {
    }

    public BankResponse(Long id, String code, String name, String country, boolean active, Instant creationDate) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.country = country;
        this.active = active;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}
