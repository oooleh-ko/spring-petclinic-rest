package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * A veterinarian.
 */

@Schema(name = "Vet", description = "A veterinarian.")
public class VetDto {

  private String firstName;

  private String lastName;

  private List<@Valid SpecialtyDto> specialties = new ArrayList<>();

  private Integer id;

  public VetDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VetDto(String firstName, String lastName, List<@Valid SpecialtyDto> specialties, Integer id) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.specialties = specialties;
    this.id = id;
  }

  public VetDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * The first name of the vet.
   * @return firstName
   */
  @NotNull @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}$") @Size(min = 1, max = 30) 
  @Schema(name = "firstName", example = "James", description = "The first name of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty("firstName")
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public VetDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * The last name of the vet.
   * @return lastName
   */
  @NotNull @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}\\.?$") @Size(min = 1, max = 30) 
  @Schema(name = "lastName", example = "Carter", description = "The last name of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  @JsonProperty("lastName")
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public VetDto specialties(List<@Valid SpecialtyDto> specialties) {
    this.specialties = specialties;
    return this;
  }

  public VetDto addSpecialtiesItem(SpecialtyDto specialtiesItem) {
    if (this.specialties == null) {
      this.specialties = new ArrayList<>();
    }
    this.specialties.add(specialtiesItem);
    return this;
  }

  /**
   * The specialties of the vet.
   * @return specialties
   */
  @NotNull @Valid 
  @Schema(name = "specialties", description = "The specialties of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("specialties")
  public List<@Valid SpecialtyDto> getSpecialties() {
    return specialties;
  }

  @JsonProperty("specialties")
  public void setSpecialties(List<@Valid SpecialtyDto> specialties) {
    this.specialties = specialties;
  }

  public VetDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the vet.
   * minimum: 0
   * @return id
   */
  @Min(value = 0) 
  @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the vet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VetDto vet = (VetDto) o;
    return Objects.equals(this.firstName, vet.firstName) &&
        Objects.equals(this.lastName, vet.lastName) &&
        Objects.equals(this.specialties, vet.specialties) &&
        Objects.equals(this.id, vet.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, specialties, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VetDto {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    specialties: ").append(toIndentedString(specialties)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

