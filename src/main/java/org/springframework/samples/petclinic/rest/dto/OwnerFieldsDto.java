package org.springframework.samples.petclinic.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Editable fields of a pet owner.
 */
public class OwnerFieldsDto {

    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}$")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}\\.?$")
    private String lastName;

    @NotNull
    @Size(min = 1, max = 255)
    private String address;

    @NotNull
    @Size(min = 1, max = 80)
    private String city;

    @NotNull
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[0-9]*$")
    private String telephone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
