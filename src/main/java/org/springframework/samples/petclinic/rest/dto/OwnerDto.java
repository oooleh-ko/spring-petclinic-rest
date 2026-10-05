package org.springframework.samples.petclinic.rest.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * A pet owner.
 */
@JsonPropertyOrder({"firstName", "lastName", "address", "city", "telephone", "pets", "id"})
public class OwnerDto {

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

    @Valid
    private List<PetDto> pets = new ArrayList<>();

    @Min(0)
    private Integer id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public OwnerDto firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public OwnerDto lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OwnerDto address(String address) {
        this.address = address;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public OwnerDto city(String city) {
        this.city = city;
        return this;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public OwnerDto telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public List<PetDto> getPets() {
        return pets;
    }

    public void setPets(List<PetDto> pets) {
        this.pets = pets;
    }

    public OwnerDto addPetsItem(PetDto item) {
        if (this.pets == null) {
            this.pets = new ArrayList<>();
        }
        this.pets.add(item);
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OwnerDto id(Integer id) {
        this.id = id;
        return this;
    }
}
