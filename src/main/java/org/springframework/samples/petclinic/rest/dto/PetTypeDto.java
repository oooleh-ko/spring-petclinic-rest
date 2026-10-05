package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A pet type.
 */
@JsonPropertyOrder({"name", "id"})
public class PetTypeDto {

    @NotNull
    @Size(min = 1, max = 80)
    private String name;

    @NotNull
    @Min(0)
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetTypeDto name(String name) {
        this.name = name;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PetTypeDto id(Integer id) {
        this.id = id;
        return this;
    }
}
