package br.com.fiap.gastrosphere.core.infra.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.gastrosphere.core.infra.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByAddressId(UUID id);
    boolean existsByUserType_Id(UUID userTypeId);

    @Modifying
    @Query("UPDATE UserModel u " +
            "SET u.password = :newPassword, u.lastModifiedAt = :lastModifiedAt " +
            "WHERE u.id = :id AND u.password = :oldPassword")
    Integer updatePassword(@Param("id") UUID id,
                           @Param("oldPassword") String oldPassword,
                           @Param("newPassword") String newPassword,
                           @Param("lastModifiedAt") LocalDate lastModifiedAt);

}
