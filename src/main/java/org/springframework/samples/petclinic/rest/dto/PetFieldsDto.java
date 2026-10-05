package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.samples.petclinic.rest.validation.PetAgeValidation;

/**
 * Editable fields of a pet.
 */
public class PetFieldsDto {

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @PetAgeValidation
    private LocalDate birthDate;

    @NotNull
    @Valid
    private PetTypeDto type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PetTypeDto getType() {
        return type;
    }

    public void setType(PetTypeDto type) {
        this.type = type;
    }
}
