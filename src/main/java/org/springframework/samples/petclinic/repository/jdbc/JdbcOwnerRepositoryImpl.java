/*
 * Copyright 2002-2017 the original author or authors.
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
package org.springframework.samples.petclinic.repository.jdbc;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Repository;

/**
 * A simple JDBC-based implementation of the {@link OwnerRepository} interface.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Thomas Risberg
 * @author Mark Fisher
 * @author Antoine Rey
 * @author Vitaliy Fedoriv
 */
@DependsOnDatabaseInitialization
@Repository
public class JdbcOwnerRepositoryImpl implements OwnerRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Owner> ownerRowMapper = (rs, rowNum) -> {
        Owner owner = new Owner();
        owner.setId(rs.getInt("id"));
        owner.setFirstName(rs.getString("first_name"));
        owner.setLastName(rs.getString("last_name"));
        owner.setAddress(rs.getString("address"));
        owner.setCity(rs.getString("city"));
        owner.setTelephone(rs.getString("telephone"));
        return owner;
    };

    public JdbcOwnerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Loads {@link Owner Owners} whose last name <i>starts</i> with the given name,
     * together with their {@link Pet Pets} and {@link Visit Visits}.
     */
    @Override
    public Collection<Owner> findByLastName(String lastName) {
        List<Owner> owners = jdbcTemplate.query(
            "SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE last_name LIKE ?",
            ownerRowMapper,
            lastName + "%");
        loadPetsAndVisits(owners);
        return owners;
    }

    @Override
    public Page<Owner> findByLastName(String lastName, Pageable pageable) {
        List<Owner> owners = jdbcTemplate.query(
            "SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE last_name LIKE ? " +
                "ORDER BY id LIMIT ? OFFSET ?",
            ownerRowMapper,
            lastName + "%", pageable.getPageSize(), pageable.getOffset());
        loadPetsAndVisits(owners);
        Long total = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM owners WHERE last_name LIKE ?", Long.class, lastName + "%");
        return new PageImpl<>(owners, pageable, total);
    }

    /**
     * Loads the {@link Owner} with the given id, together with its {@link Pet Pets} and {@link Visit Visits}.
     */
    @Override
    public Owner findById(int id) {
        Owner owner = jdbcTemplate.query(
                "SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE id = ?",
                ownerRowMapper,
                id)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ObjectRetrievalFailureException(Owner.class, id));
        loadPetsAndVisits(owner);
        return owner;
    }

    @Override
    public Collection<Owner> findAll() {
        List<Owner> owners = jdbcTemplate.query(
            "SELECT id, first_name, last_name, address, city, telephone FROM owners", ownerRowMapper);
        loadPetsAndVisits(owners);
        return owners;
    }

    @Override
    public Page<Owner> findAll(Pageable pageable) {
        List<Owner> owners = jdbcTemplate.query(
            "SELECT id, first_name, last_name, address, city, telephone FROM owners ORDER BY id LIMIT ? OFFSET ?",
            ownerRowMapper,
            pageable.getPageSize(), pageable.getOffset());
        loadPetsAndVisits(owners);
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owners", Long.class);
        return new PageImpl<>(owners, pageable, total);
    }

    private void loadPetsAndVisits(List<Owner> owners) {
        for (Owner owner : owners) {
            loadPetsAndVisits(owner);
        }
    }

    /**
     * Loads the owner's pets with their types and visits in one query. A pet with several visits
     * comes back as several rows, so pets are collected in a map by id and each row adds its visit.
     */
    private void loadPetsAndVisits(Owner owner) {
        Map<Integer, Pet> petsById = new LinkedHashMap<>();
        RowCallbackHandler addRow = rs -> {
            int petId = rs.getInt("pet_id");
            Pet pet = petsById.get(petId);
            if (pet == null) {
                PetType type = new PetType();
                type.setId(rs.getInt("type_id"));
                type.setName(rs.getString("type_name"));

                pet = new Pet();
                pet.setId(petId);
                pet.setName(rs.getString("pet_name"));
                pet.setBirthDate(rs.getObject("birth_date", LocalDate.class));
                pet.setType(type);
                petsById.put(petId, pet);
            }
            // LEFT JOIN: visit columns are NULL for a pet without visits
            if (rs.getObject("visit_id") != null) {
                Visit visit = new Visit();
                visit.setId(rs.getInt("visit_id"));
                visit.setDate(rs.getObject("visit_date", LocalDate.class));
                visit.setDescription(rs.getString("description"));
                pet.addVisit(visit);
            }
        };
        jdbcTemplate.query(
            "SELECT p.id AS pet_id, p.name AS pet_name, p.birth_date, " +
                "t.id AS type_id, t.name AS type_name, " +
                "v.id AS visit_id, v.visit_date, v.description " +
                "FROM pets p " +
                "JOIN types t ON p.type_id = t.id " +
                "LEFT JOIN visits v ON v.pet_id = p.id " +
                "WHERE p.owner_id = ? ORDER BY p.id",
            addRow,
            owner.getId());
        for (Pet pet : petsById.values()) {
            owner.addPet(pet);
        }
    }

    @Override
    public void save(Owner owner) {
        if (owner.isNew()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?)",
                    new String[]{"id"});
                ps.setString(1, owner.getFirstName());
                ps.setString(2, owner.getLastName());
                ps.setString(3, owner.getAddress());
                ps.setString(4, owner.getCity());
                ps.setString(5, owner.getTelephone());
                return ps;
            }, keyHolder);
            owner.setId(keyHolder.getKey().intValue());
        } else {
            jdbcTemplate.update(
                "UPDATE owners SET first_name = ?, last_name = ?, address = ?, city = ?, telephone = ? WHERE id = ?",
                owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(),
                owner.getTelephone(), owner.getId());
        }
    }

    @Override
    public void delete(Owner owner) {
        // the owner's pets and their visits are removed by ON DELETE CASCADE
        jdbcTemplate.update("DELETE FROM owners WHERE id = ?", owner.getId());
    }

}
