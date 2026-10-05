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
 * Editable fields of a pet owner.
 */

@Schema(name = "OwnerFields", description = "Editable fields of a pet owner.")
public class OwnerFieldsDto {

  private String firstName;

  private String lastName;

  private String address;

  private String city;

  private String telephone;

  public OwnerFieldsDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OwnerFieldsDto(String firstName, String lastName, String address, String city, String telephone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.city = city;
    this.telephone = telephone;
  }

  public OwnerFieldsDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * The first name of the pet owner.
   * @return firstName
   */
  @NotNull @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}$") @Size(min = 1, max = 30) 
  @Schema(name = "firstName", example = "George", description = "The first name of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty("firstName")
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public OwnerFieldsDto lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * The last name of the pet owner.
   * @return lastName
   */
  @NotNull @Pattern(regexp = "^[\\p{L}]+([ '-][\\p{L}]+){0,2}\\.?$") @Size(min = 1, max = 30) 
  @Schema(name = "lastName", example = "Franklin", description = "The last name of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  @JsonProperty("lastName")
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public OwnerFieldsDto address(String address) {
    this.address = address;
    return this;
  }

  /**
   * The postal address of the pet owner.
   * @return address
   */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "address", example = "110 W. Liberty St.", description = "The postal address of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  @JsonProperty("address")
  public void setAddress(String address) {
    this.address = address;
  }

  public OwnerFieldsDto city(String city) {
    this.city = city;
    return this;
  }

  /**
   * The city of the pet owner.
   * @return city
   */
  @NotNull @Size(min = 1, max = 80) 
  @Schema(name = "city", example = "Madison", description = "The city of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  public OwnerFieldsDto telephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

  /**
   * The telephone number of the pet owner.
   * @return telephone
   */
  @NotNull @Pattern(regexp = "^[0-9]*$") @Size(min = 1, max = 20) 
  @Schema(name = "telephone", example = "6085551023", description = "The telephone number of the pet owner.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("telephone")
  public String getTelephone() {
    return telephone;
  }

  @JsonProperty("telephone")
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OwnerFieldsDto ownerFields = (OwnerFieldsDto) o;
    return Objects.equals(this.firstName, ownerFields.firstName) &&
        Objects.equals(this.lastName, ownerFields.lastName) &&
        Objects.equals(this.address, ownerFields.address) &&
        Objects.equals(this.city, ownerFields.city) &&
        Objects.equals(this.telephone, ownerFields.telephone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, address, city, telephone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OwnerFieldsDto {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    telephone: ").append(toIndentedString(telephone)).append("\n");
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

