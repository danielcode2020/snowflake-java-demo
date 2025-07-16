package org.example.snowflakejava.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "name") // name is unique
@Entity
@Table(name = "AUTHORITY", schema = "PUBLIC")
public class Authority {

    @Id
    private String name;

}
