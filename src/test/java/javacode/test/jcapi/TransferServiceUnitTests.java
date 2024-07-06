package javacode.test.jcapi;

import javacode.test.jcapi.model.Account;
import javacode.test.jcapi.repository.AccountRepository;
import javacode.test.jcapi.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
public class TransferServiceUnitTests {

   @Mock
   private AccountRepository accountRepository;

   @InjectMocks
   private TransferService transferService;

   private final UUID accountId = UUID.randomUUID();

   private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setUuid(accountId);
        account.setName("Test Account");
        account.setAccount(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Happy flow money deposit test")
    public void moneyDepositHappyFlow() {
        BigDecimal depositAmount = new BigDecimal("50.00");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = transferService.transferMoney(accountId, "DEPOSIT", depositAmount);

        assertNotNull(updatedAccount);
        assertEquals(new BigDecimal("150.00"), updatedAccount.getAccount());
        verify(accountRepository).save(updatedAccount);
    }

    @Test
    @DisplayName("Happy flow money withdraw test")
    public void moneyWithdrawHappyFlow() {
        BigDecimal withdrawAmount = new BigDecimal("50.00");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        Account updatedAccount = transferService.transferMoney(accountId, "WITHDRAW", withdrawAmount);

        assertNotNull(updatedAccount);
        assertEquals(new BigDecimal("50.00"), updatedAccount.getAccount());
        verify(accountRepository).save(updatedAccount);
    }

    @Test
    @DisplayName("Insufficient funds test")
    public void moneyWithdrawInsufficientFunds() {
        BigDecimal withdrawAmount = new BigDecimal("150.00");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transferService.transferMoney(accountId, "WITHDRAW", withdrawAmount);
        });

        assertEquals("Insufficient funds. Cannot withdraw", exception.getMessage());
    }

    @Test
    @DisplayName("Account not found test")
    public void accountNotFound() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transferService.transferMoney(accountId, "DEPOSIT", new BigDecimal("50.00"));
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"UNSUPPORTED", "INVALID", "UNKNOWN"})
    @DisplayName("Unsupported operation type should throw IllegalStateException")
    public void unsupportedOperation(String operationType) {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        BigDecimal amount = new BigDecimal("50.00");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transferService.transferMoney(accountId, operationType, amount);
        });

        assertEquals("Unsupported operation type", exception.getMessage());
    }

}
