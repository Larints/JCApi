package javacode.test.jcapi.service;

import javacode.test.jcapi.model.Account;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferServiceInterface {

    public Account transferMoney(UUID uuid, String operationType, BigDecimal amount);

    public Account getAccount(UUID uuid);


}
