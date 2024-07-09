package javacode.test.jcapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * POJO represents an account entity in the application.
 */
@Data
@ToString
@Entity(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "uuid", columnDefinition = "UUID")
    private UUID uuid;
    private String name;
    @Version
    @UpdateTimestamp
    @Column(nullable = false)
    private BigDecimal account;

}
