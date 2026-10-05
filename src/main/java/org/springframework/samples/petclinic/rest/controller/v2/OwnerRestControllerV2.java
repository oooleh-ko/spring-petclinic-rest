package org.springframework.samples.petclinic.rest.controller.v2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.rest.dto.OwnerPageDto;
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
@Tag(name = "owner-v2", description = "Version 2 endpoints related to pet owners.")
@RequestMapping("/api")
public class OwnerRestControllerV2 {

    private final ClinicService clinicService;
    private final OwnerMapper ownerMapper;

    public OwnerRestControllerV2(ClinicService clinicService, OwnerMapper ownerMapper) {
        this.clinicService = clinicService;
        this.ownerMapper = ownerMapper;
    }

    @GetMapping(value = "/v2/owners", produces = "application/json")
    @Operation(summary = "Lists pet owners with pagination")
    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    public ResponseEntity<OwnerPageDto> listOwnersPage(
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "20") @Min(1) @Max(100) Integer size) {
        int pageNumber = page == null ? 0 : page;
        int pageSize = size == null ? 20 : size;
        Page<Owner> owners = this.clinicService.findOwners(
            lastName,
            PageRequest.of(pageNumber, pageSize, Sort.by("id")));
        return new ResponseEntity<>(ownerMapper.toOwnerPageDto(owners), HttpStatus.OK);
    }
}
