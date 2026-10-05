package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * Editable fields of a pet.
 */

@Schema(name = "PetFields", description = "Editable fields of a pet.")
public class PetFieldsDto {

  private String name;

  @org.springframework.samples.petclinic.rest.validation.PetAgeValidation
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthDate;

  private PetTypeDto type;

  public PetFieldsDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PetFieldsDto(String name, LocalDate birthDate, PetTypeDto type) {
    this.name = name;
    this.birthDate = birthDate;
    this.type = type;
  }

  public PetFieldsDto name(String name) {
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

  public PetFieldsDto birthDate(LocalDate birthDate) {
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

  public PetFieldsDto type(PetTypeDto type) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PetFieldsDto petFields = (PetFieldsDto) o;
    return Objects.equals(this.name, petFields.name) &&
        Objects.equals(this.birthDate, petFields.birthDate) &&
        Objects.equals(this.type, petFields.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, birthDate, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PetFieldsDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

