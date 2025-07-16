package org.example.snowflakejava.repository;

import org.example.snowflakejava.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaSpecificationRepository extends JpaSpecificationExecutor<User>, JpaRepository<User, Long> {
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
