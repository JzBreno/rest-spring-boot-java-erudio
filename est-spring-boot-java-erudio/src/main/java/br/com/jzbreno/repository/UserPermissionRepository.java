package br.com.jzbreno.repository;

import br.com.jzbreno.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPermissionRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :userName")
    Optional<User> findByUsername(@Param("userName") String userName);

}
