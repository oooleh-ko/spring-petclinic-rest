/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.rest.controller.v1;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

/**
 * @author Vitaliy Fedoriv
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@Validated
@Tag(name = "specialties", description = "Endpoints related to vet specialties.")
@RequestMapping("/api")
public class SpecialtyRestControllerV1 {

    private final ClinicService clinicService;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyRestControllerV1(ClinicService clinicService, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.specialtyMapper = specialtyMapper;
    }

    @GetMapping(value = "/specialties", produces = "application/json")
    @Operation(summary = "Lists specialties")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    public ResponseEntity<List<SpecialtyDto>> listSpecialties() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.addAll(specialtyMapper.toSpecialtyDtos(this.clinicService.findAllSpecialties()));
        if (specialties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }

    @GetMapping(value = "/specialties/{specialtyId}", produces = "application/json")
    @Operation(summary = "Get a specialty by ID")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    public ResponseEntity<SpecialtyDto> getSpecialty(@PathVariable("specialtyId") @Min(0) Integer specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), HttpStatus.OK);
    }

    @PostMapping(value = "/specialties", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Create a specialty")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    public ResponseEntity<SpecialtyDto> addSpecialty(@Valid @RequestBody SpecialtyDto specialtyDto) {
        HttpHeaders headers = new HttpHeaders();
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        this.clinicService.saveSpecialty(specialty);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/specialties/{id}").buildAndExpand(specialty.getId()).toUri());
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/specialties/{specialtyId}", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Update a specialty by ID")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    public ResponseEntity<SpecialtyDto> updateSpecialty(
        @PathVariable("specialtyId") @Min(0) Integer specialtyId,
        @Valid @RequestBody SpecialtyDto specialtyDto) {
        Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
        if (currentSpecialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentSpecialty.setName(specialtyDto.getName());
        this.clinicService.saveSpecialty(currentSpecialty);
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(currentSpecialty), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/specialties/{specialtyId}", produces = "application/json")
    @Operation(summary = "Delete a specialty by ID")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    public ResponseEntity<SpecialtyDto> deleteSpecialty(@PathVariable("specialtyId") @Min(0) Integer specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteSpecialty(specialty);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
