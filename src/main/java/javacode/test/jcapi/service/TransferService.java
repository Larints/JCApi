package javacode.test.jcapi.service;

import jakarta.persistence.OptimisticLockException;
import javacode.test.jcapi.DTO.TransferRequest;
import javacode.test.jcapi.exceptions.AccountNotFoundException;
import javacode.test.jcapi.model.Account;
import javacode.test.jcapi.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * Service class for handling account transfer operations.
 */
@Service
@AllArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository accountRepository;

    /**
     * Transfers money to or from an account based on operation type and amount.
     *
     * @param uuid         the UUID of the account involved in the transfer
     * @param operationType the type of operation (DEPOSIT or WITHDRAW)
     * @param amount       the amount to deposit or withdraw
     * @return the updated account information after the transfer
     * @throws RuntimeException     if the account with the given UUID is not found
     * @throws IllegalStateException if the operation type is not supported
     * @throws AccountNotFoundException if there are insufficient funds for a withdrawal operation
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(retryFor = OptimisticLockException.class, maxAttempts = 10)
    public Account transferMoney(String operationType, BigDecimal amount, Account account) {
        TransferRequest.OperationType type;
        try {
            type = TransferRequest.OperationType.valueOf(operationType);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unsupported operation type");
        }
        switch (type) {
            case DEPOSIT -> {
                account.setAccount(account.getAccount().add(amount));
                log.info("Account successfully updated: {}", account);
            }
            case WITHDRAW -> withdraw(amount, account);
        }

        return accountRepository.save(account);
    }

    /**
     * Helper method to withdraw money from an account.
     *
     * @param amount  the amount to withdraw
     * @param account the account from which to withdraw money
     * @throws AccountNotFoundException if there are insufficient funds for the withdrawal
     */
    private void withdraw(BigDecimal amount, Account account) {
        BigDecimal currentBalance = account.getAccount();
        BigDecimal newBalance = currentBalance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            account.setAccount(newBalance);
            log.info("Account successfully updated: {}", account);
        } else {
            log.error("Insufficient funds. Cannot withdraw from account: {}", account);
            throw new AccountNotFoundException("Insufficient funds. Cannot withdraw");
        }
    }

    /**
     * Retrieves account information for a given UUID.
     *
     * @param uuid the UUID of the account to retrieve
     * @return the account information
     * @throws RuntimeException if the account with the given UUID is not found
     */
    public Account getAccount(UUID uuid) {
        return accountRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Account not found"));
    }

}