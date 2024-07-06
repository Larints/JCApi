package javacode.test.jcapi.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a transfer request DTO.
 */
@Data
public class TransferRequest {
    private UUID uuid;
    private OperationType operationType;
    private BigDecimal amount;


    public enum OperationType {

        WITHDRAW,
        DEPOSIT

    }
}
