package io.sitoolkit.util.sbrs;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordRepository<T extends ResetPasswordEntity>
    extends CrudRepository<T, String> {
  Optional<T> findByAccountId(String accountId);
}
