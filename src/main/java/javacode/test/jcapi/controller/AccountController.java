package javacode.test.jcapi.controller;

import javacode.test.jcapi.DTO.TransferRequest;
import javacode.test.jcapi.model.Account;
import javacode.test.jcapi.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor

/*
 * Controller for handling account-related operations.
 */
public class AccountController {

    public final TransferService transferService;

    /**
     * Transfers money to or from an account.
     *
     * @param request the transfer request containing account UUID, operation type, and amount
     * @return the updated account information wrapped in a ResponseEntity
     */
    @PostMapping("/api/v1/wallet")
    public ResponseEntity<Account> transferMoney(@RequestBody TransferRequest request) {
        Account updatedAccount = transferService.transferMoney(request.getUuid(), request.getOperationType().toString(),
                request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Retrieves account information for a given account UUID.
     *
     * @param WALLET_UUID the UUID of the account to retrieve
     * @return the account information wrapped in a ResponseEntity
     */

    @GetMapping("api/v1/wallets/{WALLET_UUID}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID WALLET_UUID) {
        Account account = transferService.getAccount(WALLET_UUID);
        return ResponseEntity.ok(account);
    }

}
