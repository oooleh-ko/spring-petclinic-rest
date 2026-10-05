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
import org.springframework.samples.petclinic.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto;
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

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@Validated
@Tag(name = "pettypes", description = "Endpoints related to pet types.")
@RequestMapping("/api")
public class PetTypeRestControllerV1 {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;


    public PetTypeRestControllerV1(ClinicService clinicService, PetTypeMapper petTypeMapper) {
        this.clinicService = clinicService;
        this.petTypeMapper = petTypeMapper;
    }

    @GetMapping(value = "/pettypes", produces = "application/json")
    @Operation(summary = "Lists pet types")
    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    public ResponseEntity<List<PetTypeDto>> listPetTypes() {
        List<PetType> petTypes = new ArrayList<>(this.clinicService.findAllPetTypes());
        if (petTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDtos(petTypes), HttpStatus.OK);
    }

    @GetMapping(value = "/pettypes/{petTypeId}", produces = "application/json")
    @Operation(summary = "Get a pet type by ID")
    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    public ResponseEntity<PetTypeDto> getPetType(@PathVariable("petTypeId") @Min(0) Integer petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK);
    }

    @PostMapping(value = "/pettypes", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Create a pet type")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    public ResponseEntity<PetTypeDto> addPetType(@Valid @RequestBody PetTypeFieldsDto petTypeFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        final PetType type = petTypeMapper.toPetType(petTypeFieldsDto);
        this.clinicService.savePetType(type);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pettypes/{id}").buildAndExpand(type.getId()).toUri());
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/pettypes/{petTypeId}", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Update a pet type by ID")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    public ResponseEntity<PetTypeDto> updatePetType(
        @PathVariable("petTypeId") @Min(0) Integer petTypeId,
        @Valid @RequestBody PetTypeDto petTypeDto) {
        PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
        if (currentPetType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPetType.setName(petTypeDto.getName());
        this.clinicService.savePetType(currentPetType);
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(currentPetType), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/pettypes/{petTypeId}", produces = "application/json")
    @Operation(summary = "Delete a pet type by ID")
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    public ResponseEntity<PetTypeDto> deletePetType(@PathVariable("petTypeId") @Min(0) Integer petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deletePetType(petType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
