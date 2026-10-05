package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A booking for a vet visit.
 */
@JsonPropertyOrder({"description", "id", "petId", "date"})
public class VisitDto {

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @Min(0)
    private Integer id;

    @NotNull
    @Min(0)
    private Integer petId;

    private LocalDate date;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VisitDto description(String description) {
        this.description = description;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VisitDto id(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public VisitDto petId(Integer petId) {
        this.petId = petId;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public VisitDto date(LocalDate date) {
        this.date = date;
        return this;
    }
}
