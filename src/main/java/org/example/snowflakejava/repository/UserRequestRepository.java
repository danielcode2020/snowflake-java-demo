package org.example.snowflakejava.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Row;
import com.snowflake.snowpark_java.Session;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeSQLException;
import org.example.snowflakejava.web.controller.dto.SourceAndYearDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRequestRepository {

    @Qualifier("snowflakeJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    private final Session snowflakeSession;

    private final ObjectMapper objectMapper;

    public UserRequestRepository(JdbcTemplate jdbcTemplate, Session snowflakeSession) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowflakeSession = snowflakeSession;
        this.objectMapper = new ObjectMapper();
    }

    public UUID callString(String value){
        // stored procedure should contain schema name like : <schema_name>.<procedure_name>
        DataFrame dataFrame = snowflakeSession.storedProcedure("PUBLIC.CALL_STRING", value);
        Row row = dataFrame.collect()[0];
        return UUID.fromString(row.getAs(0,String.class));
    }

    public UUID callInt(int value){
        try {
            return getUuid(value);
        } catch (SnowflakeSQLException e){
            System.err.println(e.getErrorCode());  // --20002
            System.err.println(e.getMessage());   //  Uncaught exception of type 'MY_EXCEPTION' on line 6 at position 8 : CUSTOM_EXCEPTION
            throw new RuntimeException(e.getMessage());
        }
    }

    private UUID getUuid(int value) throws SnowflakeSQLException{
        DataFrame dataFrame = snowflakeSession.storedProcedure("PUBLIC.CALL_INT", value);
        Row row = dataFrame.collect()[0];
        return UUID.fromString(row.getAs(0, String.class));
    }


    public UUID callIntArrayStoredProcedure(int[] ints) {
        // Changing the type of int[] to String[] still does not help, nor List<Integer>
        // Documentation says that String[] should map directly to ARRAY in snowflake
        // https://docs.snowflake.com/en/developer-guide/udf-stored-procedure-data-type-mapping#label-sql-java-data-type-mappings
        DataFrame dataFrame = snowflakeSession.storedProcedure("PUBLIC.CALL_INT_ARRAY", ints);
        Row row = dataFrame.collect()[0];
        return UUID.fromString(row.getAs(0,String.class));
    }

    public UUID callIntArraySql(int[] ints) {
        String arrayElements = Arrays.stream(ints)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        DataFrame dataFrame = snowflakeSession.sql("CALL PUBLIC.CALL_INT_ARRAY(ARRAY_CONSTRUCT(%s))".formatted(arrayElements));
        Row row = dataFrame.collect()[0];
        return UUID.fromString(row.getAs(0,String.class));
    }


    public UUID createUserRequestStoredProcedure(String reason, String allocationPeriodType,
                                                 List<SourceAndYearDto> sourceIdYears, List<Integer> months)
            throws JsonProcessingException {
        Row[] rows  = snowflakeSession.storedProcedure("PUBLIC.CREATE_USER_REQUEST_V1",
                reason,
                allocationPeriodType,
                objectMapper.writeValueAsString(sourceIdYears),  // serialize to Json String
                objectMapper.writeValueAsString(months))         // serialize to Json String
                .collect();
        Row row = rows[0];
        return UUID.fromString(row.getAs(0, String.class));
    }

    // tried to call create user request using sql native query
    public UUID createUserRequestSql(String reason, String allocationPeriodType,
                                                 List<SourceAndYearDto> sourceIdYears, List<Integer> months)
            throws JsonProcessingException {
        Row[] rows  = snowflakeSession.sql(
                String.format("CALL PUBLIC.CREATE_USER_REQUEST_V1(%s,%s,ARRAY_CONSTRUCT(%s),ARRAY_CONSTRUCT(%s))",
                        reason,
                        allocationPeriodType,
                        objectMapper.writeValueAsString(sourceIdYears),
                        objectMapper.writeValueAsString(months)
                ))
                .collect();
        Row row = rows[0];
        return UUID.fromString(row.getAs(0, String.class));
    }
}
