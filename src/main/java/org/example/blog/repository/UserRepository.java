package org.example.blog.repository;

import org.example.blog.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    @Query("SELECT u FROM CustomUser u WHERE u.login = :login")
    CustomUser findByLogin(@Param("login") String login);

    boolean existsByLogin(String login);
}