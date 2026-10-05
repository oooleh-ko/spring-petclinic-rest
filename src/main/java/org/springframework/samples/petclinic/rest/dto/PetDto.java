package org.springframework.samples.petclinic.rest.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.samples.petclinic.rest.validation.PetAgeValidation;

/**
 * A pet.
 */
@JsonPropertyOrder({"name", "birthDate", "type", "id", "visits", "ownerId"})
public class PetDto {

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @PetAgeValidation
    private LocalDate birthDate;

    @NotNull
    @Valid
    private PetTypeDto type;

    @Min(0)
    private Integer id;

    @Valid
    private List<VisitDto> visits = new ArrayList<>();

    @Min(0)
    private Integer ownerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetDto name(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PetDto birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PetTypeDto getType() {
        return type;
    }

    public void setType(PetTypeDto type) {
        this.type = type;
    }

    public PetDto type(PetTypeDto type) {
        this.type = type;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PetDto id(Integer id) {
        this.id = id;
        return this;
    }

    public List<VisitDto> getVisits() {
        return visits;
    }

    public void setVisits(List<VisitDto> visits) {
        this.visits = visits;
    }

    public PetDto addVisitsItem(VisitDto item) {
        if (this.visits == null) {
            this.visits = new ArrayList<>();
        }
        this.visits.add(item);
        return this;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public PetDto ownerId(Integer ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
