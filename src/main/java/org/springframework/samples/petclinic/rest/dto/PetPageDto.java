package org.springframework.samples.petclinic.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * A page of pets.
 */
@JsonPropertyOrder({"content", "page", "size", "totalElements", "totalPages"})
public class PetPageDto {

    @NotNull
    @Valid
    private List<PetDto> content;

    @NotNull
    @Min(0)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer size;

    @NotNull
    @Min(0)
    private Long totalElements;

    @NotNull
    @Min(0)
    private Integer totalPages;

    public List<PetDto> getContent() {
        return content;
    }

    public void setContent(List<PetDto> content) {
        this.content = content;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
