package org.springframework.samples.petclinic.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A role.
 */
public class RoleDto {

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
