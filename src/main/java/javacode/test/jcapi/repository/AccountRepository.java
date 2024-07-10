package javacode.test.jcapi.repository;

import jakarta.persistence.LockModeType;
import javacode.test.jcapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Account} entities in the database.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Override
    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findById(@NonNull UUID uuid);
}
