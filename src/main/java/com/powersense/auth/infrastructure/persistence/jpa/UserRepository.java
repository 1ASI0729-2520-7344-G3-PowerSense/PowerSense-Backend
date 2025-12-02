package com.powersense.auth.infrastructure.persistence.jpa;

import com.powersense.auth.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por email (value object embeddable)
     * JPA genera automáticamente el query usando la notación de underscore
     * para acceder a propiedades embeddables
     */
    Optional<User> findByEmail_Value(String email);

    /**
     * Verifica si existe un usuario con el email dado
     */
    boolean existsByEmail_Value(String email);

    /**
     * Busca usuarios activos por email
     */
    Optional<User> findByEmail_ValueAndIsActiveTrue(String email);
}