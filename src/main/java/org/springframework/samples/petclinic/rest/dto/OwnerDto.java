package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * A pet owner.
 */

@Schema(name = "Owner", description = "A pet owner.")
public class OwnerDto {

  private String firstName;

  private String lastName;

  private String address;

  private String city;

  private String telephone;

  private @Nullable Integer id;

  private List<PetDto> pets = new ArrayList<>();

  public OwnerDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OwnerDto(String firstName, String lastName, String address, String city, String telephone, List<PetDto> pets) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.city = city;
    this.telephone = telephone;
    this.pets = pets;
  }

  public OwnerDto firstName(String firstName) {
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

  public OwnerDto lastName(String lastName) {
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

  public OwnerDto address(String address) {
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

  public OwnerDto city(String city) {
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

  public OwnerDto telephone(String telephone) {
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

  public OwnerDto id(@Nullable Integer id) {
    this.id = id;
    return this;
  }

  /**
   * The ID of the pet owner.
   * minimum: 0
   * @return id
   */
  @Min(value = 0) 
  @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "The ID of the pet owner.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable Integer id) {
    this.id = id;
  }

  public OwnerDto pets(List<PetDto> pets) {
    this.pets = pets;
    return this;
  }

  public OwnerDto addPetsItem(PetDto petsItem) {
    if (this.pets == null) {
      this.pets = new ArrayList<>();
    }
    this.pets.add(petsItem);
    return this;
  }

  /**
   * The pets owned by this individual including any booked vet visits.
   * @return pets
   */
  @Valid 
  @Schema(name = "pets", accessMode = Schema.AccessMode.READ_ONLY, description = "The pets owned by this individual including any booked vet visits.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pets")
  public List<PetDto> getPets() {
    return pets;
  }

  @JsonProperty("pets")
  public void setPets(List<PetDto> pets) {
    this.pets = pets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OwnerDto owner = (OwnerDto) o;
    return Objects.equals(this.firstName, owner.firstName) &&
        Objects.equals(this.lastName, owner.lastName) &&
        Objects.equals(this.address, owner.address) &&
        Objects.equals(this.city, owner.city) &&
        Objects.equals(this.telephone, owner.telephone) &&
        Objects.equals(this.id, owner.id) &&
        Objects.equals(this.pets, owner.pets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, address, city, telephone, id, pets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OwnerDto {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    telephone: ").append(toIndentedString(telephone)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    pets: ").append(toIndentedString(pets)).append("\n");
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

