package javacode.test.jcapi.repository;

import javacode.test.jcapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Account} entities in the database.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

}
