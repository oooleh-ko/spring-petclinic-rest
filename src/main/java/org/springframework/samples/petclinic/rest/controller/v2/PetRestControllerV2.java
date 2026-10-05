package org.springframework.samples.petclinic.rest.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.dto.PetPageDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@Validated
@Tag(name = "pet-v2", description = "Version 2 endpoints related to pets.")
@RequestMapping("/api")
public class PetRestControllerV2 {

    private final ClinicService clinicService;
    private final PetMapper petMapper;

    public PetRestControllerV2(ClinicService clinicService, PetMapper petMapper) {
        this.clinicService = clinicService;
        this.petMapper = petMapper;
    }

    @GetMapping(value = "/v2/pets", produces = "application/json")
    @Operation(summary = "Lists pets with pagination.")
    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    public ResponseEntity<PetPageDto> listPetsPage(
        @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "20") @Min(1) @Max(100) Integer size) {
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 20 : size;
        Page<Pet> pets = this.clinicService.findPets(
            PageRequest.of(pageNumber, pageSize, Sort.by("id")));
        return new ResponseEntity<>(petMapper.toPetPageDto(pets), HttpStatus.OK);
    }

}
