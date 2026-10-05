package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.jspecify.annotations.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * Fields of specialty of vets.
 */

@Schema(name = "Specialty", description = "Fields of specialty of vets.")
public class SpecialtyDto {

  private Integer id;

  private String name;

  public SpecialtyDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpecialtyDto(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public SpecialtyDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the specialty.
   * minimum: 0
   * @return id
   */
  @Min(value = 0) 
  @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the specialty.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  public SpecialtyDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the specialty.
   * @return name
   */
  @NotNull @Size(min = 1, max = 80) 
  @Schema(name = "name", example = "radiology", description = "The name of the specialty.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialtyDto specialty = (SpecialtyDto) o;
    return Objects.equals(this.id, specialty.id) &&
        Objects.equals(this.name, specialty.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpecialtyDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

