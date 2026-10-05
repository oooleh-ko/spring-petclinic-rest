package org.springframework.samples.petclinic.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Editable fields of a pet type.
 */
public class PetTypeFieldsDto {

    @NotNull
    @Size(min = 1, max = 80)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
