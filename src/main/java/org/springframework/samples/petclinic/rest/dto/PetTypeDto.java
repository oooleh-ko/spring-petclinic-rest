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
 * A pet type.
 */

@Schema(name = "PetType", description = "A pet type.")
public class PetTypeDto {

  private String name;

  private Integer id;

  public PetTypeDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PetTypeDto(String name, Integer id) {
    this.name = name;
    this.id = id;
  }

  public PetTypeDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the pet type.
   * @return name
   */
  @NotNull @Size(min = 1, max = 80) 
  @Schema(name = "name", example = "cat", description = "The name of the pet type.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public PetTypeDto id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the pet type.
   * minimum: 0
   * @return id
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "id", example = "1", description = "The ID of the pet type.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    PetTypeDto petType = (PetTypeDto) o;
    return Objects.equals(this.name, petType.name) &&
        Objects.equals(this.id, petType.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PetTypeDto {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

