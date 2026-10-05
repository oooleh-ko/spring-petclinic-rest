package org.springframework.samples.petclinic.rest.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

/**
 * A page of pet owners.
 */

@Schema(name = "OwnerPage", description = "A page of pet owners.")
public class OwnerPageDto {

  private List<OwnerDto> content = new ArrayList<>();

  private Integer page;

  private Integer size;

  private Long totalElements;

  private Integer totalPages;

  public OwnerPageDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OwnerPageDto(List<OwnerDto> content, Integer page, Integer size, Long totalElements, Integer totalPages) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
  }

  public OwnerPageDto content(List<OwnerDto> content) {
    this.content = content;
    return this;
  }

  public OwnerPageDto addContentItem(OwnerDto contentItem) {
    if (this.content == null) {
      this.content = new ArrayList<>();
    }
    this.content.add(contentItem);
    return this;
  }

  /**
   * Pet owners in the requested page.
   * @return content
   */
  @NotNull @Valid 
  @Schema(name = "content", description = "Pet owners in the requested page.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("content")
  public List<OwnerDto> getContent() {
    return content;
  }

  @JsonProperty("content")
  public void setContent(List<OwnerDto> content) {
    this.content = content;
  }

  public OwnerPageDto page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Zero-based page index.
   * minimum: 0
   * @return page
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "page", example = "0", description = "Zero-based page index.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  @JsonProperty("page")
  public void setPage(Integer page) {
    this.page = page;
  }

  public OwnerPageDto size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Requested page size.
   * minimum: 1
   * @return size
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "size", example = "5", description = "Requested page size.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  @JsonProperty("size")
  public void setSize(Integer size) {
    this.size = size;
  }

  public OwnerPageDto totalElements(Long totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * Total number of owners matching the request.
   * minimum: 0
   * @return totalElements
   */
  @NotNull @Min(value = 0L) 
  @Schema(name = "totalElements", example = "10", description = "Total number of owners matching the request.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalElements")
  public Long getTotalElements() {
    return totalElements;
  }

  @JsonProperty("totalElements")
  public void setTotalElements(Long totalElements) {
    this.totalElements = totalElements;
  }

  public OwnerPageDto totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Total number of pages matching the request.
   * minimum: 0
   * @return totalPages
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "totalPages", example = "2", description = "Total number of pages matching the request.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  @JsonProperty("totalPages")
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OwnerPageDto ownerPage = (OwnerPageDto) o;
    return Objects.equals(this.content, ownerPage.content) &&
        Objects.equals(this.page, ownerPage.page) &&
        Objects.equals(this.size, ownerPage.size) &&
        Objects.equals(this.totalElements, ownerPage.totalElements) &&
        Objects.equals(this.totalPages, ownerPage.totalPages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, page, size, totalElements, totalPages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OwnerPageDto {\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
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

