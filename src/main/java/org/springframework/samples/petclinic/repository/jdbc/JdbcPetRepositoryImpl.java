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
import java.util.List;

import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;

/**
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Thomas Risberg
 * @author Mark Fisher
 * @author Vitaliy Fedoriv
 */
@DependsOnDatabaseInitialization
@Repository
public class JdbcPetRepositoryImpl implements PetRepository {

    /**
     * Each pet is selected together with its type and owner.
     */
    private static final String SELECT_PETS =
        "SELECT p.id, p.name, p.birth_date, " +
            "t.id AS type_id, t.name AS type_name, " +
            "o.id AS owner_id, o.first_name, o.last_name, o.address, o.city, o.telephone " +
            "FROM pets p " +
            "JOIN types t ON p.type_id = t.id " +
            "JOIN owners o ON p.owner_id = o.id ";

    private final JdbcTemplate jdbcTemplate;

    private final OwnerRepository ownerRepository;

    private final RowMapper<PetType> petTypeRowMapper = (rs, rowNum) -> {
        PetType petType = new PetType();
        petType.setId(rs.getInt("id"));
        petType.setName(rs.getString("name"));
        return petType;
    };

    private final RowMapper<Pet> petRowMapper = (rs, rowNum) -> {
        PetType type = new PetType();
        type.setId(rs.getInt("type_id"));
        type.setName(rs.getString("type_name"));

        Owner owner = new Owner();
        owner.setId(rs.getInt("owner_id"));
        owner.setFirstName(rs.getString("first_name"));
        owner.setLastName(rs.getString("last_name"));
        owner.setAddress(rs.getString("address"));
        owner.setCity(rs.getString("city"));
        owner.setTelephone(rs.getString("telephone"));

        Pet pet = new Pet();
        pet.setId(rs.getInt("id"));
        pet.setName(rs.getString("name"));
        pet.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        pet.setType(type);
        pet.setOwner(owner);
        return pet;
    };

    public JdbcPetRepositoryImpl(JdbcTemplate jdbcTemplate, OwnerRepository ownerRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<PetType> findPetTypes() {
        return jdbcTemplate.query("SELECT id, name FROM types ORDER BY name", petTypeRowMapper);
    }

    /**
     * Loads the pet through its owner, so the returned pet comes with its visits
     * and an owner that has all of its pets.
     */
    @Override
    public Pet findById(int id) {
        Integer ownerId = jdbcTemplate.query("SELECT owner_id FROM pets WHERE id = ?",
                (rs, rowNum) -> rs.getInt("owner_id"), id)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ObjectRetrievalFailureException(Pet.class, id));
        Owner owner = ownerRepository.findById(ownerId);
        return EntityUtils.getById(owner.getPets(), Pet.class, id);
    }

    @Override
    public void save(Pet pet) {
        if (pet.isNew()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES (?, ?, ?, ?)", new String[]{"id"});
                ps.setString(1, pet.getName());
                ps.setObject(2, pet.getBirthDate());
                ps.setInt(3, pet.getType().getId());
                ps.setInt(4, pet.getOwner().getId());
                return ps;
            }, keyHolder);
            pet.setId(keyHolder.getKey().intValue());
        } else {
            jdbcTemplate.update("UPDATE pets SET name = ?, birth_date = ?, type_id = ?, owner_id = ? WHERE id = ?",
                pet.getName(), pet.getBirthDate(), pet.getType().getId(), pet.getOwner().getId(), pet.getId());
        }
    }

    @Override
    public Collection<Pet> findAll() {
        return jdbcTemplate.query(SELECT_PETS + "ORDER BY p.id", petRowMapper);
    }

    @Override
    public Page<Pet> findAll(Pageable pageable) {
        List<Pet> pets = jdbcTemplate.query(SELECT_PETS + "ORDER BY p.id LIMIT ? OFFSET ?",
            petRowMapper, pageable.getPageSize(), pageable.getOffset());
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM pets", Long.class);
        return new PageImpl<>(pets, pageable, total);
    }

    @Override
    public void delete(Pet pet) {
        // the pet's visits are removed by ON DELETE CASCADE
        jdbcTemplate.update("DELETE FROM pets WHERE id = ?", pet.getId());
    }

}
