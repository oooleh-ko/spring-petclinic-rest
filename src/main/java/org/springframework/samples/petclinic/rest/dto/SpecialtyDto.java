package org.springframework.samples.petclinic.rest.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A specialty of vets.
 */
@JsonPropertyOrder({"id", "name"})
public class SpecialtyDto {

    @Min(0)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 80)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
