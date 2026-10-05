package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * A pet.
 */

@Schema(name = "Pet", description = "A pet.")
public class PetDto {

  private String name;

  @org.springframework.samples.petclinic.rest.validation.PetAgeValidation
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthDate;

  private PetTypeDto type;

  private Integer id;

  private @Nullable Integer ownerId;

  private List<VisitDto> visits = new ArrayList<>();

  public PetDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PetDto(String name, LocalDate birthDate, PetTypeDto type, Integer id, List<VisitDto> visits) {
    this.name = name;
    this.birthDate = birthDate;
    this.type = type;
    this.id = id;
    this.visits = visits;
  }

  public PetDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the pet.
   * @return name
   */
  @NotNull @Size(max = 30) 
  @Schema(name = "name", example = "Leo", description = "The name of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public PetDto birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  /**
   * The date of birth of the pet.
   * @return birthDate
   */
  @NotNull @Valid 
  @Schema(name = "birthDate", example = "2010-09-07", description = "The date of birth of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("birthDate")
  public LocalDate getBirthDate() {
    return birthDate;
  }

  @JsonProperty("birthDate")
  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public PetDto type(PetTypeDto type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull @Valid 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public PetTypeDto getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(PetTypeDto type) {
    this.type = type;
  }

  public PetDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the pet.
   * minimum: 0
   * @return id
   */
  @Min(value = 0) 
  @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  public PetDto ownerId(@Nullable Integer ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * The ID of the pet's owner.
   * minimum: 0
   * @return ownerId
   */
  @Min(value = 0) 
  @Schema(name = "ownerId", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the pet's owner.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public @Nullable Integer getOwnerId() {
    return ownerId;
  }

  @JsonProperty("ownerId")
  public void setOwnerId(@Nullable Integer ownerId) {
    this.ownerId = ownerId;
  }

  public PetDto visits(List<VisitDto> visits) {
    this.visits = visits;
    return this;
  }

  public PetDto addVisitsItem(VisitDto visitsItem) {
    if (this.visits == null) {
      this.visits = new ArrayList<>();
    }
    this.visits.add(visitsItem);
    return this;
  }

  /**
   * Vet visit bookings for this pet.
   * @return visits
   */
  @Valid 
  @Schema(name = "visits", accessMode = Schema.AccessMode.READ_ONLY, description = "Vet visit bookings for this pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("visits")
  public List<VisitDto> getVisits() {
    return visits;
  }

  @JsonProperty("visits")
  public void setVisits(List<VisitDto> visits) {
    this.visits = visits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PetDto pet = (PetDto) o;
    return Objects.equals(this.name, pet.name) &&
        Objects.equals(this.birthDate, pet.birthDate) &&
        Objects.equals(this.type, pet.type) &&
        Objects.equals(this.id, pet.id) &&
        Objects.equals(this.ownerId, pet.ownerId) &&
        Objects.equals(this.visits, pet.visits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, birthDate, type, id, ownerId, visits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PetDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    visits: ").append(toIndentedString(visits)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

