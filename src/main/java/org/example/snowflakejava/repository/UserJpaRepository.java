package org.example.snowflakejava.repository;

import org.example.snowflakejava.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query(value = "CALL PUBLIC.CALL_INT(:money)", nativeQuery = true)
    String callInt(@Param("money") Integer money);

    @Query(value = "call CALL_INT_ARRAY(:list);", nativeQuery = true)
    String callInt(@Param("list") Integer[] money);

    @Query(value = "call CREATE_USER_REQUEST_V1(:reason, :allocationPeriodType, :sourceIdYears, :months)", nativeQuery = true)
    String callCreateUserRequest(@Param("reason") String reason,
                                 @Param("allocationPeriodType") String allocationPeriodType,
                                 @Param("sourceIdYears") String sourceIdYears,
                                 @Param("months") String months);
}
