package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * A booking for a vet visit.
 */

@Schema(name = "Visit", description = "A booking for a vet visit.")
public class VisitDto {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate date;

  private String description;

  private Integer id;

  private Integer petId;

  public VisitDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VisitDto(String description, Integer id, Integer petId) {
    this.description = description;
    this.id = id;
    this.petId = petId;
  }

  public VisitDto date(@Nullable LocalDate date) {
    this.date = date;
    return this;
  }

  /**
   * The date of the visit.
   * @return date
   */
  @Valid 
  @Schema(name = "date", example = "2013-01-01", description = "The date of the visit.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public @Nullable LocalDate getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(@Nullable LocalDate date) {
    this.date = date;
  }

  public VisitDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * The description for the visit.
   * @return description
   */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "description", example = "rabies shot", description = "The description for the visit.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  public VisitDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the visit.
   * minimum: 0
   * @return id
   */
  @Min(value = 0) 
  @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the visit.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  public VisitDto petId(Integer petId) {
    this.petId = petId;
    return this;
  }

  /**
   * The ID of the pet.
   * minimum: 0
   * @return petId
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "petId", example = "1", description = "The ID of the pet.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("petId")
  public Integer getPetId() {
    return petId;
  }

  @JsonProperty("petId")
  public void setPetId(Integer petId) {
    this.petId = petId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisitDto visit = (VisitDto) o;
    return Objects.equals(this.date, visit.date) &&
        Objects.equals(this.description, visit.description) &&
        Objects.equals(this.id, visit.id) &&
        Objects.equals(this.petId, visit.petId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, description, id, petId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VisitDto {\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    petId: ").append(toIndentedString(petId)).append("\n");
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

