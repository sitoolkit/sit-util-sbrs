package io.sitoolkit.util.sbrs;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository<T extends AccountEntity> extends CrudRepository<T, String> {
  Optional<T> findByMailAddress(String mailAddress);
}
