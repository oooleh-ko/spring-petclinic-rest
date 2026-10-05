package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Editable fields of a vet visit.
 */
public class VisitFieldsDto {

    private LocalDate date;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
